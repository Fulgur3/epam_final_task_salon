package com.controller;

public class PageRouter {
    private boolean redirect;
    private String page;

    public String getPage(){return page; }

    public void setPage(String page){this.page=page;}

    public boolean getRedirect(){return redirect;}

    public void setRedirect(boolean value){this.redirect=value;}
}
