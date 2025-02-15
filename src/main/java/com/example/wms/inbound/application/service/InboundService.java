package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundCheckReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckUpdateReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundCheckedProductReqDto;
import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundAllProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundProductDto;
import com.example.wms.inbound.adapter.in.dto.response.InboundResDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.domain.InboundCheck;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundCheckPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InboundService implements InboundUseCase {

    private final AssignInboundNumberPort assignInboundNumberPort;
    private final InboundPort inboundPort;
    private final ProductPort productPort;
    private final InboundCheckPort inboundCheckPort;
    private final InboundRetrievalPort inboundRetrievalPort;
    private final LotPort lotPort;
    private final OrderPort orderPort;

    @Transactional
    @Override
    public Long createInboundPlan(InboundReqDto inboundReqDto) {

        Inbound inboundPlan = Inbound.builder()
                .scheduleNumber(makeNumber("IS"))
                .scheduleDate(inboundReqDto.getScheduleDate())
                .orderId(inboundReqDto.getOrderId())
                .supplierId(inboundReqDto.getSupplierId())
                .build();

        inboundPort.save(inboundPlan);
        return inboundPlan.getInboundId();
    }

    private String makeNumber(String format) {
        String currentDate = LocalDate.now().toString().replace("-","");
        String number = switch (format) {
            case "IS" -> assignInboundNumberPort.findMaxISNumber();
            case "IC" -> assignInboundNumberPort.findMaxICNumber();
            case "PA" -> assignInboundNumberPort.findMaxPANumber();
            default -> null;
        };

        String nextNumber = "0000";

        if (number != null) {
            int lastNumber = Integer.parseInt(number.substring(number.length()-4));
            nextNumber = String.format("%04d", lastNumber+1);
        }

        return format + currentDate + nextNumber;
    }

    @Override
    public Page<InboundResDto> getInboundPlans(Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundAllProductDto> inboundAllProductDtoList = inboundRetrievalPort.findInboundProductListWithPagination(safePageable);
        Integer count = inboundRetrievalPort.countAllInboundPlan();

        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList,pageable,count);
    }

    @Override
    public Page<InboundResDto> getFilteredInboundPlans(String inboundScheduleNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundAllProductDto> inboundAllProductDtoList = inboundRetrievalPort.findInboundFilteringWithPagination(inboundScheduleNumber, startDate, endDate, safePageable);
        Integer count = inboundRetrievalPort.countFilteredInboundPlan(inboundScheduleNumber, startDate, endDate);

        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList,pageable,count);
    }

    @Override
    public List<InboundProductDto> getAllInboundProductList(OrderProduct orderProduct) {

        return inboundRetrievalPort.findInboundProductListByOrderId(orderProduct.getOrderId());
    }



    private List<InboundResDto> convertToInboundResDto(List<InboundAllProductDto> planProductList) {
        if (planProductList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, InboundResDto> inboundMap = new LinkedHashMap<>();

        for (InboundAllProductDto dto : planProductList) {
            inboundMap.putIfAbsent(dto.getInboundId(),
                    InboundResDto.builder()
                            .inboundId(dto.getInboundId())
                            .inboundStatus(dto.getInboundStatus())
                            .createdAt(dto.getCreatedAt())
                            .scheduleNumber(dto.getScheduleNumber())
                            .scheduleDate(dto.getScheduleDate())
                            .orderId(dto.getOrderId())
                            .orderNumber(dto.getOrderNumber())
                            .orderDate(dto.getOrderDate())
                            .supplierId(dto.getSupplierId())
                            .supplierName(dto.getSupplierName())
                            .productList(getAllInboundProductList(OrderProduct.builder().orderId(dto.getOrderId()).build()))
                            .build()
            );
        }

        return new ArrayList<>(inboundMap.values());
    }

    @Transactional
    @Override
    public void createInboundSchedule(Order order) {
        Inbound inboundPlan = Inbound.builder()
                .inboundStatus("입하예정")
                .scheduleNumber(makeNumber("IS"))
                .scheduleDate(order.getInboundDate().toLocalDate())
                .orderId(order.getOrderId())
                .supplierId(order.getSupplierId())
                .build();

        inboundPort.save(inboundPlan);
    }

    @Transactional
    @Override
    public void deleteInboundPlan(Long inboundId) {
        inboundPort.delete(inboundId);
    }

    @Transactional
    @Override
    public void createInboundCheck(InboundCheckReqDto inboundCheckReqDto) {

        Inbound inbound = inboundPort.findById(inboundCheckReqDto.getInboundId());
        if (inbound == null) {
            throw new NotFoundException("inbound not found with id " + inboundCheckReqDto.getInboundId());
        }

        inbound.setInboundStatus("입하검사완료"); // 수정 필요
        inbound.setCheckDate(inboundCheckReqDto.getCheckDate());
        inbound.setCheckNumber(makeNumber("IC"));

        List<InboundCheck> inboundCheckList = new ArrayList<>();

        for (InboundCheckedProductReqDto checkedProduct : inboundCheckReqDto.getCheckedProductList()) {
            Long productId = checkedProduct.getProductId();
            Long defectiveLotCount = checkedProduct.getDefectiveLotCount();

            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("product not found with id :" + productId);
            }

            InboundCheck inboundCheck = InboundCheck.builder()
                    .inboundId(inbound.getInboundId())
                    .productId(productId)
                    .defectiveLotCount(defectiveLotCount)
                    .build();

            inboundCheckList.add(inboundCheck);
            inboundCheckPort.save(inboundCheck);

            lotPort.updateStatus(productId);

            if (defectiveLotCount > 0) {
                orderPort.createOrder(product.getSupplierId(), defectiveLotCount);
            }
        }

        inboundPort.updateIC(inbound.getInboundId(), inbound.getCheckDate(), inbound.getScheduleNumber());

    }

    @Override
    public Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundAllProductDto> inboundAllProductDtoList = inboundRetrievalPort.findInboundFilteringWithPagination(inboundCheckNumber, startDate, endDate, safePageable);
        Integer count = inboundRetrievalPort.countFilteredInboundCheck(inboundCheckNumber, startDate, endDate);

        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList, pageable, count);
    }

    @Transactional
    @Override
    public void updateInboundCheck(Long inboundId, InboundCheckUpdateReqDto updateReqDto) {
        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("Inbound not found with id " + updateReqDto.getInboundId());
        }

        inbound.setCheckDate(LocalDate.parse(updateReqDto.getCheckDate()));
        inbound.setCheckNumber(makeNumber("IC"));

        inboundPort.updateIC(inbound.getInboundId(), inbound.getCheckDate(), inbound.getCheckNumber());

        List<InboundCheck> existingChecks = inboundCheckPort.findByInboundId(updateReqDto.getInboundId());

        Map<Long, InboundCheck> checkMap = existingChecks.stream()
                .collect(Collectors.toMap(InboundCheck::getProductId, Function.identity()));

        List<InboundCheck> updatedChecks = new ArrayList<>();

        for (InboundCheckedProductReqDto checkedProduct : updateReqDto.getCheckedProductList()) {
            Long productId = checkedProduct.getProductId();
            Long updatedDefectiveCount = checkedProduct.getDefectiveLotCount();

            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("Product not found with id : " + productId);
            }

            if (checkMap.containsKey(productId)) {
                InboundCheck existingCheck = checkMap.get(productId);
                existingCheck.setDefectiveLotCount(updatedDefectiveCount);
                updatedChecks.add(existingCheck);
            }

            if (updatedDefectiveCount > 0) {
                orderPort.createOrder(product.getSupplierId(), updatedDefectiveCount);
            }
        }

        inboundCheckPort.saveAll(updatedChecks);
    }


}

