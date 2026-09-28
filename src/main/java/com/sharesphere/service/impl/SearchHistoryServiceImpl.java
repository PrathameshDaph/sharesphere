package com.sharesphere.service.impl;

import com.sharesphere.entity.SearchHistory;
import com.sharesphere.entity.User;
import com.sharesphere.repository.SearchHistoryRepository;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public void record(Long userId, String query) {
        if (query == null || query.isBlank()) return;
        userRepository.findById(userId).ifPresent(user -> {
            SearchHistory sh = SearchHistory.builder().user(user).query(query.trim()).build();
            searchHistoryRepository.save(sh);
        });
    }

    @Override
    public List<String> getRecentQueries(Long userId) {
        return searchHistoryRepository.findRecentQueriesByUserId(userId).stream().limit(10).toList();
    }

    @Override
    @Transactional
    public void clearHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }
}
