package com.example.wms.outbound.application.service;

import com.example.wms.infrastructure.exception.DuplicatedException;
import com.example.wms.notification.application.domain.Notification;
import com.example.wms.notification.application.port.out.NotificationPort;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.port.in.CreateOutboundAssignUseCase;
import com.example.wms.outbound.application.port.out.CreateOutboundAssignPort;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateOutboundAssignService implements CreateOutboundAssignUseCase {

    private final CreateOutboundAssignPort createOutboundAssignPort;
    private final NotificationPort notificationPort;

    @Override
    public Notification createOutboundAssign(Long outboundPlanId) {

        // outboundPlanId 중복 확인하기
        int count = createOutboundAssignPort.findOutboundAssign(outboundPlanId);

        if (count > 0) {
            // 이미 존재하면 예외 던짐
            throw new DuplicatedException("해당 outboundPlanID가 이미 존재합니다.");
        }

        String currentDate = LocalDate.now().toString().replace("-", ""); // 예: 2025-02-10 -> 20250210

        // DB에서 가장 큰 outboundAssignNumber 조회
        String maxOutboundAssignNumber = createOutboundAssignPort.findMaxOutboundAssignNumber();
        String nextNumber = "0000";

        if (maxOutboundAssignNumber != null) {
            // 마지막 4자리 숫자 추출해서 + 1
            String lastNumberStr = maxOutboundAssignNumber.substring(maxOutboundAssignNumber.length() - 4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d", lastNumber + 1);
        }

        String osNumber = "OA" + currentDate + nextNumber;

        Outbound outbound = Outbound.builder()
                .outboundPlanId(outboundPlanId)
                .outboundAssignNumber(osNumber)
                .outboundAssignDate(LocalDate.now())
                .outboundPickingNumber(null)
                .outboundPickingDate(null)
                .outboundPackingNumber(null)
                .outboundPackingDate(null)
                .outboundLoadingNumber(null)
                .outboundLoadingDate(null)
                .build();

        createOutboundAssignPort.save(outbound);

        Notification notification = Notification.builder()
                .content("출고 지시가 등록되었습니다.")
                .event("출고 지시")
                .userRole(UserRole.ROLE_ADMIN)
                .build();

        notificationPort.save(notification);
        return notification;
    }
}
