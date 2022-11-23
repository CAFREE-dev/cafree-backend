package net.cafree.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import net.cafree.domain.cafe.dto.request.CafeRegisterRequest;
import net.cafree.domain.cafe.dto.request.CafeUpdateRequest;
import net.cafree.domain.cafe.dto.response.CafeResponse;
import net.cafree.domain.cafe.dto.response.SimpleCafeResponse;
import net.cafree.domain.cafe.entity.Cafe;
import net.cafree.domain.cafe.service.CafeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/{id}")
    public ResponseEntity<CafeResponse> CafeDetails(@PathVariable Long id) {
        return ResponseEntity.ok(cafeService.findById(id).toCafeResponse());
    }

    @GetMapping
    public ResponseEntity<List<SimpleCafeResponse>> cafeList() {
        return ResponseEntity.ok(cafeService.findAll().stream()
                .map(Cafe::toSimpleCafeResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<CafeResponse> cafeAdd(@RequestBody @Validated CafeRegisterRequest cafeRegisterRequest) {
        return ResponseEntity.ok(cafeService.save(cafeRegisterRequest).toCafeResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CafeResponse> cafeModify(@PathVariable Long id, @RequestBody @Validated CafeUpdateRequest cafeUpdateRequest) {
        return ResponseEntity.ok(cafeService.update(id, cafeUpdateRequest).toCafeResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cafeRemove(@PathVariable Long id) {
        cafeService.delete(id);

        return ResponseEntity.ok().build();
    }

}
