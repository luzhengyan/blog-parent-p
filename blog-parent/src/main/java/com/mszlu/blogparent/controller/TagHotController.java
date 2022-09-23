package com.mszlu.blogparent.controller;

import com.mszlu.blogparent.service.TagService;
import com.mszlu.blogparent.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagHotController {
    @Autowired
    private TagService tagService;
    @GetMapping("hot")
    private Result hot(){
        int limit = 5;
        return tagService.hots(limit);
    }
    @GetMapping
    private Result findAll(){
        int limit = 5;
        return tagService.findAll();
    }
    @GetMapping("detail")
    private Result findAllDetail(){
        return tagService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    private Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}

