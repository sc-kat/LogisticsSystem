package com.example.project.controller;

import com.example.project.dto.DestinationDto;
import com.example.project.exception.ConditionsNotMetException;
import com.example.project.exception.DataNotFound;
import com.example.project.service.DestinationService;
import com.example.project.support.MyResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public Long addDestinations(@Valid @RequestBody DestinationDto destinationDto) throws ConditionsNotMetException {
        return destinationService.addDestination(destinationDto);
    }

    @PutMapping("/update")
    public MyResponse updateDestination(@Valid @RequestBody DestinationDto destinationDto) throws DataNotFound, ConditionsNotMetException {
        List<Long> toReturnIds = new ArrayList<>();
        toReturnIds.add(destinationService.updateDestination(destinationDto));


        return new MyResponse("Update done.", toReturnIds);

    }

    @GetMapping
    public List<DestinationDto> getAllDestinations(){
        return destinationService.getAllDestinations();
    }
    @GetMapping("/{destinationId}")
    public ResponseEntity<Object> getDestinationById(@PathVariable(name = "destinationId") String destinationId) throws DataNotFound {
        long id;

        try {
            id = Long.parseLong(destinationId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("The provided id does not have the correct format.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(destinationService.getDestinationById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Object> deleteDestinationById(@PathVariable(name = "destinationId") String destinationId) throws DataNotFound {
        long id;
        try {
            id = Long.parseLong(destinationId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("The provided id does not have the correct format.", HttpStatus.BAD_REQUEST);
        }
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>("The destination was deleted.", HttpStatus.OK);
    }
}
