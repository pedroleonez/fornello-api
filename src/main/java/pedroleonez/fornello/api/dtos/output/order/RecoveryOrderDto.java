package pedroleonez.fornello.api.dtos.output.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import pedroleonez.fornello.api.dtos.output.user.RecoveryUserDto;
import pedroleonez.fornello.api.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RecoveryOrderDto(

        Long id,

        RecoveryUserDto user,

        List<RecoveryOrderItemDto> orderItems,

        Status status,

        String paymentMethod,

        BigDecimal amount,

        RecoveryDeliveryData deliveryData,

        @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdDate

) {
}
