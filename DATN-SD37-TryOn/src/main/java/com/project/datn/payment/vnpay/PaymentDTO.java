package com.project.datn.payment.vnpay;

import lombok.Builder;
import lombok.Getter;

public abstract class PaymentDTO {
    @Builder
    @Getter
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
