package com.ssafy.triptogether.syncaccount.service;

import com.ssafy.triptogether.syncaccount.data.response.BankAccountsLoadResponse;
import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsLoadResponse;

public interface SyncAccountLoadService {
    SyncAccountsLoadResponse syncAccountsLoad(Long memberId);

    BankAccountsLoadResponse bankAccountsLoad(Long memberId);
}
