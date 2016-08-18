package com.womai.m.mip.dao.order;

import com.womai.m.mip.domain.order.raw.Orderdetailinfo;
import com.womai.m.mip.domain.order.raw.Orderdetailreceiveinfo;
import com.womai.m.mip.domain.order.raw.Orderdetailstatistics;
import com.womai.m.mip.domain.order.raw.ROOrderDetail;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
public interface OrderMapper {

    public void insertOrder(ROOrderDetail orderDetail);
    public void insertOrderDetailInfo(Orderdetailinfo orderDetail);
    public void insertOrderDetailStatistics(Orderdetailstatistics orderdetailstatistics);
    public void insertOrderdetailreceiveinfo(Orderdetailreceiveinfo orderdetailreceiveinfo);

}
