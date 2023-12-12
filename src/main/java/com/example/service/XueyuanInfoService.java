package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.XueyuanInfoDao;
import com.example.entity.XueyuanInfo;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class XueyuanInfoService {

    @Resource
    private XueyuanInfoDao xueyuanInfoDao;


    public void add(XueyuanInfo xueyuanInfo) {
        XueyuanInfo info = xueyuanInfoDao.findByName(xueyuanInfo.getName());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException("-1", "该学院名称已存在");
        }
        xueyuanInfoDao.insertSelective(xueyuanInfo);
    }

    public List<XueyuanInfo> findAll() {
        return xueyuanInfoDao.selectAll();
    }

    public void update(XueyuanInfo xueyuanInfo) {
        xueyuanInfoDao.updateByPrimaryKeySelective(xueyuanInfo);
    }

    public void deleteById(Long id) {
        xueyuanInfoDao.deleteByPrimaryKey(id);
    }

    public List<XueyuanInfo> find(String search) {
        return xueyuanInfoDao.find(search);
    }
}
