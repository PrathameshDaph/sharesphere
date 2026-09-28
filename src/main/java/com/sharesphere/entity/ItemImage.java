package com.sharesphere.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Item item;

    @Column(nullable = false)
    private String imageUrl;

    @Builder.Default
    @Column(name = "is_primary")
    private boolean primary = false;
}
