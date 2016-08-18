package com.womai.m.mip.service.order;

import com.womai.m.mip.domain.order.raw.ROOrderDetail;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
public interface OrderService {

    public void recordOrderInfo(ROOrderDetail orderDetail) throws Exception;
}
