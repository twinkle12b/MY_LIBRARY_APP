package com.react2code.springbootlibrary.requestModel;

import lombok.Data;

@Data
public class PaymentInfoRequest {

    private int amount;
    private String currency;
    private String receiptEmail;

}
