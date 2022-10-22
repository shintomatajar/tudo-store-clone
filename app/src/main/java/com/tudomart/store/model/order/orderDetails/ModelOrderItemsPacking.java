package com.tudomart.store.model.order.orderDetails;

public class ModelOrderItemsPacking {
    String imageUrl;
    String itemQuantity;
    String itemPrice;
    String itemName;
    String itemUnit;
    Boolean isFrozenFoode, isItemSelecetd;
    String barcode;
    String stock;
    String blnCheck;
    String productId;
    String orderId;
    Boolean isOutOfStock = false;
    String fkSubCategoryId = "";
    Boolean blnSubstitute;
    String fkCartId;

    public String getFkCartId() {
        return fkCartId;
    }

    public String getFkSubCategoryId() {
        return fkSubCategoryId;
    }

    public Boolean getBlnSubstitute() {
        return blnSubstitute;
    }

    public String getfkSubCategoryId() {
        return fkSubCategoryId;
    }


    public Boolean getOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        isOutOfStock = outOfStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Boolean getFrozenFoode() {
        return isFrozenFoode;
    }

    public void setFrozenFoode(Boolean frozenFoode) {
        isFrozenFoode = frozenFoode;
    }

    public Boolean getItemSelecetd() {
        return isItemSelecetd;
    }

    public void setItemSelecetd(Boolean itemSelecetd) {
        isItemSelecetd = itemSelecetd;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getBlnCheck() {
        return blnCheck;
    }

    public void setBlnCheck(String blnCheck) {
        this.blnCheck = blnCheck;
    }

    public String getProductId() {
        return productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public ModelOrderItemsPacking(String imageUrl, String itemQuantity, String itemPrice, String itemName, String itemUnit, Boolean isFrozenFoode, Boolean isItemSelecetd, String barcode, String stock, String blnCheck, String productId, String orderId, String fkSubCategoryId, Boolean blnSubstitute, String fkCartId) {
        this.imageUrl = imageUrl;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.isFrozenFoode = isFrozenFoode;
        this.isItemSelecetd = isItemSelecetd;
        this.barcode = barcode;
        this.stock = stock;
        this.blnCheck = blnCheck;
        this.productId = productId;
        this.orderId = orderId;
        this.fkSubCategoryId = fkSubCategoryId;
        this.blnSubstitute = blnSubstitute;
        this.fkCartId = fkCartId;
    }
}
