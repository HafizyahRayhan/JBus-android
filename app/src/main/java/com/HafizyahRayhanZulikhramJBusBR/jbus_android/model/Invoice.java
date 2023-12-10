package com.HafizyahRayhanZulikhramJBusBR.jbus_android.model;

public class Invoice extends Serializable {

    public PaymentStatus status;

    public enum PaymentStatus {

        FAILED,

        WAITING,

        SUCCESS
    }
}

