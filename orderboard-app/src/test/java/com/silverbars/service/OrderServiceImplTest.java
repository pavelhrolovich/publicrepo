package com.silverbars.service;

import com.silverbars.dao.OrderDao;
import com.silverbars.enums.OrderType;
import com.silverbars.exception.OrderBoardInvalidOperationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderServiceImpl classToTest;

    @Test
    public void shouldRegisterOrderSuccessfully() {
        // Given
        String user = "Test User";
        double quantity = 2.5;
        BigDecimal price = BigDecimal.TEN;
        OrderType orderType = OrderType.BUY;

        // When
        classToTest.registerOrder(user, quantity, price, orderType);

        // Then

        verify(orderDao).registerOrder(user, quantity, price, orderType);
    }

    @Test
    public void shouldCancelOrderSuccessfully() throws OrderBoardInvalidOperationException {
        // Given
        long orderId = 1;
        String user = "Test User";

        // When
        classToTest.cancelOrder(orderId, user);

        // Then
        verify(orderDao).cancelOrder(orderId, user);
    }

    @Test
    public void shouldThrowExceptionWhenInvalidCancellationRequestExecuted() throws OrderBoardInvalidOperationException {
        // Given
        long orderId = 1;
        String user = "Test User";
        doThrow(new OrderBoardInvalidOperationException("Order Id [1] is already cancelled by user [Test User]"))
                .when(orderDao)
                .cancelOrder(orderId, user);

        // When
        try {
            classToTest.cancelOrder(orderId, user);
            Assert.fail("Expected exception to be thrown");
        } catch (OrderBoardInvalidOperationException e) {
            assertThat(e.getMessage(), equalTo("Order Id [1] is already cancelled by user [Test User]"));
        }

        // Then
        verify(orderDao).cancelOrder(orderId, user);
    }

    @Test
    public void shouldGetLiveOrdersSuccessfully() {
        // Given

        // When
        classToTest.getLiveOrders();

        // Then
        verify(orderDao).getLiveOrders();
    }
}
