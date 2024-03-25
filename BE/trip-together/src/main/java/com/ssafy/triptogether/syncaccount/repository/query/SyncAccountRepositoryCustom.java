package com.ssafy.triptogether.syncaccount.repository.query;

import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsDetail;

import java.util.List;

public interface SyncAccountRepositoryCustom {
    List<SyncAccountsDetail> memberSyncAccountsLoad(long memberId);

    Boolean memberSyncAccountExist(long memberId);
}
