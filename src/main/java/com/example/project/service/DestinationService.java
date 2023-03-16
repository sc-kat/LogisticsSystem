package com.example.project.service;

import com.example.project.converter.DestinationConverter;
import com.example.project.converter.OrderConverter;
import com.example.project.dto.DestinationDto;
import com.example.project.entity.DestinationEntity;
import com.example.project.exception.DataNotFound;
import com.example.project.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationConverter destinationConverter;
    private final OrderConverter orderConverter;

    public DestinationService(DestinationRepository destinationRepository, DestinationConverter destinationConverter, OrderConverter orderConverter) {
        this.destinationRepository = destinationRepository;
        this.destinationConverter = destinationConverter;
        this.orderConverter = orderConverter;
    }


    public Long addDestination(DestinationDto destinationDto) {
        DestinationEntity destinationEntity = destinationConverter.fromDtoToEntity(destinationDto);
        destinationEntity.setOrders(orderConverter.fromDtosToEntities(destinationDto.getOrders()));
        destinationEntity.getOrders().forEach(orderEntity -> orderEntity.setDestination(destinationEntity));

        return destinationRepository.save(destinationEntity).getId();
    }

    public Long updateDestination(DestinationDto destinationDto) throws DataNotFound {
        //TODO validare pe ID
        DestinationEntity destinationEntity = destinationRepository.findById(destinationDto.getId())
                    .orElseThrow(() -> new DataNotFound(String.format("The destination with id %s, does not exist.", destinationDto.getId())));


        destinationEntity.setName(destinationDto.getName());
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
