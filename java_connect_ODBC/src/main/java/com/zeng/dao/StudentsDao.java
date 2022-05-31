package com.zeng.dao;

import com.zeng.bean.Students;

public class StudentsDao {
    public int addStudent(Students students){
        String sql = "insert into students ()  values (#{sid},#{sname},#{age},#{sex})";
        return 0;
    }
}
