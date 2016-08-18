package com.womai.m.mip.service.order.impl;

import com.womai.m.mip.domain.order.raw.ROOrderDetail;
import com.womai.m.mip.manager.order.OrderManager;
import com.womai.m.mip.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderManager orderManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void recordOrderInfo(ROOrderDetail orderDetail) throws Exception {
        orderManager.recordOrder(orderDetail);
    }
}
