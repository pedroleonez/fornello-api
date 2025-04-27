package pedroleonez.fornello.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pedroleonez.fornello.api.dtos.input.order.CreateOrderDto;
import pedroleonez.fornello.api.dtos.input.order.CreateOrderItemDto;
import pedroleonez.fornello.api.dtos.input.order.UpdateStatusOrderDto;
import pedroleonez.fornello.api.dtos.output.order.RecoveryOrderDto;
import pedroleonez.fornello.api.entities.*;
import pedroleonez.fornello.api.enums.PaymentMethod;
import pedroleonez.fornello.api.enums.Status;
import pedroleonez.fornello.api.mappers.OrderMapper;
import pedroleonez.fornello.api.repositories.OrderRepository;
import pedroleonez.fornello.api.repositories.ProductVariationRepository;
import pedroleonez.fornello.api.repositories.UserRepository;
import pedroleonez.fornello.api.security.authentication.JwtTokenService;
import pedroleonez.fornello.api.enums.RoleName;
import pedroleonez.fornello.api.exceptions.ProductVariationNotFoundException;
import pedroleonez.fornello.api.exceptions.OrderNotFoundForUserException;
import pedroleonez.fornello.api.exceptions.OrderNotFoundException;
import pedroleonez.fornello.api.exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final JwtTokenService jwtTokenService;
    private final ProductVariationRepository productVariationRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderService(JwtTokenService jwtTokenService, ProductVariationRepository productVariationRepository, OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.jwtTokenService = jwtTokenService;
        this.productVariationRepository = productVariationRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    public RecoveryOrderDto createOrder(String token, CreateOrderDto createOrderDto) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;

        for(CreateOrderItemDto createOrderItemDto : createOrderDto.orderItems()) {
            ProductVariation productVariation = productVariationRepository
                    .findByProductIdAndProductVariationId(createOrderItemDto.productId(), createOrderItemDto.productVariationId())
                    .orElseThrow(ProductVariationNotFoundException::new);

            amount = amount.add(productVariation.getPrice().multiply(new BigDecimal(createOrderItemDto.quantity())));

            OrderItem orderItem = OrderItem.builder()
                    .quantity(createOrderItemDto.quantity())
                    .productVariation(productVariation)
                    .build();

            orderItems.add(orderItem);
        }

        DeliveryData deliveryData = DeliveryData.builder()
                .receiverName(createOrderDto.deliveryData().receiverName())
                .address(createOrderDto.deliveryData().address())
                .number(createOrderDto.deliveryData().number())
                .complement(createOrderDto.deliveryData().complement())
                .district(createOrderDto.deliveryData().district())
                .zipCode(createOrderDto.deliveryData().zipCode())
                .city(createOrderDto.deliveryData().city())
                .state(createOrderDto.deliveryData().state())
                .phoneNumber(createOrderDto.deliveryData().phone_number())
                .build();

        User user = getUser(token);

        Order order = Order.builder()
                .user(user)
                .paymentMethod(PaymentMethod.valueOf(createOrderDto.paymentMethod().toUpperCase()))
                .orderItems(orderItems)
                .amount(amount)
                .deliveryData(deliveryData)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        deliveryData.setOrder(order);

        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public RecoveryOrderDto getOrderById(String token, Long orderId) {
        User user = getUser(token);

        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            return orderMapper.mapOrderToRecoveryOrderDto(orderRepository
                    .findByOrderIdAndUserId(orderId, user.getId())
                    .orElseThrow(OrderNotFoundForUserException::new));
        }
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new));
    }

    public Page<RecoveryOrderDto> getOrders(String token, Pageable pageable) {
        User user = getUser(token);

        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(), pageable);
            return orderPage.map(orderMapper::mapOrderToRecoveryOrderDto);
        }
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::mapOrderToRecoveryOrderDto);
    }

    public Page<RecoveryOrderDto> getOrderByStatus(String statusName, String token, Pageable pageable) {
        User user = getUser(token);

        if (user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_CUSTOMER))) {
            Page<Order> orderPage = orderRepository.findOrderByStatusAndUser(Status.valueOf(statusName.toUpperCase()), user.getId(), pageable);
            return orderPage.map(orderMapper::mapOrderToRecoveryOrderDto);
        }
        Page<Order> orderPage = orderRepository.findByStatus(Status.valueOf(statusName.toUpperCase()), pageable);
        return orderPage.map(orderMapper::mapOrderToRecoveryOrderDto);
    }

    public RecoveryOrderDto changeOrderStatus(Long orderId, UpdateStatusOrderDto updateStatusOrderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundForUserException::new);
        order.setStatus(Status.valueOf(updateStatusOrderDto.status().toUpperCase()));
        return orderMapper.mapOrderToRecoveryOrderDto(orderRepository.save(order));
    }

    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(orderId);
    }

    private User getUser(String token) {
        String subject = jwtTokenService.getSubjectFromToken(token.replace("Bearer ", ""));
        return userRepository.findByEmail(subject).orElseThrow(UserNotFoundException::new);
    }
}
