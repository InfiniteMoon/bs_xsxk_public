package com.example.dao;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.XueyuanInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface XueyuanInfoDao extends Mapper<XueyuanInfo> {

    @Select("select * from xueyuan_info where name = #{name}")
    XueyuanInfo findByName(@Param("name") String name);

    @Select("select * from xueyuan_info where name like concat('%', #{search}, '%')")
    List<XueyuanInfo> find(@Param("search") String search);
}
