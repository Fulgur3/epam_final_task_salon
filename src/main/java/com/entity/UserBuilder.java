package com.entity;

public class UserBuilder {
    private int id;
    private String login;
    private String password;
    private String userName;
    private String email;
    private String phoneNumber;
    private User.Status status;
    private String cardNumber;

    public UserBuilder setId(int id){
        this.id=id;
        return this;
    }

    public UserBuilder setLogin(String login){
        this.login=login;
        return this;
    }
    public UserBuilder setPassword(String password){
        this.password=password;
        return this;
    }

    public UserBuilder setUserName(String userName){
        this.userName=userName;
        return this;
    }

    public UserBuilder setEmail(String email){
        this.email=email;
        return this;
    }
    public UserBuilder setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
        return this;
    }

    public UserBuilder setStatus(User.Status status){
        this.status=status;
        return this;
    }

    public UserBuilder setStatus(String status){
        for(User.Status st: User.Status.values()){
            if(st.name().equalsIgnoreCase(status)){
                this.status=st;
            }
        }
        return this;
    }

    public UserBuilder setCardNumber(String cardNumber){
        this.cardNumber=cardNumber;
        return this;
    }

    public User create(){
        User user=new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setUsername(userName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setStatus(status);
        user.setCardNumber(cardNumber);
        return user;
    }
}
