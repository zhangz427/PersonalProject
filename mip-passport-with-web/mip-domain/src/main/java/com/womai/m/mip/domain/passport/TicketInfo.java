package com.womai.m.mip.domain.passport;

/**
 * Created by zheng.zhang on 2016/5/23.
 */
public class TicketInfo {

    private String ticketGrantingCookie;

    private String serviceToken;


    public TicketInfo() {}

    public TicketInfo(String ticketGrantingCookie, String serviceToken) {
        this.ticketGrantingCookie = ticketGrantingCookie;
        this.serviceToken = serviceToken;
    }

    public String getTicketGrantingCookie() {
        return ticketGrantingCookie;
    }

    public void setTicketGrantingCookie(String ticketGrantingCookie) {
        this.ticketGrantingCookie = ticketGrantingCookie;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }
}
