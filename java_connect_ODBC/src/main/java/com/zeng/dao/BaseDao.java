package com.zeng.dao;

import java.util.List;

public interface BaseDao<T> {
    public int add();
    public int del();
    public int update();
    public List<T> query();
}
