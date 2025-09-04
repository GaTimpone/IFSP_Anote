package com.ifsp.app.controller;

import com.ifsp.app.model.Tag;
import com.ifsp.app.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService= tagService;
    }

    @GetMapping
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    @PostMapping
    public Tag create(@RequestBody Tag tag) {
        return tagService.save(tag);
    }
}
