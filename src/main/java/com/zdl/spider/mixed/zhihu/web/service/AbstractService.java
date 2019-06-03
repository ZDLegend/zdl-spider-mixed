package com.zdl.spider.mixed.zhihu.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

/**
 * Created by ZDLegend on 2019/6/3 15:09
 */
public abstract class AbstractService<E> {

    @Autowired
    private EntityManager entityManager;

    public abstract JpaRepository<E, String> getDao();

    public E getById(String id) {
        return getDao().findById(id).orElse(null);
    }

    public E insert(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    public E delete(String id) {
        E entity = getById(id);
        getDao().deleteById(id);
        return entity;
    }
}
