package com.tudomart.store.model.order;

public class ModelOrderList {
    String orderId;
    String paymentType;
    String amount;
    String itemsCount;
    int status;
    String duration;
    String timeStamp;
    String timeSlot;
    String deliveryType;
    String strPlace;
    String strEmirate;

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getAmount() {
        if (amount.equalsIgnoreCase("null")) {
            return "0";
        }
        if (amount != null) {
            return amount;
        } else {
            return "0";
        }
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public int getStatus() {
        return status;
    }

    public String getDuration() {
        return duration;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getStrPlace() {
        return strPlace;
    }

    public String getStrEmirate() {
        return strEmirate;
    }

    public ModelOrderList(String orderId, String paymentType, String amount, String itemsCount, int status, String duration, String timeStamp, String timeSlot, String deliveryType, String strPlace, String strEmirate) {
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.itemsCount = itemsCount;
        this.status = status;
        this.duration = duration;
        this.timeStamp = timeStamp;
        this.timeSlot = timeSlot;
        this.deliveryType = deliveryType;
        this.strPlace = strPlace;
        this.strEmirate = strEmirate;
    }
}
