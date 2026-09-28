package com.sharesphere.service;

import com.sharesphere.dto.response.FavoriteResponse;
import java.util.List;

public interface FavoriteService {
    FavoriteResponse addFavorite(Long itemId, Long userId);
    void removeFavorite(Long itemId, Long userId);
    List<FavoriteResponse> getMyFavorites(Long userId);
    boolean isFavorited(Long itemId, Long userId);
}
