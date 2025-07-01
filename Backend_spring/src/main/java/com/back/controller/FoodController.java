package com.back.controller;

import com.back.dto.FoodDTO;
import com.back.model.Food;
import com.back.model.Entrepreneur;
import com.back.service.IFoodService;
import com.back.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

    private final IFoodService service;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<FoodDTO>> findAll() throws Exception {
        List<FoodDTO> list = mapperUtil.mapList(service.findAll(), FoodDTO.class, "foodMapper");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> findById(@PathVariable Integer id) throws Exception {
        FoodDTO dto = mapperUtil.map(service.findById(id), FoodDTO.class, "foodMapper");
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody FoodDTO dto) throws Exception {
        Food food = mapperUtil.map(dto, Food.class, "foodMapper");
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setIdEntrepreneur(dto.getEntrepreneurId());
        food.setEntrepreneur(entrepreneur);

        Food saved = service.save(food);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getIdFood()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDTO> update(@PathVariable Integer id,
                                          @Valid @RequestBody FoodDTO dto) throws Exception {
        Food food = mapperUtil.map(dto, Food.class, "foodMapper");
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setIdEntrepreneur(dto.getEntrepreneurId());
        food.setEntrepreneur(entrepreneur);

        Food updated = service.update(food, id);
        return ResponseEntity.ok(mapperUtil.map(updated, FoodDTO.class, "foodMapper"));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<FoodDTO> updateAvailability(@PathVariable Integer id,
                                                      @RequestParam boolean available) throws Exception {
        Food updated = service.updateAvailability(id, available);
        return ResponseEntity.ok(mapperUtil.map(updated, FoodDTO.class, "foodMapper"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}