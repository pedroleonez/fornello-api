package pedroleonez.fornello.api.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pedroleonez.fornello.api.dtos.output.order.RecoveryDeliveryData;
import pedroleonez.fornello.api.dtos.output.order.RecoveryOrderDto;
import pedroleonez.fornello.api.dtos.output.order.RecoveryOrderItemDto;
import pedroleonez.fornello.api.entities.DeliveryData;
import pedroleonez.fornello.api.entities.Order;
import pedroleonez.fornello.api.entities.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface OrderMapper {

    @Mapping(qualifiedByName = "mapDeliveryDataToRecoveryDeliveryData", target = "deliveryData")
    @Mapping(qualifiedByName = "mapOrderItemListToRecoveryOrderItemDtoList", target = "orderItems")
    @Mapping(qualifiedByName = "mapUserToUserDto", target = "user")
    @Named("mapOrderToRecoveryOrderDto")
    RecoveryOrderDto mapOrderToRecoveryOrderDto(Order order);

    @IterableMapping(qualifiedByName = "mapOrderItemToRecoveryOrderItemDto")
    @Named("mapOrderItemListToRecoveryOrderItemDtoList")
    List<RecoveryOrderItemDto> mapOrderItemListToRecoveryOrderItemDtoList(List<OrderItem> orderItems);

    @Named("mapOrderItemToRecoveryOrderItemDto")
    RecoveryOrderItemDto mapOrderItemToRecoveryOrderItemDto(OrderItem orderItem);

    @Named("mapDeliveryDataToRecoveryDeliveryData")
    RecoveryDeliveryData mapDeliveryDataToRecoveryDeliveryData(DeliveryData deliveryData);
}
