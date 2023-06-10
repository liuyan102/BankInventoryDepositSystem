package com.innovation.Service;

import com.innovation.vo.AccountShowingInformation;
import com.innovation.vo.CustomerTransferData;

public interface CustomerTransactionService {
    AccountShowingInformation getAccountShowingInformation(String idNumber);
    String transferRequestFeedback(CustomerTransferData customerTransferData);
    String getReceiveName(String bankName,String ReceiveAccount);
}
