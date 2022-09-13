package com.tudomart.store.model.order.orderDetails;

public class ModelOrderItems {
    String imageUrl;
    String itemQuantity;
    String itemPrice;
    String itemName;
    String itemUnit;
    Boolean isFrozenFoode;
    String stock;
    String barcode;
    String blnCheck;

    public ModelOrderItems(String imageUrl, String itemQuantity, String itemPrice, String itemName, String itemUnit, Boolean isFrozenFoode, String stock, String barcode, String blnCheck) {
        this.imageUrl = imageUrl;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.isFrozenFoode = isFrozenFoode;
        this.stock = stock;
        this.barcode = barcode;
        this.blnCheck = blnCheck;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public Boolean getFrozenFoode() {
        return isFrozenFoode;
    }

    public String getStock() {
        return stock;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBlnCheck() {
        return blnCheck;
    }
}
