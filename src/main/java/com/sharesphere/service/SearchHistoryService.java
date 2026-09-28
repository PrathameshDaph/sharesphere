package com.sharesphere.service;

import java.util.List;

public interface SearchHistoryService {
    void record(Long userId, String query);
    List<String> getRecentQueries(Long userId);
    void clearHistory(Long userId);
}
