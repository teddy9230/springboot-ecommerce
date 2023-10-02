package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;


public interface OrderService {
    void saveOrder(ShoppingCart cart);

    void acceptOrder(Long id);

    void cancelOrder(Long id);

//    List<Order> findAll(String username);
//
//    List<Order> findALlOrders();
//

}
