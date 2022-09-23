package com.mszlu.blogparent.controller;

import com.mszlu.blogparent.vo.Result;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
//        ll
      return Result.success(null);
    }
}
