package com.example.wms.inbound.application.service;

import com.example.wms.inbound.adapter.in.dto.request.InboundReqDto;
import com.example.wms.inbound.application.domain.Inbound;
import com.example.wms.inbound.application.port.in.InboundUseCase;
import com.example.wms.inbound.application.port.out.AssignInboundNumberPort;
import com.example.wms.inbound.application.port.out.InboundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InboundService implements InboundUseCase {

    private final AssignInboundNumberPort assignInboundNumberPort;
    private final InboundPort inboundPort;

    @Transactional
    @Override
    public void createInboundPlan(InboundReqDto inboundReqDto) {

        Inbound inboundPlan = Inbound.builder()
                .scheduleNumber(makeNumber("IS"))
                .scheduleDate(inboundReqDto.getScheduleDate())
                .orderId(inboundReqDto.getOrderId())
                .supplierId(inboundReqDto.getSupplierId())
                .checkNumber(null)
                .checkDate(null)
                .putAwayDate(null)
                .putAwayNumber(null)
                .build();

        inboundPort.save(inboundPlan);
    }

    public String makeNumber(String format) {
        String currentDate = LocalDate.now().toString().replace("-","");
        String numberFormat = null;
        String number = null;

        if(format.equals("IS")) {
            number = assignInboundNumberPort.findMaxISNumber();
            numberFormat = "IS";
        }
        else if (format.equals("IC")) {
            number = assignInboundNumberPort.findMaxICNumber();
            numberFormat = "IC";
        }
        else if (format.equals("PA")) {
            number = assignInboundNumberPort.findMaxPANumber();
            numberFormat = "PA";
        }
        String nextNumber = "0000";

        if (number != null) {
            String lastNumberStr = number.substring(number.length()-4);
            int lastNumber = Integer.parseInt(lastNumberStr);
            nextNumber = String.format("%04d",lastNumber+1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(numberFormat).append(currentDate).append(nextNumber);
        return sb.toString();
    }
}
