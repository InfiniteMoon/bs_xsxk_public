package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.dao.StudentInfoDao;
import com.example.dao.XueyuanInfoDao;
import com.example.entity.Account;
import com.example.entity.StudentInfo;
import com.example.entity.XueyuanInfo;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentInfoService {

    @Resource
    private StudentInfoDao studentInfoDao;
    @Resource
    private XueyuanInfoDao xueyuanInfoDao;


    public Account login(String name, String password) {
        // 去数据库里根据用户名和密码查询学生信息
        StudentInfo studentInfo = studentInfoDao.findByNameAndPassword(name, password);
        if(ObjectUtil.isEmpty(studentInfo)){
            throw new CustomException("-1", "用户名、密码或者角色选择错误");
        }

        return  studentInfo;
    }

    public void register(StudentInfo studentInfo) {
        // 1.从数据库中根据用户名查询出一条信息，如果有，说明用户名重复，提示重新输入
        StudentInfo info = studentInfoDao.findByName(studentInfo.getName());
        if(ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        // 2.如果没有，说明可以注册
        studentInfoDao.insertSelective(studentInfo);
    }

    public StudentInfo findById(Long id) {
        return  studentInfoDao.selectByPrimaryKey(id);
    }

    public void update(StudentInfo studentInfo) {
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public List<StudentInfo> findAll() {
        List<StudentInfo> list = studentInfoDao.selectAll();
        // 处理一下这个list，把里面每一个学生信息对应的学院名称赋值进去
        for (StudentInfo studentInfo : list){
            if (ObjectUtil.isNotEmpty(studentInfo.getXueyuanId())){
                XueyuanInfo xueyuanInfo = xueyuanInfoDao.selectByPrimaryKey(studentInfo.getXueyuanId());
                studentInfo.setXueyuanName(xueyuanInfo.getName());
            }
        }
        return list;
    }

    public void add(StudentInfo studentInfo) {
        // 1.从数据库中根据用户名查询出一条信息，如果有，说明用户名重复，提示重新输入
        StudentInfo info = studentInfoDao.findByName(studentInfo.getName());
        if(ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        // 2.如果没有密码，初始化默认密码
        if (ObjectUtil.isEmpty(studentInfo.getPassword())) {
            studentInfo.setPassword("123456");
        }
        studentInfoDao.insertSelective(studentInfo);
    }

    public void deleteById(Long id) {
        studentInfoDao.deleteByPrimaryKey(id);
    }
}
