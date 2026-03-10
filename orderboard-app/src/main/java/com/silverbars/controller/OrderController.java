package com.silverbars.controller;

import com.silverbars.bean.Order;
import com.silverbars.bean.OrderRequest;
import com.silverbars.bean.OrderSummaryHolder;
import com.silverbars.exception.OrderBoardInvalidOperationException;
import com.silverbars.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Rest Controller to facilitate operations on Order object
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
     * In a prod env User information can be extracted from the security context
     * I am using a default Test User which can be overridden if required
     */
    @PostMapping("/order")
    public ResponseEntity<Order> registerOrder(@RequestBody OrderRequest orderRequest, @RequestParam(defaultValue = "Test User") String user) {
        try {
            String effectiveUser = orderRequest.getUser() != null ? orderRequest.getUser() : user;
            Order order = orderService.registerOrder(effectiveUser, orderRequest.getQuantity(), orderRequest.getPrice(), orderRequest.getOrderType());
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            String errorMessage = "Unable to register order";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }

    @GetMapping("/order")
    public ResponseEntity<OrderSummaryHolder> getOrders() {
        try {
            OrderSummaryHolder orderSummaryHolder = orderService.getLiveOrders();
            if (CollectionUtils.isEmpty(orderSummaryHolder.getBuyOrderSummary()) && CollectionUtils.isEmpty(orderSummaryHolder.getSellOrderSummary())) {
                String errorMessage = "No Live Orders found in the system";
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
            } else {
                return ResponseEntity.ok(orderSummaryHolder);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Unable to retrieve orders from the system";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /*
     * In a prod env User information can be extracted from the security context
     * I am using a default Test User which can be overridden if required
     */
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable long orderId, @RequestParam(defaultValue = "Test User") String user) {
        try {
            Order order = orderService.cancelOrder(orderId, user);
            return ResponseEntity.ok(order);
        } catch (OrderBoardInvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            String errorMessage = "Unable to cancel order";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, e);
        }
    }
}
