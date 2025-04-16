package pedroleonez.fornello.api.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedroleonez.fornello.api.dtos.input.order.CreateOrderDto;
import pedroleonez.fornello.api.dtos.input.order.UpdateStatusOrderDto;
import pedroleonez.fornello.api.dtos.output.order.RecoveryOrderDto;
import pedroleonez.fornello.api.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<RecoveryOrderDto> createOrder(@RequestHeader("Authorization") String token, @RequestBody @Valid CreateOrderDto createOrderDto) {
        return new ResponseEntity<>(orderService.createOrder(token, createOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<RecoveryOrderDto> getOrderById(@RequestHeader("Authorization") String token, @PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrderById(token, orderId), HttpStatus.OK);
    }

    @GetMapping("/status/{statusName}")
    public ResponseEntity<Page<RecoveryOrderDto>> getOrderByStatus(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({ @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC), //Critério de ordenação
                     @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @PathVariable String statusName,
            @RequestHeader("Authorization") String token
    ) {
        return new ResponseEntity<>(orderService.getOrderByStatus(statusName, token, pageable), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<RecoveryOrderDto> changeOrderStatus(@PathVariable Long orderId, @RequestBody UpdateStatusOrderDto updateStatusOrderDto) {
        return new ResponseEntity<>(orderService.changeOrderStatus(orderId, updateStatusOrderDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RecoveryOrderDto>> getOrders(
            @PageableDefault(size = 8)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC), //Critério de ordenação
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)}) //Critério de desempate
            Pageable pageable,
            @RequestHeader("Authorization") String token
    ) {
        return new ResponseEntity<>(orderService.getOrders(token, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
