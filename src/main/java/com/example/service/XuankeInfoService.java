package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class XuankeInfoService {

    @Resource
    private XuankeInfoDao xuankeInfoDao;
    @Resource
    private ZhuanyeInfoDao zhuanyeInfoDao;
    @Resource
    private TeacherInfoDao teacherInfoDao;
    @Resource
    private StudentInfoDao studentInfoDao;

    @Resource
    private ClassInfoDao classInfoDao;

    public List<XuankeInfo> findAll(HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("user");
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException("-1", "登陆已失效，请重新登录");
        }
        List<XuankeInfo> list;

        if (1 == user.getLevel()){
            // 管理员
            list = xuankeInfoDao.selectAll();
        } else if (2 == user.getLevel()){
            // 教师
            list = xuankeInfoDao.findByCondition(user.getId(), null);
        } else {
            // 学生
            list = xuankeInfoDao.findByCondition(null, user.getId());
        }
        for (XuankeInfo xuankeInfo : list) {
            ZhuanyeInfo zhuanyeInfo = zhuanyeInfoDao.selectByPrimaryKey(xuankeInfo.getZhuanyeId());
            TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(xuankeInfo.getTeacherId());
            StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(xuankeInfo.getStudentId());
            xuankeInfo.setZhuanyeName(zhuanyeInfo.getName());
            xuankeInfo.setTeacherName(teacherInfo.getName());
            xuankeInfo.setStudentName(studentInfo.getName());
        }
        return list;
    }

    public void add(XuankeInfo xuankeInfo) {
        xuankeInfoDao.insertSelective(xuankeInfo);
    }

    public XuankeInfo find(String name, Long teacherId, Long studentId) {
        return  xuankeInfoDao.find(name, teacherId, studentId);
    }

    public void delete(Long id) {
        XuankeInfo xuankeInfo = xuankeInfoDao.selectByPrimaryKey(id);
        ClassInfo classInfo = classInfoDao.findBynameAndTeacher(xuankeInfo.getName(), xuankeInfo.getTeacherId());
        xuankeInfoDao.deleteByPrimaryKey(id);
        classInfo.setYixuan(classInfo.getYixuan() - 1);
        classInfoDao.updateByPrimaryKeySelective(classInfo);
    }

    public void update(XuankeInfo xuankeInfo) {
        xuankeInfoDao.updateByPrimaryKeySelective(xuankeInfo);
    }
}
