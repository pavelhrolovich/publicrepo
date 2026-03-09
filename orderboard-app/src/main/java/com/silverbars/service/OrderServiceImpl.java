package com.silverbars.service;

import com.silverbars.bean.Order;
import com.silverbars.bean.OrderSummaryHolder;
import com.silverbars.dao.OrderDao;
import com.silverbars.enums.OrderType;
import com.silverbars.exception.OrderBoardInvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * For the sake of simplicity and because I am using in memory solution to store Data,
 * I have not added Transactional Manager and related configuration to this project,
 * I am writing comments to show how the production grade application will look like
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * In a production environment this service will be Transactional
     */
    @Override
    public Order registerOrder(String user, double quantity, BigDecimal price, OrderType orderType) {
        return orderDao.registerOrder(user, quantity, price, orderType);
    }

    /**
     * In a production environment this service will be Transactional
     */
    @Override
    public Order cancelOrder(long orderId, String user) throws OrderBoardInvalidOperationException {
        return orderDao.cancelOrder(orderId, user);
    }

    /**
     * In a production environment this service will be Transactional Read Only
     */
    @Override
    public OrderSummaryHolder getLiveOrders() {
        return orderDao.getLiveOrders();
    }
}
