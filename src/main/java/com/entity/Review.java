package com.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Review extends Entity {
    private int id;
    private int userId;
    private int activityId;
    private Timestamp creationDate;
    private String message;
    private int mark;
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityId=" + activityId +
                ", creationDate=" + creationDate +
                ", message='" + message +
                ", mark=" + mark +
                ", userLogin=" + userLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id &&
                userId == review.userId &&
                activityId == review.activityId &&
                mark == review.mark &&
                Objects.equals(creationDate, review.creationDate) &&
                Objects.equals(message, review.message) &&
                Objects.equals(userLogin, review.userLogin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, activityId, creationDate, message, mark, userLogin);
    }
}
