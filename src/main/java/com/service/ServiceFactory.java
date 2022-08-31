package com.service;


public class ServiceFactory {
    private static ServiceFactory instance =new ServiceFactory();
    private OrderService orderService= new OrderService();
    private UserService userService = new UserService();
    private ActivityService activityService= new ActivityService();
    private ReviewService reviewService= new ReviewService();

    public static ServiceFactory getInstance(){return instance;}

    public OrderService getOrderService(){return orderService;}

    public UserService getUserService(){return userService;}

    public ActivityService getActivityService(){return activityService;}

    public ReviewService getReviewService(){return reviewService; }

    private ServiceFactory(){

    }
}
