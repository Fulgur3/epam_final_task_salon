package com.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class Order extends Entity {
    public enum Status {
        PENDING("pending"), CONFIRMED("confirmed"), CANCELLED("cancelled"), FINISHED("finished");
        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private int id;
    private int userId;
    private List<Activity> activityList = new ArrayList<>();
    private Status status;
    private Timestamp dateTime;
    private BigDecimal price;
    private boolean paid;
    private String userLogin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int activityListSize() {
        return activityList.size();
    }

    public boolean addActivity(Activity activity) {
        return activityList.add(activity);
    }

    public Activity getActivity(int index) {
        return activityList.get(index);
    }

    public List<Activity> getActivityList() {
        return Collections.unmodifiableList(activityList);
    }

    public String getStatus() {
        return status.getName();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        for (Status st : Status.values()) {
            if (st.name().equalsIgnoreCase(status)) {
                this.status = st;
            }
        }
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean getPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityList=" + activityList +
                ", status=" + status +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", paid=" + paid +
                ", userLogin=" + userLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                userId == order.userId &&
                Objects.equals(activityList, order.activityList) &&
                status == order.status &&
                Objects.equals(dateTime, order.dateTime) &&
                Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, activityList, status, dateTime, price);
    }
}
