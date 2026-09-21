package com.lulumart.service;

import com.lulumart.dao.OrderDao;
import com.lulumart.dao.ProductDao;
import com.lulumart.dao.UserDao;
import com.lulumart.dto.Stats;
import com.lulumart.model.Role;

public class AdminService {

    private final UserDao userDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;

    public AdminService() {
        this(new UserDao(), new ProductDao(), new OrderDao());
    }

    public AdminService(UserDao userDao, ProductDao productDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.orderDao = orderDao;
    }

    public Stats dashboardStats() {
        Stats stats = new Stats();
        stats.setTotalUsers(userDao.countAll());
        stats.setTotalBuyers(userDao.countByRole(Role.BUYER));
        stats.setTotalSellers(userDao.countByRole(Role.SELLER));
        stats.setTotalProducts(productDao.countAll());
        stats.setTotalOrders(orderDao.countAll());
        stats.setTotalSales(orderDao.totalSales());
        return stats;
    }
}