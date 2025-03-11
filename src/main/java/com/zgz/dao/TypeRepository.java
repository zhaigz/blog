package com.zgz.dao;

import com.zgz.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    //自定义
    Type findByName(String name);

    //目的：根据Type实体中blog的size进行排序，可以采用Pageable的分页功能实现条数控制
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
