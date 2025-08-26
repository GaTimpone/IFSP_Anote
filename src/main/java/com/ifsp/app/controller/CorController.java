package com.ifsp.app.controller;

import com.ifsp.app.model.enums.Cor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cores")
public class CorController {

    @GetMapping
    public List<String> listarCores() {
        return Arrays.stream(Cor.values()).map(Enum::name).collect(Collectors.toList());
    }
}
