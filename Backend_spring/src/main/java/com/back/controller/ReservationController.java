package com.back.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
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

import com.back.dto.ReservationDTO;
import com.back.dto.RoomDTO;
import com.back.model.Reservation;
import com.back.model.Room;
import com.back.repo.IReservationRepo;
import com.back.repo.IRoomRepo;
import com.back.service.IReservationService;
import com.back.util.MapperUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
	// @Autowired
	private final IReservationService service;

	private final MapperUtil mapperUtil;

	private final IReservationRepo reservationRepository;
	private final IRoomRepo roomRepository;

    private static final Logger log = Logger.getLogger(ReservationController.class.getName());

	
	@GetMapping
	public ResponseEntity<List<ReservationDTO>> findAll() throws Exception {
		// List<MedicDTO> list =
		// service.findAll().stream().map(this::convertToDto).toList();
		List<ReservationDTO> list = mapperUtil.mapList(service.findAll(), ReservationDTO.class, "reservationMapper");

		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservationDTO> findById(@PathVariable("id") Integer id) throws Exception {
		ReservationDTO obj = mapperUtil.map(service.findById(id), ReservationDTO.class, "reservationMapper");

		return ResponseEntity.ok(obj);
	}

	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody ReservationDTO dto) throws Exception {
		Reservation obj = service.save(mapperUtil.map(dto, Reservation.class, "reservationMapper"));

		// localhost:8080/medics/{id}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdReservation()).toUri();

		return ResponseEntity.created(location).build();
		// return new ResponseEntity<>(obj, HttpStatus.CREATED);
	}

	@PutMapping("/rooms/{id}")
	public ResponseEntity<Room> updateRoomAvailability(
	    @PathVariable Integer id, 
	    @RequestBody Map<String, Boolean> requestBody
	) {
	    // Verificar que el campo 'available' esté presente
	    if (!requestBody.containsKey("available")) {
	        return ResponseEntity.badRequest().build();
	    }

	    Optional<Room> roomOpt = roomRepository.findById(id);
	    if (roomOpt.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    // Actualizar solo el campo de disponibilidad
	    roomRepository.updateRoomAvailability(id, requestBody.get("available"));
	    
	    // Recuperar y devolver la habitación actualizada
	    Room updatedRoom = roomRepository.findById(id).get();
	    return ResponseEntity.ok(updatedRoom);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws Exception {
		service.delete(id);

		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/available")
	public ResponseEntity<List<RoomDTO>> findAvailableRooms(
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
	) throws Exception {
		System.out.println("Buscando habitaciones disponibles para fechas: {} hasta {}"+ checkIn+ checkOut);
	    List<Room> availableRooms = roomRepository.findAvailableRoomsInDateRange(checkIn, checkOut);
	    System.out.println("Habitaciones encontradas: {}"+ availableRooms.size());
	    
	    List<RoomDTO> availableRoomDTOs = mapperUtil.mapList(availableRooms, RoomDTO.class, "roomMapper");
	    
	    return ResponseEntity.ok(availableRoomDTOs);
	}


}
