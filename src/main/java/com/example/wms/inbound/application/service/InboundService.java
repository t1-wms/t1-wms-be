package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.*;
import com.example.wms.inbound.adapter.in.dto.response.*;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import com.example.wms.inbound.application.port.out.InboundRetrievalPort;
import com.example.wms.infrastructure.exception.NotFoundException;
import com.example.wms.infrastructure.pagination.util.PageableUtils;
import com.example.wms.inventory.application.port.out.InventoryPort;
import com.example.wms.order.application.domain.Order;
import com.example.wms.order.application.domain.OrderProduct;
import com.example.wms.order.application.port.out.OrderPort;
import com.example.wms.order.application.port.out.OrderProductPort;
import com.example.wms.product.adapter.in.dto.LotInfoDto;
import com.example.wms.product.application.domain.Lot;
import com.example.wms.product.application.domain.LotStatus;
import com.example.wms.product.application.domain.Product;
import com.example.wms.product.application.port.out.BinPort;
import com.example.wms.product.application.port.out.LotPort;
import com.example.wms.product.application.port.out.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InboundService implements InboundUseCase {

    private final AssignInboundNumberPort assignInboundNumberPort;
    private final InboundPort inboundPort;
    private final ProductPort productPort;
    private final InboundRetrievalPort inboundRetrievalPort;
    private final LotPort lotPort;
    private final OrderPort orderPort;
    private final BinPort binPort;
    private final OrderProductPort orderProductPort;
    private final InventoryPort inventoryPort;


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
                .scheduleDate(order.getInboundDate())
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
    public void createInboundCheck(Long inboundId, InboundCheckReqDto inboundCheckReqDto) {

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("inbound not found with id " + inboundId);
        }

        // 해당 inbound 에 입하검사 관련 정보 업데이트
        inbound.setInboundStatus("입하검사");
        inbound.setCheckDate(LocalDate.now());
        inbound.setCheckNumber(makeNumber("IC"));

        for (InboundCheckedProductReqDto checkedProduct : inboundCheckReqDto.getCheckedProductList()) {

            Long productId = checkedProduct.getProductId();
            Long defectiveCount = checkedProduct.getDefectiveCount();

            Product product = productPort.findById(productId);

            if (product == null) {
                throw new NotFoundException("product not found with id :" + productId);
            }

            OrderProduct orderProduct = orderProductPort.findByProductId(productId);
            orderProduct.setDefectiveCount(defectiveCount);

            // 재발주
            if (defectiveCount > 0) {
                orderPort.createOrder(orderProduct.getProductId(), inboundId, defectiveCount);
            }
        }

        // 해당 inbound 에 입하 검사 업데이트 완료
        inboundPort.updateIC(inbound.getInboundId(), inbound.getCheckDate(), inbound.getScheduleNumber());
    }

    @Override
    public Page<InboundResDto> getFilteredInboundCheck(String inboundCheckNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundAllProductDto> inboundAllProductDtoList = inboundRetrievalPort.findInboundCheckFilteringWithPagination(inboundCheckNumber, startDate, endDate, safePageable);

        Integer count = inboundRetrievalPort.countFilteredInboundCheck(inboundCheckNumber, startDate, endDate);

        List<InboundResDto> inboundResDtoList = convertToInboundResDto(inboundAllProductDtoList);

        return new PageImpl<>(inboundResDtoList, pageable, count);
    }

    @Transactional
    @Override
    public void updateInboundCheck(Long inboundId, InboundCheckUpdateReqDto updateReqDto) {

        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("Inbound not found with id " + inboundId);
        }

        inbound.setCheckDate(LocalDate.now());
        inbound.setCheckNumber(makeNumber("IC"));
        inboundPort.updateIC(inbound.getInboundId(), inbound.getCheckDate(), inbound.getCheckNumber()); // 관련 inbound 테이블에 업데이트

        for (InboundCheckedProductReqDto checkedProduct : updateReqDto.getCheckedProductList()) { // 품목별 defectiveCount
            OrderProduct orderProduct = orderProductPort.findByProductId(checkedProduct.getProductId());

            if (orderProduct == null) {
                throw new NotFoundException("OrderProduct not found with productId : " + checkedProduct.getProductId());
            }

            Long beforeDefectiveCount = orderProduct.getDefectiveCount(); // 기존 defectiveCount

            Long productId = checkedProduct.getProductId();

            Long updatedDefectiveCount = checkedProduct.getDefectiveCount(); // 수정한 defectiveCount

            Product product = productPort.findById(productId);


            if (product == null) {
                throw new NotFoundException("Product not found with id : " + productId);
            }

            if (beforeDefectiveCount - updatedDefectiveCount < 0) {
                orderPort.createOrder(productId, inboundId, updatedDefectiveCount-beforeDefectiveCount); // 재발주
            } else if (beforeDefectiveCount - updatedDefectiveCount > 0) {
                // 발주 수정 메서드 추가
            }
            orderProduct.setDefectiveCount(updatedDefectiveCount); // 발주 품목 별 불량품 개수 업데이트
        }

    }

    @Override
    public Page<InboundPutAwayResDto> getFilteredPutAway(String inboundPutAwayNumber, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Pageable safePageable = PageableUtils.convertToSafePageableStrict(pageable, Inbound.class);

        List<InboundPutAwayResDto> inboundPutAwayList = inboundRetrievalPort.findFilteredInboundPutAway(inboundPutAwayNumber, startDate, endDate, safePageable);
        Integer count = inboundRetrievalPort.countFilteredPutAway(inboundPutAwayNumber, startDate, endDate);

        return new PageImpl<>(inboundPutAwayList, safePageable, count);
    }

    @Override
    public void deleteInboundCheck(Long inboundId) {
        Inbound inbound = inboundPort.findById(inboundId);

        if (inbound == null) {
            throw new NotFoundException("not found with id " + inboundId);
        }

        inbound.setCheckNumber(null);
        inbound.setCheckDate(null);
        inbound.setInboundStatus("입하예정");

        inboundPort.updateIC(inboundId, null, null);
    }

    @Transactional
    @Override
    public InboundWorkerCheckResDto createInboundCheckByWorker(List<InboundCheckWorkerReqDto> workerCheckRequests) {
        if(workerCheckRequests.isEmpty()) {
            throw new IllegalArgumentException("검수할 품목이 없습니다.");
        }

        String scheduleNumber = workerCheckRequests.get(0).getScheduleNumber();
        Long orderId = inboundPort.getOrderIdByScheduleNumber(scheduleNumber);

        if (orderId == null) {
            throw new IllegalArgumentException("해당 입하 예정 번호에 대한 주문을 찾을 수 없습니다: " + scheduleNumber);
        }

        for (InboundCheckWorkerReqDto reqDto : workerCheckRequests) {
            inboundPort.updateOrderProduct(orderId, reqDto.getProductId(), reqDto.getIsDefective());
        }

        String checkNumber = makeNumber("IC");
        inboundPort.updateInboundCheck(scheduleNumber, checkNumber);

        List<LotInfoDto> lots = inboundPort.getLotsByCheckNumber(checkNumber);

        return new InboundWorkerCheckResDto(checkNumber, lots);
    }

    @Override
    public void putAway(Long inboundId, List<InboundPutAwayReqDto> putAwayRequests) {
        String putAwayNumber = makeNumber("PA");
        LocalDate putAwayDate = LocalDate.now();

        inboundPort.updatePA(inboundId, putAwayDate, putAwayNumber);

        for (InboundPutAwayReqDto request : putAwayRequests) {
            Long productId = request.getProductId();
            Integer lotCount = request.getLotCount();
            Product product = productPort.findById(productId);
            Integer lotUnit = product.getLotUnit();
            String locationBinCode = productPort.getLocationBinCode(productId);

            Long binId = findExactBinId(locationBinCode);

            if (binId == null) {
                throw new NotFoundException("해당 품목을 적치할 bin이 없습니다. " + locationBinCode);
            }

            for(int i=0; i<lotCount; i++) {
                Lot lot = Lot.builder()
                        .productId(productId)
                        .binId(binId)
                        .lotNumber(locationBinCode)
                        .status(LotStatus.입고)
                        .inboundId(inboundId)
                        .build();
                lotPort.insertLot(lot);
            }

            binPort.incrementBinAmount(binId, lotCount);
            Integer totalCount = lotCount * lotUnit;
            inventoryPort.updateInventory(productId, totalCount);
        }
    }

    private Long findExactBinId(String locationBinCode) {
        if (locationBinCode.matches("[A-F]-\\d{2}-\\d{2}-\\d{2}")) {
            return binPort.findBinIdByBinCode(locationBinCode);
        }

        if (locationBinCode.matches("[A-F]-\\d{2}-\\d{2}")) {
            String[] parts = locationBinCode.split("-");
            String zone = parts[0];
            Integer aisle = Integer.parseInt(parts[1]);
            Integer row = Integer.parseInt(parts[2]);
            return binPort.findAvailableBinIdInRow(zone, aisle, row);
        }

        if (locationBinCode.matches("[A-F]-\\d{2}")) {
            String[] parts = locationBinCode.split("-");
            String zone = parts[0];
            Integer aisle = Integer.parseInt(parts[1]);
            return binPort.findAvailableBinIdInAisle(zone, aisle);
        }
        return null;
    }

    @Override
    public Page<ProductInboundResDto> getAllInboundByProductWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<ProductInboundResDto> inboundList = inboundRetrievalPort.findAllInboundByProductWithPagination(startDate, endDate, pageable);
        return new PageImpl<>(inboundList, pageable, inboundList.size());
    }

    @Override
    public Page<SupplierInboundResDto> getAllInboundBySupplierWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<SupplierInboundResDto> inboundList = inboundRetrievalPort.findAllInboundBySupplierWithPagination(startDate, endDate, pageable);
        return new PageImpl<>(inboundList, pageable, inboundList.size());
    }

    @Override
    public Page<InboundProgressResDto> getAllInboundProgressWithPagination(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<InboundProgressDetailDto> inboundList = inboundRetrievalPort.findAllInboundProgressWithPagination(startDate, endDate, pageable);

        List<InboundProgressDetailDto> scheduleList = inboundList.stream()
                .filter(i -> i.getCheckNumber() == null && i.getPutAwayNumber() == null)
                .toList();

        List<InboundProgressDetailDto> checkList = inboundList.stream()
                .filter(i -> i.getCheckNumber() != null && i.getPutAwayNumber() == null)
                .toList();

        List<InboundProgressDetailDto> putAwayList = inboundList.stream()
                .filter(i -> i.getPutAwayNumber() != null)
                .toList();

        List<InboundProgressResDto> resultList = List.of(
                new InboundProgressResDto(scheduleList, checkList, putAwayList)
        );

        return new PageImpl<>(resultList, pageable, resultList.size());
    }


}

