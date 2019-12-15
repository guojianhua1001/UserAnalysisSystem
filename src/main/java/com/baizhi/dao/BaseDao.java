package com.baizhi.dao;

import java.util.List;

public interface BaseDao<T> {
    //添加
    void insert(T t);

    //修改
    void update(T t);

    //删除
    void delete(String id);

    //根据id查询
    T selectById(String id);

    //查询所有
    List<T> selectAll();
}
