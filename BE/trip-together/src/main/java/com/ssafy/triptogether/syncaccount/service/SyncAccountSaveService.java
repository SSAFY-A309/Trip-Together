package com.ssafy.triptogether.syncaccount.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.syncaccount.data.request.MainSyncAccountUpdateRequest;
import com.ssafy.triptogether.syncaccount.data.request.SyncAccountDeleteRequest;
import com.ssafy.triptogether.syncaccount.data.request.SyncAccountSaveRequest;

public interface SyncAccountSaveService {
    void mainSyncAccountUpdate(Long memberId, MainSyncAccountUpdateRequest mainSyncAccountUpdateRequest);

    void syncAccountSave(Long memberId, PinVerifyRequest pinVerifyRequest, SyncAccountSaveRequest syncAccountSaveRequest);

    void syncAccountDelete(Long memberId, PinVerifyRequest pinVerifyRequest, SyncAccountDeleteRequest syncAccountDeleteRequest);
}
