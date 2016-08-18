package com.womai.m.mip.manager.order.impl;

import com.womai.m.mip.dao.order.OrderMapper;
import com.womai.m.mip.domain.order.raw.Orderdetailinfo;
import com.womai.m.mip.domain.order.raw.Orderdetailreceiveinfo;
import com.womai.m.mip.domain.order.raw.Orderdetailstatistics;
import com.womai.m.mip.domain.order.raw.ROOrderDetail;
import com.womai.m.mip.manager.order.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
@Component("orderManager")
public class OrderManagerImpl implements OrderManager {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void recordOrder(ROOrderDetail orderDetail) throws Exception {
        System.out.println(orderDetail.getOrderType());
        orderMapper.insertOrder(orderDetail);
        Orderdetailinfo orderdetailinfo = orderDetail.getOrderdetail_info();
        orderdetailinfo.setId(orderDetail.getId());
        orderMapper.insertOrderDetailInfo(orderdetailinfo);
        Orderdetailstatistics orderdetailstatistics = orderDetail.getOrderdetail_statistics();
        orderdetailstatistics.setId(orderDetail.getId());
        orderMapper.insertOrderDetailStatistics(orderdetailstatistics);
        Orderdetailreceiveinfo orderdetailreceiveinfo = orderDetail.getOrderdetail_receiveinfo();
        orderdetailreceiveinfo.setId(orderDetail.getId());
        orderMapper.insertOrderdetailreceiveinfo(orderdetailreceiveinfo);
    }
}
