package com.example.project.repository;
import com.example.project.entity.OrderEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    @Modifying
    @Query(value = "UPDATE orders o SET o.lastUpdated = :currentDate WHERE o.id = :id")
    void updateLastUpdated(@Param("currentDate") LocalDate currentDate,
                           @Param("id") Long id);

    @Query(value = "SELECT o FROM orders o WHERE o.deliveryDate = :deliveryDate")
    List<OrderEntity> findAllByDeliveryDate(@Param("deliveryDate") LocalDate deliveryDate);

}
