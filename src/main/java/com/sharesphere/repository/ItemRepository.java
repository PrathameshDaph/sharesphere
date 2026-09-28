package com.sharesphere.repository;

import com.sharesphere.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdAndStatusNot(Long ownerId, ItemStatus status);

    List<Item> findByOwnerId(Long ownerId);

    long countByCategoryId(Long categoryId);

    @Query("""
        SELECT i FROM Item i
        WHERE i.status = 'AVAILABLE'
        AND (:keyword IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:categoryId IS NULL OR i.category.id = :categoryId)
        AND (:listingType IS NULL OR i.listingType = :listingType)
        AND (:condition IS NULL OR i.condition = :condition)
        AND (:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%')))
        AND (:minPrice IS NULL OR i.price >= :minPrice)
        AND (:maxPrice IS NULL OR i.price <= :maxPrice)
        """)
    Page<Item> searchItems(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("listingType") ListingType listingType,
            @Param("condition") String condition,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    @Query("SELECT i FROM Item i WHERE i.status = 'AVAILABLE' ORDER BY i.viewCount DESC")
    List<Item> findPopularItems(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.status = 'AVAILABLE' ORDER BY i.createdAt DESC")
    List<Item> findLatestItems(Pageable pageable);

    List<Item> findByStatusAndCategory(ItemStatus status, Category category);

    long countByStatus(ItemStatus status);
    long countByOwnerId(Long ownerId);
}
