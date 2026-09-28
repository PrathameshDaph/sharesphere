package com.sharesphere.repository;

import com.sharesphere.entity.Review;
import com.sharesphere.entity.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByItemIdOrderByCreatedAtDesc(Long itemId);
    List<Review> findByRevieweeIdOrderByCreatedAtDesc(Long revieweeId);
    List<Review> findByReviewerIdOrderByCreatedAtDesc(Long reviewerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :userId")
    Double findAverageRatingByUser(@Param("userId") Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.item.id = :itemId")
    Double findAverageRatingByItem(@Param("itemId") Long itemId);

    boolean existsByReviewerIdAndItemId(Long reviewerId, Long itemId);
}
