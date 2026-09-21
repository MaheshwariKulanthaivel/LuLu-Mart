package com.lulumart.service;

import com.lulumart.dao.OrderDao;
import com.lulumart.dao.ProductDao;
import com.lulumart.dto.Stats;

public class SellerService {

    private final ProductDao productDao;
    private final OrderDao orderDao;

    public SellerService() {
        this(new ProductDao(), new OrderDao());
    }

    public SellerService(ProductDao productDao, OrderDao orderDao) {
        this.productDao = productDao;
        this.orderDao = orderDao;
    }

    public Stats dashboardStats(long sellerId) {
        Stats stats = new Stats();
        stats.setTotalProducts(productDao.countBySeller(sellerId));
        stats.setLowStockProducts(productDao.lowStockBySeller(sellerId, 5));
        stats.setIncomingOrders(orderDao.countByStatus("PENDING"));
        stats.setTotalSales(productDao.sellerSales(sellerId));
        return stats;
    }
}