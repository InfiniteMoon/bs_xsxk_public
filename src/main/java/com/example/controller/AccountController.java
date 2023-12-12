package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.service.AdminInfoService;
import com.example.service.StudentInfoService;
import com.example.service.TeacherInfoService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 跟账号相关的接口
* */
@RestController
@RequestMapping
public class AccountController {

    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private TeacherInfoService teacherInfoService;
    @Resource
    private StudentInfoService studentInfoService;

    @RequestMapping("/captcha")
    public void  captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // png
        SpecCaptcha captcha = new SpecCaptcha(135, 33, 4);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        CaptchaUtil.out(captcha, request, response);
    }


    /*
    * 登陆
    * */
    @PostMapping("/login")
    public Result login(@RequestBody Account user, HttpServletRequest request){

        if (!CaptchaUtil.ver(user.getVerCode(), request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return Result.error("1001", "验证码不正确");
        }

        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel())){
            return Result.error("-1","请完善输入信息");
        }

        Integer level = user.getLevel();
        Account loginUser = new Account();
        if (1 == level){
            loginUser = adminInfoService.login(user.getName(),user.getPassword());
        }
        if (2 == level){
            loginUser = teacherInfoService.login(user.getName(),user.getPassword());
        }
        if (3 == level){
            loginUser = studentInfoService.login(user.getName(), user.getPassword());
        }

        request.getSession().setAttribute("user",loginUser);

        return Result.success(loginUser);
    }
    @PostMapping("/register")
    public Result register(@RequestBody Account user, HttpServletRequest request){
        // 数据有没有填
        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel())){
            return Result.error("-1","请完善输入信息");
        }

        Integer level = user.getLevel();
        if(2 == level){
            // 教师注册
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(user, teacherInfo);
            teacherInfoService.add(teacherInfo);
        }
        if(3 == level){
            // 学生注册
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(user, studentInfo);
            studentInfoService.add(studentInfo);
        }

        return  Result.success();
    }
    @GetMapping("/getUser")
    public  Result getUser(HttpServletRequest request){
        Account user = (Account) request.getSession().getAttribute("user");
        Integer level = user.getLevel();
        AdminInfo loginUser = new AdminInfo();
        if (1 == level){
            // 获取管理员
            AdminInfo adminInfo = adminInfoService.findById(user.getId());
            return  Result.success(adminInfo);
        }
        if (2 == level){
            // 从教师表里面获取教师信息
            TeacherInfo teacherInfo = teacherInfoService.findById(user.getId());
            return Result.success(teacherInfo);
        }
        if (3 == level){
            // 从学生表里面获取学生信息
            StudentInfo studentInfo = studentInfoService.findById(user.getId());
            return  Result.success(studentInfo);
        }
        return  Result.success(new Account());
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account, HttpServletRequest request) {

        // 1.当前登陆角色
        Account user = (Account) request.getSession().getAttribute("user");

        // 2.更新密码
        String oldPassword = account.getPassword();

        if (!user.getPassword().equals(oldPassword)) {
            return Result.error("-1", "原密码输入错误");
        }

        String newPassword = account.getNewPassword();


        Integer level = user.getLevel();
        if (1 == level){
            AdminInfo adminInfo = new AdminInfo();
            BeanUtils.copyProperties(user, adminInfo);
            adminInfo.setPassword(newPassword);
            adminInfoService.update(adminInfo);

        }
        if (2 == level){
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(user, teacherInfo);
            teacherInfo.setPassword(newPassword);
            teacherInfoService.update(teacherInfo);

        }
        if (3 == level){
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(user, studentInfo);
            studentInfo.setPassword(newPassword);
            studentInfoService.update(studentInfo);

        }

        // 3.把session中信息清掉
        request.getSession().setAttribute("user", null);

        return Result.success();
    }

    @GetMapping("/logout")
    public  Result logout(HttpServletRequest request) {
        request.getSession().setAttribute("user", null);
        return  Result.success();
    }
}
