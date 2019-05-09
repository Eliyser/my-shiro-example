package com.haien.chapter19.dao;

import com.haien.chapter19.entity.UrlFilter;

import java.util.List;

public interface UrlFilterDao {
    public UrlFilter createUrlFilter(UrlFilter urlFilter);
    public UrlFilter updateUrlFilter(UrlFilter urlFilter);
    public void deleteUrlFilter(Long urlFilterId);

    public UrlFilter findOne(Long urlFilterId);
    public List<UrlFilter> findAll();
}
