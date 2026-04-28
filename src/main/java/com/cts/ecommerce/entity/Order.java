package com.cts.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class Order {
    private int orderId;
    private int userId;
    private double totalPrice;
    private int shippingAddressId;
    private String orderStatus;
    private String paymentStatus;
    private int shoppingCartId;

    public Order(){

    }

    public Order(int userId, double totalPrice, int shippingAddressId, String orderStatus, String paymentStatus, int shoppingCartId) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.shippingAddressId = shippingAddressId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.shoppingCartId = shoppingCartId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalPrice=" + totalPrice +
                ", shippingAddressId=" + shippingAddressId +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", shoppingCartId=" + shoppingCartId +
                '}';
    }
}
