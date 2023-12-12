package com.example.dao;

import com.example.entity.XuankeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface XuankeInfoDao extends Mapper<XuankeInfo> {

    @Select("select * from xuanke_info where name = #{name} and teacherId = #{teacherId} and studentId = #{studentId} limit 1")
    XuankeInfo find(@Param("name") String name, @Param("teacherId") Long teacherId, @Param("studentId") Long studentId);

    List<XuankeInfo> findByCondition(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);
}
