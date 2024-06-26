package com.ssafy.triptogether.flashmob.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.auth.utils.SecurityMember;
import com.ssafy.triptogether.auth.validator.flashmobmember.FlashMobMemberVerify;
import com.ssafy.triptogether.auth.validator.pin.PinVerify;
import com.ssafy.triptogether.chat.util.ChatMessageUtil;
import com.ssafy.triptogether.flashmob.data.request.ApplyFlashmobRequest;
import com.ssafy.triptogether.flashmob.data.request.AttendeesReceiptDetail;
import com.ssafy.triptogether.flashmob.data.request.SettlementSaveAttendeesDetail;
import com.ssafy.triptogether.flashmob.data.request.SettlementSaveRequest;
import com.ssafy.triptogether.flashmob.data.response.*;
import com.ssafy.triptogether.flashmob.domain.*;
import com.ssafy.triptogether.flashmob.domain.document.Receipt;
import com.ssafy.triptogether.flashmob.domain.document.ReceiptHistory;
import com.ssafy.triptogether.flashmob.repository.*;
import com.ssafy.triptogether.flashmob.utils.FlashMobUtils;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.exceptions.category.ForbiddenException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.domain.RoomStatus;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberFlashmobUtils;
import com.ssafy.triptogether.member.utils.MemberUtils;
import com.ssafy.triptogether.tripaccount.concurrency.DistributedLock;
import com.ssafy.triptogether.tripaccount.data.request.AccountHistorySaveRequest;
import com.ssafy.triptogether.tripaccount.data.request.PaymentReceiverDetail;
import com.ssafy.triptogether.tripaccount.data.request.PaymentSenderDetail;
import com.ssafy.triptogether.tripaccount.domain.Currency;
import com.ssafy.triptogether.tripaccount.domain.TripAccount;
import com.ssafy.triptogether.tripaccount.domain.Type;
import com.ssafy.triptogether.tripaccount.provider.AccountHistoryProvider;
import com.ssafy.triptogether.tripaccount.repository.CurrencyRepository;
import com.ssafy.triptogether.tripaccount.repository.TripAccountRepository;
import com.ssafy.triptogether.tripaccount.utils.TripAccountUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class FlashMobServiceImpl implements FlashMobSaveService, FlashMobLoadService {
	// Repository
	private final FlashMobRepository flashMobRepository;
	private final MemberFlashMobRepository memberFlashMobRepository;
	private final MemberRepository memberRepository;
	private final SettlementRepository settlementRepository;
	private final RequesterSettlementRepository requesterSettlementRepository;
	private final ParticipantSettlementRepository participantSettlementRepository;
	private final ReceiptRepository receiptRepository;
	private final TripAccountRepository tripAccountRepository;
	private final CurrencyRepository currencyRepository;
	// Provider
	private final AccountHistoryProvider accountHistoryProvider;
	private final ChatMessageUtil chatMessageUtil;

	@Transactional
	@Override
	public void sendAttendanceRequest(long flashmobId, long memberId) {
		// find member & flashmob
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);
		FlashMob flashMob = flashMobRepository.findById(flashmobId)
			.orElseThrow(() -> new NotFoundException("SendAttendanceRequest", UNDEFINED_FLASHMOB));

		// blacklist prevention
		if (member.getReportCount() >= 5) {
			throw new BadRequestException("sendAttendanceRequest", BLACKLIST_MEMBER);
		}

		// double click prevention
		if (memberFlashMobRepository.existsByFlashMobIdAndMemberId(flashmobId, memberId)) {
			throw new BadRequestException("sendAttendanceRequest", MEMBER_FLASHMOB_EXIST);
		}

		// create member flashmob & save
		MemberFlashMob memberFlashMob = MemberFlashMob.builder()
			.isMaster(false)
			.roomStatus(RoomStatus.WAIT)
			.member(member)
			.flashMob(flashMob)
			.build();
		memberFlashMobRepository.save(memberFlashMob);

		// send chat message
		chatMessageUtil.sendAttendMsg(flashmobId, memberId, member.getNickname(), member.getImageUrl());
	}

	@Transactional
	@Override
	public void checkDeniedFlashmob(long flashmobId, long memberId) {
		// find member flashmob
		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);

		// check room status
		memberFlashMob.checkDenial();
	}

	@Transactional
	@Override
	public void cancelFlashmob(long flashmobId, long memberId) {
		// find member flashmob
		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);

		// cancel attendance request
		memberFlashMobRepository.delete(memberFlashMob);
	}

	@Transactional
	@Override
	public RequestMemberResponse applyFlashmob(long flashmobId, long memberId,
		ApplyFlashmobRequest applyFlashmobRequest, long masterId) {
		// check if master
		boolean isMaster = memberFlashMobRepository.isMaster(flashmobId, masterId);
		if (!isMaster) {
			throw new ForbiddenException("applyFlashmob", MEMBER_NOT_MASTER, masterId);
		}

		// find member flashmob & member
		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(memberFlashMobRepository,
			flashmobId, memberId);
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		// apply & send message
		boolean isAccepted = false;
		if (applyFlashmobRequest.status().equals(RoomStatus.ATTEND)) {
			memberFlashMob.applyAcceptance();
			chatMessageUtil.sendJoinMsg(flashmobId, memberId, memberFlashMob.getMember().getNickname(),
				memberFlashMob.getMember().getImageUrl());
			isAccepted = true;
		} else if (applyFlashmobRequest.status().equals(RoomStatus.REFUSE_UNCHECK)) {
			memberFlashMob.applyDenial();
		} else {
			throw new BadRequestException("applyFlashmob", BAD_STATUS_REQUEST, applyFlashmobRequest.status());
		}

		// create & return response
		return RequestMemberResponse.builder()
			.memberId(member.getId())
			.memberProfileImageUrl(member.getImageUrl())
			.nickname(member.getNickname())
			.isAccepted(isAccepted)
			.build();
	}

	@Transactional
	@Override
	public void exitFlashmob(SecurityMember securityMember, long flashmobId) {
		long memberCnt = memberFlashMobRepository.countMemberFlashMobsByFlashMob_Id(flashmobId);
		if (memberCnt == 1L) {
			flashMobRepository.deleteById(flashmobId);
			return;
		}

		MemberFlashMob memberFlashMob = MemberFlashmobUtils.findByFlashmobIdAndMemberId(
			memberFlashMobRepository, flashmobId, securityMember.getId());
		if (memberFlashMob.getIsMaster()) {
			MemberFlashMob nextMaster = MemberFlashmobUtils.findByFlashmobIdNotInMemberId(
				memberFlashMobRepository, flashmobId, securityMember.getId());
			// 다른 멤버 찾아서 방장으로 바꿈
			nextMaster.memberToMaster();
		}
		// 멤버를 repo.delete
		memberFlashMobRepository.delete(memberFlashMob);
	}

	/**
	 * 정산 요청
	 * @param memberId 요청자 member_id
	 * @param flashmobId 발생한 플래시몹 flashmob_id
	 * @param settlementSaveRequest 정산 요청 내용
	 */
	@FlashMobMemberVerify
	@Transactional
	@Override
	public void settlementSave(long memberId, long flashmobId, SettlementSaveRequest settlementSaveRequest) {
		Member requester = MemberUtils.findByMemberId(memberRepository, memberId);
		FlashMob flashMob = FlashMobUtils.findByFlashmobId(flashMobRepository, flashmobId);
		Settlement settlement = makeSettlement(settlementSaveRequest, flashMob);
		RequesterSettlement requesterSettlement = RequesterSettlement.builder()
			.member(requester)
			.settlement(settlement)
			.build();
		requesterSettlementRepository.save(requesterSettlement);

		settlementSaveRequest.attendeesDetails()
			.forEach(attendeesDetail -> {
				Member sender = MemberUtils.findByMemberId(memberRepository, attendeesDetail.memberId());
				ParticipantSettlement participantSettlement = ParticipantSettlement.builder()
					.price(attendeesDetail.memberPrice())
					.hasSent(false)
					.member(sender)
					.settlement(settlement)
					.build();
				participantSettlementRepository.save(participantSettlement);
				makeReceipt(attendeesDetail, participantSettlement);
			});
		chatMessageUtil.sendSettlementMsg(flashmobId, memberId, requester.getNickname(), requester.getImageUrl(),
			String.format("{ \"settlement_id\": \"%d\", \"currency_code\": \"%s\"}",
				settlement.getId(),
				settlementSaveRequest.currencyCode()));
	}

	/**
	 * 정산 요청 송금
	 * @param memberId 송금자 member_id
	 * @param flashmobId 플래시몹 flashmob_id
	 * @param settlementId 정산 요청 settlement_id
	 * @param pinVerifyRequest PIN 인증 요청
	 */
	@FlashMobMemberVerify
	@DistributedLock(key = "'settlement:' + #flashmobId + ':' + #settlementId")
	@Transactional
	@Override
	public void settlementSend(long memberId, long flashmobId, long settlementId, PinVerifyRequest pinVerifyRequest) {
		Member participantMember = MemberUtils.findByMemberId(memberRepository, memberId);
		Settlement settlement = settlementRepository.findById(settlementId)
			.orElseThrow(
				() -> new NotFoundException("SettlementSend", SETTLEMENT_NOT_FOUND)
			);
		Currency currency = TripAccountUtils.findByCurrencyCode(currencyRepository, settlement.getCurrencyCode());
		ParticipantSettlement participantSettlement = participantSettlementRepository.participantFindByMemberIdAndSettlementId(
			memberId, settlementId);
		if (participantSettlement.getHasSent()) {
			throw new BadRequestException("SettlementSend", SETTLEMENT_SEND_BAD_REQUEST);
		}
		TripAccount participantTripAccount = participantWithdraw(memberId, pinVerifyRequest, currency,
			participantSettlement);
		participantSettlement.settlementSend();

		Member requesterMember = requesterSettlementRepository.requesterFindBySettlementId(settlementId);
		Optional<TripAccount> requesterTripAccount = TripAccountUtils.findReqeusterTripAccount(tripAccountRepository,
			requesterMember.getId(), settlementId);
		requesterTripAccount.ifPresentOrElse(
			tripAccount -> {
				tripAccount.depositBalance(String.valueOf(participantSettlement.getPrice()));
				PaymentReceiverDetail paymentReceiverDetail = PaymentReceiverDetail.builder()
					.tripAccount(tripAccount)
					.address("역삼동")
					.businessName(participantMember.getNickname())
					.businessNum(participantMember.getUuid())
					.quantity(participantSettlement.getPrice())
					.type(Type.DEPOSIT)
					.build();
				PaymentSenderDetail paymentSenderDetail = PaymentSenderDetail.builder()
					.tripAccount(participantTripAccount)
					.address("역삼동")
					.businessName(requesterMember.getNickname())
					.businessNum(requesterMember.getUuid())
					.quantity(participantSettlement.getPrice())
					.type(Type.WITHDRAW)
					.build();
				AccountHistorySaveRequest accountHistorySaveRequest = AccountHistorySaveRequest.builder()
					.paymentReceiverDetail(paymentReceiverDetail)
					.paymentSenderDetail(paymentSenderDetail)
					.build();
				accountHistoryProvider.accountHistoryMaker(accountHistorySaveRequest);
			},
			() -> {
				TripAccount tripAccount  = TripAccount.builder()
					.balance("0")
					.member(requesterMember)
					.currency(currency)
					.build();
				TripAccount savedTripAccount = tripAccountRepository.save(tripAccount);
				savedTripAccount.depositBalance(String.valueOf(participantSettlement.getPrice()));
				PaymentReceiverDetail paymentReceiverDetail = PaymentReceiverDetail.builder()
					.tripAccount(savedTripAccount)
					.address("역삼동")
					.businessName(participantMember.getNickname())
					.businessNum(participantMember.getUuid())
					.quantity(participantSettlement.getPrice())
					.type(Type.DEPOSIT)
					.build();
				PaymentSenderDetail paymentSenderDetail = PaymentSenderDetail.builder()
					.tripAccount(participantTripAccount)
					.address("역삼동")
					.businessName(requesterMember.getNickname())
					.businessNum(requesterMember.getUuid())
					.quantity(participantSettlement.getPrice())
					.type(Type.WITHDRAW)
					.build();
				AccountHistorySaveRequest accountHistorySaveRequest = AccountHistorySaveRequest.builder()
					.paymentReceiverDetail(paymentReceiverDetail)
					.paymentSenderDetail(paymentSenderDetail)
					.build();
				accountHistoryProvider.accountHistoryMaker(accountHistorySaveRequest);
			});

		if (participantSettlementRepository.checkSettlementIsDone(settlementId)) {
			settlement.updateIsDone();
		}
	}

	@PinVerify
	private TripAccount participantWithdraw(long memberId, PinVerifyRequest pinVerifyRequest, Currency currency,
		ParticipantSettlement participantSettlement) {
		TripAccount participantTripAccount = TripAccountUtils.findByMemberIdAndCurrencyId(tripAccountRepository,
			memberId, currency.getId());
		participantTripAccount.withdrawBalance(String.valueOf(participantSettlement.getPrice()));
		return participantTripAccount;
	}

	/**
	 * 정산 요청 취소
	 * @param memberId 요청자 memberId
	 * @param flashmobId 플래시몹 flashmob_id
	 * @param settlementId 정산 요청 settlement_id
	 */
	@FlashMobMemberVerify
	@Transactional
	@Override
	public void settlementDelete(long memberId, long flashmobId, long settlementId) {
		Settlement settlement = requesterSettlementRepository.settlementFindByRequesterIdAndSettlementId(memberId,
			settlementId);
		settlementRepository.delete(settlement);
	}

	private void makeReceipt(SettlementSaveAttendeesDetail attendeesDetail, MemberSettlement participantSettlement) {
		List<ReceiptHistory> receiptHistories = attendeesDetail.receiptDetails()
			.stream().map(attendeesReceiptDetail ->
				ReceiptHistory.builder()
					.price(attendeesReceiptDetail.price())
					.businessName(attendeesReceiptDetail.businessName())
					.createdAt(attendeesReceiptDetail.createdAt())
					.build()
			).toList();
		Receipt receipt = Receipt.builder()
			.memberSettlementId(participantSettlement.getId())
			.receiptHistories(receiptHistories)
			.build();
		receiptRepository.save(receipt);
	}

	private Settlement makeSettlement(SettlementSaveRequest settlementSaveRequest, FlashMob flashMob) {
		Settlement settlement = Settlement.builder()
			.currencyCode(settlementSaveRequest.currencyCode())
			.attendanceCount(settlementSaveRequest.attendeesCount())
			.totalPrice(settlementSaveRequest.totalPrice())
			.flashMob(flashMob)
			.build();
		settlementRepository.save(settlement);
		return settlement;
	}

	@Override
	public AttendingFlashmobListFindResponse findAttendingFlashmobList(long memberId) {
		// find attending flashmobs
		List<AttendingFlashmobFindResponse> elements = flashMobRepository.findAllAttendingFlashmobElementsByMemberId(
			memberId);

		// create response & return
		return AttendingFlashmobListFindResponse.builder().elements(elements).build();
	}

	/**
	 * 플래시몹 내 정산 목록 조회
	 * @param memberId 요청자 member_id
	 * @param flashmobId 플래시몹 flashmob_id
	 * @return 정산 목록
	 */
	@FlashMobMemberVerify
	@Override
	public SettlementsLoadResponse settlementsLoad(long memberId, long flashmobId) {
		List<RequesterSettlementsLoadDetail> byRequester = settlementRepository.findByRequester(memberId);
		List<ParticipantSettlementsLoadDetail> byParticipant = settlementRepository.findByParticipant(memberId);
		return SettlementsLoadResponse.builder()
			.requesterSettlementsLoadDetails(byRequester)
			.participantSettlementsLoadDetails(byParticipant)
			.build();
	}

	/**
	 * 정산 내역 상세 조회
	 * @param memberId 요청자 member_id
	 * @param flashmobId 플래시몹 flashmob_id
	 * @param settlementId 정산 settlement_id
	 * @return 정산 내역 상세
	 */
	@FlashMobMemberVerify
	@Override
	public AttendeeReceiptsResponse receiptsLoad(long memberId, long flashmobId, long settlementId) {
		ParticipantSettlement participantSettlement = participantSettlementRepository.participantFindByMemberIdAndSettlementId(
			memberId,
			settlementId);
		Receipt receipt = receiptRepository.findByMemberSettlementId(participantSettlement.getId())
			.orElseThrow(
				() -> new NotFoundException("ReceiptsLoad", RECEIPT_NOT_FOUND)
			);
		List<AttendeesReceiptDetail> attendeesReceiptDetails = receipt.getReceiptHistories()
			.stream()
			.map(
				receiptHistory -> AttendeesReceiptDetail.builder()
					.price(receiptHistory.price())
					.businessName(receiptHistory.businessName())
					.createdAt(receiptHistory.createdAt())
					.build()
			).toList();
		return AttendeeReceiptsResponse.builder()
			.price(participantSettlement.getPrice())
			.hasSent(participantSettlement.getHasSent())
			.attendeesReceiptDetails(attendeesReceiptDetails)
			.build();
	}

	/**
	 * 정산 현황 조회
	 * @param memberId 요청자 member_id
	 * @param flashmobId 플래시몹 flashmob_id
	 * @param settlementId 정산요청 settlement_id
	 * @return 정산 현황
	 */
	@FlashMobMemberVerify
	@Override
	public AttendeesStatusResponse attendeesStatusLoad(long memberId, long flashmobId, long settlementId) {
		List<AttendeesStatusDetail> attendeesStatusDetails = requesterSettlementRepository.checkParticipantsStatus(
			memberId, settlementId);
		return AttendeesStatusResponse.builder()
			.attendeesStatusDetails(attendeesStatusDetails)
			.build();
	}

	@FlashMobMemberVerify
	@Override
	public FlashMobMembersLoadResponse flashmobMembersLoad(long memberId, long flashmobId) {
		List<FlashMobMemberDetail> allMemberInFlashMob = flashMobRepository.findAllMemberInFlashMob(flashmobId);
		return FlashMobMembersLoadResponse.builder()
			.flashMobMemberDetails(allMemberInFlashMob)
			.build();
	}
}
