package com.example.project.support;

import com.example.project.entity.DestinationEntity;
import com.example.project.entity.OrderEntity;
import com.example.project.enums.OrderStatus;
import com.example.project.exception.DataNotFound;
import com.example.project.repository.DestinationRepository;
import com.example.project.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UpdateDbFromCsvFile {

    private final DestinationRepository destinationRepository;
    private final OrderRepository orderRepository;
    private final CurrentDateAndProfitService currentDateAndProfitService;

    public UpdateDbFromCsvFile(DestinationRepository destinationRepository, OrderRepository orderRepository, CurrentDateAndProfitService currentDateAndProfitService) {
        this.destinationRepository = destinationRepository;
        this.orderRepository = orderRepository;
        this.currentDateAndProfitService = currentDateAndProfitService;
    }

    public void uploadCsvFiles() throws DataNotFound {

        List<String> destinationsListLines = readInputFile("src/main/resources/destinations.csv");
        List<String> ordersListLines = readInputFile("src/main/resources/orders.csv");
        List<DestinationEntity> destinations = splitDestinationListLines(destinationsListLines);
        destinationRepository.saveAll(destinations);

        List<OrderEntity> orders = splitOrderListLines(ordersListLines);
        orderRepository.saveAll(orders);

    }

    public List<DestinationEntity> splitDestinationListLines(List<String> inputLinesList) {

        List<DestinationEntity> destinations = new ArrayList<>();

        for(String line: inputLinesList) {
            DestinationEntity destinationEntity = new DestinationEntity();
            String[] elements = line.split(",");

            destinationEntity.setName(elements[0].trim());
            destinationEntity.setDistance(Integer.parseInt(elements[1].trim()));

            destinations.add(destinationEntity);
        }
        return destinations;
    }

    public List<OrderEntity> splitOrderListLines(List<String> inputLinesList) throws DataNotFound {

        List<OrderEntity> orders = new ArrayList<>();

        for(String line: inputLinesList) {

            OrderEntity orderEntity = new OrderEntity();
            String[] elements = line.split(",");
            String destinationName = elements[0];
            Optional<DestinationEntity> destination = destinationRepository.findByName(destinationName);
            if(destination.isEmpty()){
                throw new DataNotFound(String.format("Destination %s could not be found.", destinationName));
            }

            orderEntity.setDestination(destination.get());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            orderEntity.setDeliveryDate(LocalDate.parse(elements[1], formatter));

            orderEntity.setStatus(OrderStatus.NEW);

            orderEntity.setLastUpdated(currentDateAndProfitService.getCurrentDate());

            orders.add(orderEntity);
        }
        return orders;
    }
    public List<String> readInputFile(String inputFile) throws DataNotFound {

        List<String> inputLinesList = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line = reader.readLine();
            while(line != null) {
                inputLinesList.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new DataNotFound("The input file could not be located.");
        }

        return inputLinesList;
    }

}
