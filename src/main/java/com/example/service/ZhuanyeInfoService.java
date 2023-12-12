package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.dao.XueyuanInfoDao;
import com.example.dao.ZhuanyeInfoDao;
import com.example.entity.XueyuanInfo;
import com.example.entity.ZhuanyeInfo;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ZhuanyeInfoService {

    @Resource
    private ZhuanyeInfoDao zhuanyeInfoDao;
    @Resource
    private XueyuanInfoDao xueyuanInfoDao;


    public void add(ZhuanyeInfo zhuanyeInfo) {
        // 判断当前专业名称在数据库里有没有
        ZhuanyeInfo info = zhuanyeInfoDao.findByName(zhuanyeInfo.getName());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException("-1", "您输入的专业名称已存在");
        }
        zhuanyeInfoDao.insertSelective(zhuanyeInfo);
    }


    public List<ZhuanyeInfo> findAll() {
        List<ZhuanyeInfo> list = zhuanyeInfoDao.selectAll();
        for (ZhuanyeInfo zhuanyeInfo : list) {
            XueyuanInfo xueyuanInfo = xueyuanInfoDao.selectByPrimaryKey(zhuanyeInfo.getXueyuanId());
            zhuanyeInfo.setXueyuanName(xueyuanInfo.getName());
        }

        return list;
    }

    public void update(ZhuanyeInfo zhuanyeInfo) {
        zhuanyeInfoDao.updateByPrimaryKeySelective(zhuanyeInfo);
    }

    public void deleteById(Long id) {
        zhuanyeInfoDao.deleteByPrimaryKey(id);
    }

    public List<ZhuanyeInfo> findSearch(String search) {
        return zhuanyeInfoDao.findBySearch(search);
    }
}
