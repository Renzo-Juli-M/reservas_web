package com.back.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.back.dto.RoomDTO;
import com.back.model.Room;
import com.back.service.IRoomService;
import com.back.util.MapperUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final IRoomService service;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> findAll() throws Exception {
        List<RoomDTO> list = mapperUtil.mapList(service.findAll(), RoomDTO.class, "roomMapper");
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> findById(@PathVariable("id") Integer id) throws Exception {
        RoomDTO obj = mapperUtil.map(service.findById(id), RoomDTO.class, "roomMapper");
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody RoomDTO dto) throws Exception {
        Room obj = service.save(mapperUtil.map(dto, Room.class, "roomMapper"));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdRoom()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody RoomDTO dto)
            throws Exception {
        Room obj = service.update(mapperUtil.map(dto, Room.class, "roomMapper"), id);
        return ResponseEntity.ok(mapperUtil.map(obj, RoomDTO.class, "roomMapper"));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<RoomDTO> updateRoomAvailability(
        @PathVariable Integer id, 
        @RequestParam boolean available
    ) throws Exception {
        Room updatedRoom = service.updateAvailability(id, available);
        return ResponseEntity.ok(mapperUtil.map(updatedRoom, RoomDTO.class, "roomMapper"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}