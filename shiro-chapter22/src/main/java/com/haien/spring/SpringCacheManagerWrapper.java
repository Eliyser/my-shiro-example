package com.haien.spring;

import net.sf.ehcache.Ehcache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author haien
 * @Description 包装Spring Cache，因为spring的cache没有shiro的cache那些功能，
 *              但又有其优点，所以给它封装一些shiro中比较好的方法进去。
 * @Date 2019/3/16
 **/
public class SpringCacheManagerWrapper implements CacheManager {
    private org.springframework.cache.CacheManager cacheManager;

    //由xml文件注入一个Spring框架的cache对象
    public void setCacheManager(org.springframework.cache.CacheManager cacheManager){
        this.cacheManager=cacheManager;
    }

    /**
     * @Author haien
     * @Description 获取注入进来的cache对象并封装成SpringCacheWrapper对象，
     *              其中就提供了shiro中较好的方法。
     * @Date 2019/3/16
     * @Param [name]
     * @return org.apache.shiro.cache.Cache<K,V>
     **/
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        //获取上面setCacheManager()
        org.springframework.cache.Cache springCache=cacheManager.getCache(name);
        return new SpringCacheWrapper(springCache);
    }

    /**
     * @Author haien
     * @Description 内部类：继承shiro的Cache，重写了它的方法，封装给当前类的springcache对象
     * @Date 2019/3/16
     **/
    static class SpringCacheWrapper implements Cache{
        private org.springframework.cache.Cache springCache;

        SpringCacheWrapper(org.springframework.cache.Cache springCache) {
            this.springCache = springCache;
        }

        @Override
        public Object get(Object key) throws CacheException {
            Object value=springCache.get(key);
            if(value instanceof SimpleValueWrapper) return ((SimpleValueWrapper)value).get();
            return value;
        }

        @Override
        public Object put(Object key, Object value) throws CacheException {
            springCache.put(key,value);
            return value;
        }

        @Override
        public Object remove(Object key) throws CacheException {
            springCache.evict(key);
            return null;
        }

        @Override
        public void clear() throws CacheException {
            springCache.clear();
        }

        @Override
        public int size() {
            if(springCache.getNativeCache() instanceof Ehcache){
                Ehcache ehcache=(Ehcache)springCache.getNativeCache();
                return ehcache.getSize();
            }
            throw new UnsupportedOperationException(
                    "invoke spring cache abstract size method not supported");
        }

        @Override
        public Set keys() {
            if(springCache.getNativeCache() instanceof Ehcache){
                Ehcache ehcache=(Ehcache)springCache.getNativeCache();
                return new HashSet(ehcache.getKeys());
            }
            throw new UnsupportedOperationException(
                    "invoke spring caceh abstract keys method not supported");
        }

        @Override
        public Collection values() {
            if(springCache.getNativeCache() instanceof Ehcache){
                Ehcache ehcache=(Ehcache)springCache.getNativeCache();
                List keys=ehcache.getKeys();
                if(!CollectionUtils.isEmpty(keys)){
                    List values=new ArrayList(keys.size());
                    for(Object key:keys){
                        Object value=get(key);
                        if(value!=null)
                            values.add(value);
                    }
                    return Collections.unmodifiableList(values);
                }else{
                    return Collections.emptyList();
                }
            }
            throw new UnsupportedOperationException(
                    "invoke spring cache abstract values method not supported");
        }
    }
}



























