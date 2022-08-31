package com.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Activity extends Entity {
    public enum Status {
    AVAILABLE("available"), UNAVAILABLE("unavailable");
        private String statusName;
        Status(String statusName){this.statusName=statusName;}

        public String getName(){return statusName;}
    }

    private int id;
    private BigDecimal price;
    private String name;
    private String description;
    private Status status;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public BigDecimal getPrice(){return price;}

    public void setPrice(BigDecimal price){this.price=price;}

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description=description;}

    public String getStatus(){return status.getName();}

    public void setStatus(Status status){this.status=status;}

    public void setStatus(String status){
        for(Status st: Status.values()){
            if(st.name().equalsIgnoreCase(status)){
                this.status=st;
            }
        }
    }

    @Override
    public String toString(){
        return "Activity{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id &&
                Objects.equals(price, activity.price) &&
                Objects.equals(name, activity.name) &&
                Objects.equals(description, activity.description) &&
                status == activity.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, price, name, description, status);
    }
}


