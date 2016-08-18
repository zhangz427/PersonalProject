package com.womai.m.mip.manager.order;

import com.womai.m.mip.domain.order.raw.ROOrderDetail;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
public interface OrderManager {

    public void recordOrder(ROOrderDetail orderDetail) throws Exception;

}
