package com.example.controller;

import com.example.common.Result;
import com.example.entity.StudentInfo;
import com.example.service.StudentInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/studentInfo")
public class StudentInfoController {

    @Resource
    private StudentInfoService studentInfoService;

    @PostMapping
    public  Result add(@RequestBody StudentInfo studentInfo) {
        studentInfoService.add(studentInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody StudentInfo studentInfo) {
        studentInfoService.update(studentInfo);
        return  Result.success();
    }

    @GetMapping
    public Result findAll() {
        List<StudentInfo> list = studentInfoService.findAll();
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        studentInfoService.deleteById(id);
        return Result.success();
    }
}
