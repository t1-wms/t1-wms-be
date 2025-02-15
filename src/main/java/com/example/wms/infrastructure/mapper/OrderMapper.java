package com.example.wms.infrastructure.mapper;

import com.example.wms.order.application.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> findAll(@Param("limit") int limit, @Param("offset") int offset);

    Order findById(@Param("orderId") Long orderId);

    List<Order> findByIsApproved(@Param("isApproved") boolean isApproved);

    List<Order> findByIsDelayed(@Param("isDelayed") boolean isDelayed);

    // 승인된(isApproved = true) & 지연되지 않은(isDelayed = false) 주문 조회
    List<Order> findApprovedAndNotDelayed();

    String getLastOrderNumber();

    void createOrder(Order order);
}
