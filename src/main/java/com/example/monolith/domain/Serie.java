package com.example.monolith.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import com.example.monolith.domain.enums.StreamingPlatformsEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "series")
public class Serie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "{serie.title.notBlank}")
    @Size(min = 1, max = 100, message = "{serie.title.size}")
    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{serie.platform.notNull}")
    @Column(name = "streaming_platform", nullable = false, length = 50)
    private StreamingPlatformsEnum streamingPlatform;

    @Size(max = 1000, message = "{serie.synopsis.size}")
    @Column(columnDefinition = "TEXT", length = 1000)
    private String synopsis;

    @URL(message = "{serie.coverImage.url}")
    @Size(max = 255, message = "{serie.coverImage.size}")
    @Column(name = "cover_image_url", length = 255)
    private String coverImageUrl;
    
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rating> ratings = new HashSet<>();
}
