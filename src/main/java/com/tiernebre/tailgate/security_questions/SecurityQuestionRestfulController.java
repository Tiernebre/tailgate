package com.tiernebre.tailgate.security_questions;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security-questions")
@RequiredArgsConstructor
public class SecurityQuestionRestfulController {
    private final SecurityQuestionService service;

    @PostMapping
    public List<SecurityQuestionDto> getAll() {
        return service.getAll();
    }
}
