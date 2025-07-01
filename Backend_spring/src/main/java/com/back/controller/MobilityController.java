package com.back.controller;

import com.back.dto.MobilityDTO;
import com.back.model.Mobility;
import com.back.service.IMobilityService;
import com.back.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mobility")
@RequiredArgsConstructor
public class MobilityController {

    private final IMobilityService service;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<MobilityDTO>> findAll() throws Exception {
        List<MobilityDTO> list = mapperUtil.mapList(service.findAll(), MobilityDTO.class, "mobilityMapper");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MobilityDTO> findById(@PathVariable("id") Integer id) throws Exception {
        MobilityDTO dto = mapperUtil.map(service.findById(id), MobilityDTO.class, "mobilityMapper");
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody MobilityDTO dto) throws Exception {
        Mobility obj = service.save(mapperUtil.map(dto, Mobility.class, "mobilityMapper"));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getIdMobility()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MobilityDTO> update(@PathVariable("id") Integer id,
                                              @Valid @RequestBody MobilityDTO dto) throws Exception {
        Mobility obj = service.update(mapperUtil.map(dto, Mobility.class, "mobilityMapper"), id);
        return ResponseEntity.ok(mapperUtil.map(obj, MobilityDTO.class, "mobilityMapper"));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<MobilityDTO> updateAvailability(@PathVariable Integer id,
                                                          @RequestParam boolean available) throws Exception {
        Mobility obj = service.updateAvailability(id, available);
        return ResponseEntity.ok(mapperUtil.map(obj, MobilityDTO.class, "mobilityMapper"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}