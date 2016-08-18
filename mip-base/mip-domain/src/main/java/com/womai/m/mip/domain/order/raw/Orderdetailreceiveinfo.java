package com.womai.m.mip.domain.order.raw;

/**
 * 收货人信息
 *
 */
public class Orderdetailreceiveinfo {

    private int id;
    /**
     * 收货人名称
     */
    private String name = "";

    /**
     * 收货人电话
     */
    private String phone = "";

    /**
     * 收货人地址
     */
    private String address = "";

    /**
     * 送货时间
     */
    private String send_time = "";

    /**
     * 送货方式
     */
    private String send_way = "";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getSend_way() {
        return send_way;
    }

    public void setSend_way(String send_way) {
        this.send_way = send_way;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
