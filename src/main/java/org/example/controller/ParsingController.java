package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.dto.ParsingStatusDto;
import org.example.service.ParsingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/parse")
@Slf4j
@RequiredArgsConstructor
public class ParsingController {
    private final ParsingService parsingService;

    @PostMapping("/start")
    public ResponseEntity<?> startParsing() {
        if (parsingService.isParsingNow()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Парсинг уже выполняется"));
        }
        parsingService.startParsingAsync();

        return ResponseEntity.accepted()
                .body(Map.of("message", "Парсинг запущен"));
    }

    @GetMapping("/status")
    public ResponseEntity<ParsingStatusDto> getStatus() {
        return ResponseEntity.ok(
                ParsingStatusDto.from(parsingService.getCurrentStatus())
        );
    }

}
