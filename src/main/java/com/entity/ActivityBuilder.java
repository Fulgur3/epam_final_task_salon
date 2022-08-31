package com.entity;

import java.math.BigDecimal;

public class ActivityBuilder {
    private int id;
    private BigDecimal price;
    private String name;
    private String description;
    private Activity.Status status;

    public ActivityBuilder setId(int id){
        this.id=id;
        return this;
    }
    public ActivityBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ActivityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ActivityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ActivityBuilder setStatus(Activity.Status status) {
        this.status = status;
        return this;
    }

    public ActivityBuilder setStatus(String status) {
        for (Activity.Status st : Activity.Status.values()) {
            if (st.name().equalsIgnoreCase(status)) {
                this.status = st;
            }
        }
        return this;
    }

    public Activity create() {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setDescription(description);
        activity.setPrice(price);
        activity.setStatus(status);
        return activity;
    }
}
