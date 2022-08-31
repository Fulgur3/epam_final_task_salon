package com.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private int id;
    private int userId;
    private List<Activity> activityList = new ArrayList<>();
    private Order.Status status;
    private Timestamp dateTime;
    private BigDecimal price;
    private boolean paid;
    private String userLogin;

    public OrderBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public OrderBuilder setStatus(Order.Status status) {
        this.status = status;
        return this;
    }

    public OrderBuilder setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public OrderBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OrderBuilder setPaid(boolean paid) {
        this.paid = paid;
        return this;
    }

    public OrderBuilder setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
        return this;
    }

    public OrderBuilder setStatus(String status) {
        for (Order.Status st : Order.Status.values()) {
            if (st.name().equalsIgnoreCase(status)) {
                this.status = st;
            }
        }
        return this;
    }

    public OrderBuilder setUserLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public Order create() {
        Order order = new Order();
        order.setId(id);
        order.setUserId(userId);
        order.setStatus(status);
        order.setPrice(price);
        order.setDateTime(dateTime);
        order.setPaid(paid);
        order.setUserLogin(userLogin);
        for (Activity activity : activityList) {
            order.addActivity(activity);
        }
        return order;
    }
}