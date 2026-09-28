package com.sharesphere.repository;

import com.sharesphere.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT s.query FROM SearchHistory s WHERE s.user.id = :userId ORDER BY s.createdAt DESC")
    List<String> findRecentQueriesByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);
}
