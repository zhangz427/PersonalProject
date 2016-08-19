package com.womai.m.mip.domain.activity;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品信息（用于列表中得摘要信息）
 */
public class Sku implements Serializable {

    /**
     * 商品名称
     */
    private String name = "";

    /**
     * 交易价格
     */
    private KeyValue price1 = new KeyValue();

    /**
     * 市场价格
     */
    private KeyValue price2 = new KeyValue();

    /**
     * 	购买价格
     */
    private KeyValue buyPrice = new KeyValue();

    /**
     * 	参考价格
     */
    private KeyValue referencePrice = new KeyValue();

    /**
     * 图片
     */
    private String pic = "";

    /**
     * 商品id
     */
    private String product_id = "";

    /**
     * 商品可卖数
     */
    private String number = "";

    /**
     * 商品类型供客户端获取标签使用 1：普通 2：特价 3:团购
     */
    private String product_type = "";

    /**
     * 是否生鲜
     */
    private boolean product_fresh = false;

    /**
     * 是否可售
     */
    private boolean sellable = false;

    /**
     * 满赠满减
     */
    private List<String> promotion_tag = new ArrayList<String>();
    private List<String> category_dsp = new ArrayList<String>();
    private int sellerId;

    public List<String> getCategory_dsp() {
        return category_dsp;
    }

    public void setCategory_dsp(List<String> category_dsp) {
        this.category_dsp = category_dsp;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyValue getPrice1() {
        return price1;
    }

    public void setPrice1(KeyValue price1) {
        this.price1 = price1;
    }

    public KeyValue getPrice2() {
        return price2;
    }

    public void setPrice2(KeyValue price2) {
        this.price2 = price2;
    }

    public KeyValue getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(KeyValue buyPrice) {
        this.buyPrice = buyPrice;
    }

    public KeyValue getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(KeyValue referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public boolean isProduct_fresh() {
        return product_fresh;
    }

    public void setProduct_fresh(boolean product_fresh) {
        this.product_fresh = product_fresh;
    }

    public boolean isSellable() {
        return sellable;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public List<String> getPromotion_tag() {
        return promotion_tag;
    }

    public void setPromotion_tag(List<String> promotion_tag) {
        this.promotion_tag = promotion_tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sku sku = (Sku) o;

        if (product_id != null ? !product_id.equals(sku.product_id) : sku.product_id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return product_id != null ? product_id.hashCode() : 0;
    }
}
