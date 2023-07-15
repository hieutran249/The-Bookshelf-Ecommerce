package com.hieutt.ecommerceweb.repository;

import com.hieutt.ecommerceweb.entity.Order;
import com.hieutt.ecommerceweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUser(User user);
}
