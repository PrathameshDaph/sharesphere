package com.sharesphere.service.impl;

import com.sharesphere.dto.response.FavoriteResponse;
import com.sharesphere.dto.response.ItemResponse;
import com.sharesphere.entity.*;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.repository.*;
import com.sharesphere.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemServiceImpl itemService;

    @Override
    public FavoriteResponse addFavorite(Long itemId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        if (favoriteRepository.existsByUserIdAndItemId(userId, itemId)) {
            return favoriteRepository.findByUserIdAndItemId(userId, itemId)
                    .map(f -> toResponse(f, userId)).orElseThrow();
        }
        Favorite fav = Favorite.builder().user(user).item(item).build();
        return toResponse(favoriteRepository.save(fav), userId);
    }

    @Override
    @Transactional
    public void removeFavorite(Long itemId, Long userId) {
        favoriteRepository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<FavoriteResponse> getMyFavorites(Long userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(f -> toResponse(f, userId)).toList();
    }

    @Override
    public boolean isFavorited(Long itemId, Long userId) {
        return favoriteRepository.existsByUserIdAndItemId(userId, itemId);
    }

    private FavoriteResponse toResponse(Favorite f, Long userId) {
        return FavoriteResponse.builder()
                .id(f.getId())
                .item(itemService.toResponse(f.getItem(), userId))
                .createdAt(f.getCreatedAt()).build();
    }
}
