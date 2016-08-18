package com.womai.m.mip.domain.order.raw;



/**
 * 订单统计信息
 *
 */
public class Orderdetailstatistics {

    private int id;
    /**
     * 商品总重量
     */
    private KeyValue weight = new KeyValue();

    /**
     * 商品总金额
     */
    private KeyValue pro_total_price = new KeyValue();

    /**
     * 运费
     */
    private KeyValue freight = new KeyValue();

    /**
     * 运费优惠
     */
    private KeyValue preferences_freight = new KeyValue();

    /**
     * 卡卷冲抵
     */
    private KeyValue card_offset = new KeyValue();

    /**
     * 赠送积分
     */
    private KeyValue order_point = new KeyValue();

    /**
     * 订单优惠
     */
    private KeyValue order_privilege = new KeyValue();

    private KeyValue womaicard_pay = new KeyValue();

    public KeyValue getWomaicard_pay() {
        return womaicard_pay;
    }

    public void setWomaicard_pay(KeyValue womaicard_pay) {
        this.womaicard_pay = womaicard_pay;
    }

    public KeyValue getWeight() {
        return weight;
    }

    public void setWeight(KeyValue weight) {
        this.weight = weight;
    }

    public KeyValue getPro_total_price() {
        return pro_total_price;
    }

    public void setPro_total_price(KeyValue pro_total_price) {
        this.pro_total_price = pro_total_price;
    }

    public KeyValue getFreight() {
        return freight;
    }

    public void setFreight(KeyValue freight) {
        this.freight = freight;
    }

    public KeyValue getPreferences_freight() {
        return preferences_freight;
    }

    public void setPreferences_freight(KeyValue preferences_freight) {
        this.preferences_freight = preferences_freight;
    }

    public KeyValue getCard_offset() {
        return card_offset;
    }

    public void setCard_offset(KeyValue card_offset) {
        this.card_offset = card_offset;
    }

    public KeyValue getOrder_point() {
        return order_point;
    }

    public void setOrder_point(KeyValue order_point) {
        this.order_point = order_point;
    }

    public KeyValue getOrder_privilege() {
        return order_privilege;
    }

    public void setOrder_privilege(KeyValue order_privilege) {
        this.order_privilege = order_privilege;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
