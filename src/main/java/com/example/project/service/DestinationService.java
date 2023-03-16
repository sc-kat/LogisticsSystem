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

        Optional<DestinationEntity> destinationByIdOptional = destinationRepository.findById(destinationDto.getId());
        if(destinationByIdOptional.isEmpty()) {
            throw new DataNotFound(String.format("The destination with id %s, does not exist.", destinationDto.getId()));
        }

        DestinationEntity destinationEntity = destinationByIdOptional.get();

            destinationEntity.setName(destinationDto.getName());  //TODO Q: mai verificam daca name/distance sunt not-null daca sunt setate nonEmpty in Dto?
            destinationEntity.setDistance(destinationDto.getDistance());

        return destinationRepository.save(destinationEntity).getId();
    }


    public List<DestinationDto> getAllDestinations() {

        return StreamSupport.stream(destinationRepository.findAll().spliterator(), false).toList().stream()
                    .map(destinationConverter::fromEntityToDto)
                    .toList();

    }

    public DestinationDto getDestinationById(Long id) {
        //code to write

        return null; // null to be changed

    }

    public void deleteDestinationById(Long id) {

        //delete destination fara cascade? Orderele raman disponibile?
    }
}
