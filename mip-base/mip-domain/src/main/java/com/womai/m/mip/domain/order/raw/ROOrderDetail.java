package com.womai.m.mip.domain.order.raw;



import com.womai.m.mip.domain.ResError;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情
 *
 */
public class ROOrderDetail {

    private int id;

    /**
     * 响应信息
     */
    public String response = "";

    /**
     * 错误信息
     */
    public ResError error;

    /**
     * ﻿订单跟踪按钮显示
     */
    public boolean logistics_button = false;

    /**
     * 商品列表
     */
    public List<Orderdetailproductlist> orderdetail_productlist = new ArrayList<Orderdetailproductlist>();

    /**
     * 收货人信息
     */
    public Orderdetailreceiveinfo orderdetail_receiveinfo = new Orderdetailreceiveinfo();

    /**
     * 订单信息
     */
    public Orderdetailinfo orderdetail_info = new Orderdetailinfo();

    /**
     * 订单统计信息
     */
    public Orderdetailstatistics orderdetail_statistics = new Orderdetailstatistics();

    /**
     * 用户需支付
     */
    public String total_amount = "";

    /**
     * 是否在线支付
     */
    public boolean pay_button = false;

    /**
     * 是否可取消
     */
    public boolean cancel_button = false;

	/**
     * 是否为百度在线支付
     */
    public boolean baidupay_button = false;

    /**
     * 是否显示在线支付
     */
    public boolean isNeedOnlinePay = false;

    /**
     * 支付编号
     */
    public String paymodeId = "";

    /**
     * 支付名称
     */
    public String paymodeName = "";

    /**
     * 是否显示定制祝福
     */
    public boolean customBless_button = false;

    /**
     * 是否是蒙牛订单
     */
    public boolean mengniuOrder = false;
    /**
     * 老客拉新活动是否结束
     */
    public boolean newUserActive;
    /**
     * 是否展示老客拉新分享按钮
     */
    public boolean newUserShow;
    /**
     * 订单类型
     */
    public String orderType;
    /**
     * 是否显示确认收货按钮
     */
    public boolean showConfirmBtn;
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ResError getError() {
        return error;
    }

    public void setError(ResError error) {
        this.error = error;
    }

    public boolean isLogistics_button() {
        return logistics_button;
    }

    public void setLogistics_button(boolean logistics_button) {
        this.logistics_button = logistics_button;
    }

    public List<Orderdetailproductlist> getOrderdetail_productlist() {
        return orderdetail_productlist;
    }

    public void setOrderdetail_productlist(List<Orderdetailproductlist> orderdetail_productlist) {
        this.orderdetail_productlist = orderdetail_productlist;
    }

    public Orderdetailreceiveinfo getOrderdetail_receiveinfo() {
        return orderdetail_receiveinfo;
    }

    public void setOrderdetail_receiveinfo(Orderdetailreceiveinfo orderdetail_receiveinfo) {
        this.orderdetail_receiveinfo = orderdetail_receiveinfo;
    }

    public Orderdetailinfo getOrderdetail_info() {
        return orderdetail_info;
    }

    public void setOrderdetail_info(Orderdetailinfo orderdetail_info) {
        this.orderdetail_info = orderdetail_info;
    }

    public Orderdetailstatistics getOrderdetail_statistics() {
        return orderdetail_statistics;
    }

    public void setOrderdetail_statistics(Orderdetailstatistics orderdetail_statistics) {
        this.orderdetail_statistics = orderdetail_statistics;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public boolean isPay_button() {
        return pay_button;
    }

    public void setPay_button(boolean pay_button) {
        this.pay_button = pay_button;
    }

    public boolean isCancel_button() {
        return cancel_button;
    }

    public void setCancel_button(boolean cancel_button) {
        this.cancel_button = cancel_button;
    }

    public boolean isBaidupay_button() {
        return baidupay_button;
    }

    public void setBaidupay_button(boolean baidupay_button) {
        this.baidupay_button = baidupay_button;
    }


    public String getPaymodeId() {
        return paymodeId;
    }

    public void setPaymodeId(String paymodeId) {
        this.paymodeId = paymodeId;
    }

    public String getPaymodeName() {
        return paymodeName;
    }

    public void setPaymodeName(String paymodeName) {
        this.paymodeName = paymodeName;
    }

    public boolean isIsNeedOnlinePay() {
        return isNeedOnlinePay;
    }

    public void setNeedOnlinePay(boolean isNeedOnlinePay) {
        this.isNeedOnlinePay = isNeedOnlinePay;
    }

    public boolean isNeedOnlinePay() {
        return isNeedOnlinePay;
    }

    public boolean isCustomBless_button() {
        return customBless_button;
    }

    public void setCustomBless_button(boolean customBless_button) {
        this.customBless_button = customBless_button;
    }

    public boolean isMengniuOrder() {
        return mengniuOrder;
    }

    public void setMengniuOrder(boolean mengniuOrder) {
        this.mengniuOrder = mengniuOrder;
    }

    public boolean isNewUserActive() {
        return newUserActive;
    }

    public void setNewUserActive(boolean newUserActive) {
        this.newUserActive = newUserActive;
    }

    public boolean isNewUserShow() {
        return newUserShow;
    }

    public void setNewUserShow(boolean newUserShow) {
        this.newUserShow = newUserShow;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public boolean isShowConfirmBtn() {
        return showConfirmBtn;
    }

    public void setShowConfirmBtn(boolean showConfirmBtn) {
        this.showConfirmBtn = showConfirmBtn;
    }

    public void setIsNeedOnlinePay(boolean isNeedOnlinePay) {
        this.isNeedOnlinePay = isNeedOnlinePay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
