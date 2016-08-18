package com.womai.m.mip.domain.order.raw;



/**
 * 商品对象
 */
public class Orderdetailproductlist {
    /**
     * 商品名称
     */
    public String pro_name = "";

    /**
     * 图片
     */
    public String pro_pic = "";

    /**
     * 商品id
     */
    public String product_id = "";

    /**
     * 商品价格
     */
    public KeyValue trade_price = new KeyValue();

    /**
     * 商品数量
     */
    public String product_amount = "";

    /**
     * 商品重量
     */
    public String product_weight = "";

    /**
     * 商品积分
     */
    public String product_point = "";

    private boolean exchangeProduct;

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_pic() {
        return pro_pic;
    }

    public void setPro_pic(String pro_pic) {
        this.pro_pic = pro_pic;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public KeyValue getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(KeyValue trade_price) {
        this.trade_price = trade_price;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_point() {
        return product_point;
    }

    public void setProduct_point(String product_point) {
        this.product_point = product_point;
    }

    public boolean isExchangeProduct() {
        return exchangeProduct;
    }

    public void setExchangeProduct(boolean exchangeProduct) {
        this.exchangeProduct = exchangeProduct;
    }
}
