package com.entity;

import java.sql.Timestamp;

public class ReviewBuilder {
    private int id;
    private int userId;
    private int activityId;
    private Timestamp creationDate;
    private String message;
    private int mark;
    private String userLogin;

    public ReviewBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public ReviewBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public ReviewBuilder setActivityId(int activityId) {
        this.activityId = activityId;
        return this;
    }

    public ReviewBuilder setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ReviewBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ReviewBuilder setMark(int mark) {
        this.mark = mark;
        return this;
    }

    public ReviewBuilder setUserLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public Review create() {
        Review review = new Review();
        review.setId(id);
        review.setCreationDate(creationDate);
        review.setUserId(userId);
        review.setActivityId(activityId);
        review.setMark(mark);
        review.setMessage(message);
        review.setUserLogin(userLogin);
        return review;
    }
}
