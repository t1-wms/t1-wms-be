package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import com.example.wms.outbound.application.port.out.GetOutboundAssignPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOutboundAssignService implements CreateOutboundAssignUseCase {

    private final CreateOutboundAssignPort createOutboundAssignPort;
    private final NotificationPort notificationPort;
    private final GetOutboundAssignPort getOutboundAssignPort;

    @Override
    @Transactional
    public Notification createOutboundAssign(Long outboundPlanId) {
        // 1. 기존 출고 정보 확인
        Outbound existingOutbound = createOutboundAssignPort.findOutboundByPlanId(outboundPlanId);

        if (existingOutbound != null && existingOutbound.getOutboundAssignNumber() != null) {
            // 이미 출고 지시가 있음
            throw new DuplicatedException("이미 출고 지시가 등록된 상태입니다.");
        }

        String currentDate = LocalDate.now().toString().replace("-", "");
        String maxOutboundAssignNumber = createOutboundAssignPort.findMaxOutboundAssignNumber();
        String nextNumber = "0000";

        if (maxOutboundAssignNumber != null) {
            String lastNumberStr = maxOutboundAssignNumber.substring(maxOutboundAssignNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String oaNumber = "OA" + currentDate + nextNumber;

        if (existingOutbound != null) {
            // null값들 업데이트하기
            existingOutbound.setOutboundAssignNumber(oaNumber);
            existingOutbound.setOutboundAssignDate(LocalDate.now());

            createOutboundAssignPort.update(existingOutbound);
        } else {
            // 새로운 출고 정보 저장
            Outbound outbound = Outbound.builder()
                    .outboundPlanId(outboundPlanId)
                    .outboundAssignNumber(oaNumber)
                    .outboundAssignDate(LocalDate.now())
                    .outboundPickingNumber(null)
                    .outboundPickingDate(null)
                    .outboundPackingNumber(null)
                    .outboundPackingDate(null)
                    .outboundLoadingNumber(null)
                    .outboundLoadingDate(null)
                    .build();

            createOutboundAssignPort.save(outbound);
        }

        // outboundPlan status 바꿔주기
        OutboundPlan outboundPlan = getOutboundAssignPort.findOutboundPlanByOutboundPlanId(outboundPlanId);
        createOutboundAssignPort.updateOutboundPlanStatus(outboundPlan);

        Notification notification = Notification.builder()
                .content("출고 지시가 등록되었습니다.")
                .event("출고 지시")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}
