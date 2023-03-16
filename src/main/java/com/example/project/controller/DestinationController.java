package com.example.project.controller;

import com.example.project.dto.DestinationDto;
import com.example.project.exception.DataNotFound;
import com.example.project.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
@Validated
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @PostMapping("/add")
    public Long addDestinations(@Valid @RequestBody DestinationDto destinationDto) {
        return destinationService.addDestination(destinationDto);
    }

    @PutMapping("/update")
    public Long updateDestination(@Valid @RequestBody DestinationDto destinationDto) throws DataNotFound {
        return destinationService.updateDestination(destinationDto);
    }

    @GetMapping
    public List<DestinationDto> getAllDestinations(){
        return destinationService.getAllDestinations();
    }
    @GetMapping("/{destinationId}")
    public ResponseEntity<Object> getDestinationById(@PathVariable(name = "destinationId") String destinationId) throws DataNotFound {
        Long id;

        try {
            id = Long.valueOf(destinationId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("The provided id does not have the correct format.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(destinationService.getDestinationById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Object> deleteDestinationById(@PathVariable(name = "destinationId") String destinationId) throws DataNotFound {
        Long id;
        try {
            id = Long.valueOf(destinationId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("The provided id does not have the correct format.", HttpStatus.BAD_REQUEST);
        }
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>("The destination was deleted.", HttpStatus.OK);
    }
}
