package com.womai.m.mip.channel.order;

import com.womai.m.mip.channel.BaseController;
import com.womai.m.mip.channel.BaseGeneralResponse;
import com.womai.m.mip.channel.BaseResponse;
import com.womai.m.mip.common.utils.JacksonUtil;
import com.womai.m.mip.domain.order.raw.ROOrderDetail;
import com.womai.m.mip.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zheng.zhang on 2016/6/16.
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/orderdetail",produces="application/json;charset=utf-8")
    public BaseResponse orderdetail(HttpServletRequest request) {

        String orderdetail = request.getParameter("orderdetail");

        logger.info("order detail: {}", orderdetail);

        try {
            ROOrderDetail orderDetail = JacksonUtil.toObject(orderdetail, ROOrderDetail.class);

            System.out.println(orderDetail.getOrderType());

            orderService.recordOrderInfo(orderDetail);

        } catch(Exception e) {

        }



        return new BaseResponse();
    }
}
