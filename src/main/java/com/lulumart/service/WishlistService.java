package com.lulumart.service;

import com.lulumart.dao.WishlistDao;
import com.lulumart.model.WishlistItem;

import java.util.List;

public class WishlistService {

    private final WishlistDao wishlistDao;

    public WishlistService() {
        this(new WishlistDao());
    }

    public WishlistService(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /** Adds to the wishlist; returns true when the item is now present. */
    public boolean toggle(long userId, long productId) {
        if (wishlistDao.contains(userId, productId)) {
            wishlistDao.removeItem(userId, productId);
            return false;
        }
        wishlistDao.addItem(userId, productId);
        return true;
    }

    public void add(long userId, long productId) {
        wishlistDao.addItem(userId, productId);
    }

    public void remove(long userId, long productId) {
        wishlistDao.removeItem(userId, productId);
    }

    public List<WishlistItem> items(long userId) {
        return wishlistDao.items(userId);
    }

    public boolean contains(long userId, long productId) {
        return wishlistDao.contains(userId, productId);
    }
}