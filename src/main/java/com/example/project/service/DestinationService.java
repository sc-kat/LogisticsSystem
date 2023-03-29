package com.example.project.service;

import com.example.project.converter.DestinationConverter;
import com.example.project.dto.DestinationDto;
import com.example.project.entity.DestinationEntity;
import com.example.project.exception.ConditionsNotMetException;
import com.example.project.exception.DataNotFound;
import com.example.project.repository.DestinationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationConverter destinationConverter;

    public DestinationService(DestinationRepository destinationRepository, DestinationConverter destinationConverter) {
        this.destinationRepository = destinationRepository;
        this.destinationConverter = destinationConverter;
    }


    public Long addDestination(DestinationDto destinationDto) throws ConditionsNotMetException {
        if(destinationRepository.findByName(destinationDto.getName()).isPresent()) {
            throw new ConditionsNotMetException("The destination provided already exists.");
        }

        DestinationEntity destinationEntity = destinationConverter.fromDtoToEntity(destinationDto);
        return destinationRepository.save(destinationEntity).getId();
    }

    public Long updateDestination(DestinationDto destinationDto) throws DataNotFound, ConditionsNotMetException {

        if(destinationDto.getId() == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }

        DestinationEntity destinationEntity = destinationRepository.findById(destinationDto.getId())
                    .orElseThrow(() -> new DataNotFound(
                            String.format("The destination with id %s, does not exist.", destinationDto.getId())));

        if(destinationRepository.findByName(destinationDto.getName()).isPresent() &&
                !destinationEntity.getName().equals(destinationDto.getName())){
            throw new ConditionsNotMetException("Destination is already present in the db with another Id.");
        }

        if(!destinationDto.getName().equals(" ")){
            destinationEntity.setName(destinationDto.getName());
        }

            destinationEntity.setDistance(destinationDto.getDistance());


        return destinationRepository.save(destinationEntity).getId();
    }


    public List<DestinationDto> getAllDestinations() {

        return StreamSupport.stream(destinationRepository.findAll().spliterator(), false).toList().stream()
                    .map(destinationConverter::fromEntityToDto)
                    .toList();

    }

    public DestinationDto getDestinationById(Long id) throws DataNotFound {
        Optional<DestinationEntity> destinationById = destinationRepository.findById(id);

        if(destinationById.isEmpty()) {
            throw new DataNotFound("The Id could not be found in the database.");
        }


        return destinationConverter.fromEntityToDto(destinationById.get());

    }

    public void deleteDestinationById(Long id) throws DataNotFound {

        if(!destinationRepository.existsById(id)) {
            throw new DataNotFound(String.format("The destination with id %s does not exist in the database.", id));
        }

        destinationRepository.deleteById(id);

    }
}
