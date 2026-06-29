package com.fifa.worldcuppredictor.controller;

import com.fifa.worldcuppredictor.dto.classement.ClassementEntry;
import com.fifa.worldcuppredictor.service.ClassementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classements")
@RequiredArgsConstructor
public class ClassementController {

    private final ClassementService classementService;

    @GetMapping("/general")
    public ResponseEntity<List<ClassementEntry>> general() {
        return ResponseEntity.ok(classementService.general());
    }

    @GetMapping("/ligue/{id}")
    public ResponseEntity<List<ClassementEntry>> ligue(@PathVariable Long id) {
        return ResponseEntity.ok(classementService.ligue(id));
    }
}
