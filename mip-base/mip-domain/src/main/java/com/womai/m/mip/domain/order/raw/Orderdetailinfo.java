package com.womai.m.mip.domain.order.raw;

/**
 * 订单信息
 *
 */
public class Orderdetailinfo {

    private int id;
    /**
     * 订单号
     */
    private String order_aliascode = "";

    /**
     * 订单状态
     */
    private String order_status = "";

    /**
     * 订单金额
     */
    private String order_amount = "";

    /**
     * 赠送积分
     */
    private String order_point = "";

    /**
     * 下单时间
     */
    private String order_creattime = "";

    /**
     * 付款方式
     */
    private String order_payway = "";

    /**
     * 是否卡券
     */
    private boolean card_payway = false;

    /**
     * 是否需要发票
     */
    private String invoice = "";

    /**
     * 备注
     */
    private String remark_msg = "";

    public String getOrder_aliascode() {
        return order_aliascode;
    }

    public void setOrder_aliascode(String order_aliascode) {
        this.order_aliascode = order_aliascode;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_point() {
        return order_point;
    }

    public void setOrder_point(String order_point) {
        this.order_point = order_point;
    }

    public String getOrder_creattime() {
        return order_creattime;
    }

    public void setOrder_creattime(String order_creattime) {
        this.order_creattime = order_creattime;
    }

    public String getOrder_payway() {
        return order_payway;
    }

    public void setOrder_payway(String order_payway) {
        this.order_payway = order_payway;
    }

    public boolean isCard_payway() {
        return card_payway;
    }

    public void setCard_payway(boolean card_payway) {
        this.card_payway = card_payway;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getRemark_msg() {
        return remark_msg;
    }

    public void setRemark_msg(String remark_msg) {
        this.remark_msg = remark_msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
