package com.sharesphere.repository;

import com.sharesphere.entity.Rental;
import com.sharesphere.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByRenterIdOrderByCreatedAtDesc(Long renterId);
    List<Rental> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    List<Rental> findByRenterIdAndStatus(Long renterId, RentalStatus status);
    List<Rental> findByOwnerIdAndStatus(Long ownerId, RentalStatus status);
    List<Rental> findByItemId(Long itemId);

    @Query("SELECT r FROM Rental r WHERE r.status = 'ACTIVE' AND r.endDate < :today")
    List<Rental> findOverdueRentals(@Param("today") LocalDate today);

    long countByStatus(RentalStatus status);
    long countByRenterId(Long renterId);
    long countByOwnerId(Long ownerId);
}
