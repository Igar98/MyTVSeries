package com.example.monolith.bootstrap;

import com.example.monolith.domain.AppUser;
import com.example.monolith.domain.Rating;
import com.example.monolith.domain.Serie;
import com.example.monolith.domain.enums.StreamingPlatformsEnum;
import com.example.monolith.repositories.RatingRepository;
import com.example.monolith.repositories.SeriesRepository;
import com.example.monolith.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;
    private final RatingRepository ratingRepository;

    private boolean dataLoaded = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (dataLoaded) {
            log.info("Dev data already loaded");
            return;
        }

        log.info("Starting dev data initialization...");

        loadUsers();
        loadSeries();
        createRatings();
        updateSeriesAverageRatings();

        dataLoaded = true;
        log.info("Dev data initialization completed");
    }

    private void loadUsers() {
        log.debug("Loading users...");

        List<AppUser> users = Arrays.asList(
                createUser("john_doe"),
                createUser("jane_smith"),
                createUser("bob_wilson"));

        userRepository.saveAll(users);
        log.debug("Loaded {} users", users.size());
    }

    private AppUser createUser(String username) {
        AppUser user = new AppUser();
        user.setUsername(username);
        return user;
    }

    private void loadSeries() {
        log.debug("Loading series...");

        List<Serie> series = Arrays.asList(
                createSerie(
                        "Breaking Bad",
                        StreamingPlatformsEnum.NETFLIX,
                        "A high school chemistry teacher turned methamphetamine manufacturer partners with a former student to secure his family's financial future as he battles terminal lung cancer.",
                        "https://example.com/breaking-bad-cover.jpg"),
                createSerie(
                        "The Mandalorian",
                        StreamingPlatformsEnum.DISNEY_PLUS,
                        "A lone bounty hunter travels the outer reaches of the galaxy, protecting a mysterious alien child.",
                        "https://example.com/mandalorian-cover.jpg"),
                createSerie(
                        "The Boys",
                        StreamingPlatformsEnum.AMAZON_PRIME,
                        "A group of vigilantes takes on corrupt superheroes who abuse their powers.",
                        "https://example.com/the-boys-cover.jpg"));

        seriesRepository.saveAll(series);
        log.debug("Loaded {} series", series.size());
    }

    private Serie createSerie(String title, StreamingPlatformsEnum platform, String synopsis, String coverUrl) {
        Serie serie = new Serie();
        serie.setTitle(title);
        serie.setStreamingPlatform(platform);
        serie.setSynopsis(synopsis);
        serie.setCoverImageUrl(coverUrl);
        return serie;
    }

    private void createRatings() {
        log.debug("Creating ratings...");

        List<AppUser> users = userRepository.findAll();
        List<Serie> series = seriesRepository.findAll();

        users.forEach(user -> series.forEach(serie -> createRating(user, serie, generateRandomRating())));

        log.debug("Created ratings for {} users and {} series", users.size(), series.size());
    }

    private void createRating(AppUser user, Serie serie, BigDecimal rating) {
        Rating newRating = new Rating();
        newRating.setUser(user);
        newRating.setSerie(serie);
        newRating.setSeriesRating(rating);
        newRating.setCreatedAt(LocalDateTime.now());

        ratingRepository.save(newRating);

        // Update user and serie relationships
        user.getRatings().add(newRating);
        serie.getRatings().add(newRating);
    }

    private BigDecimal generateRandomRating() {
        // Generate random rating between 0.0 and 10.0 with one decimal place
        return BigDecimal.valueOf(Math.round(Math.random() * 100) / 10.0);
    }

    private void updateSeriesAverageRatings() {
        log.debug("Updating series average ratings...");

        List<Serie> series = seriesRepository.findAll();
        series.forEach(this::updateSerieAverageRating);

        seriesRepository.saveAll(series);
        log.debug("Updated average ratings for {} series", series.size());
    }

    private void updateSerieAverageRating(Serie serie) {
        Set<Rating> ratings = serie.getRatings();
        if (ratings.isEmpty()) {
            serie.setAvgRating(BigDecimal.ZERO);
            return;
        }

        BigDecimal sum = ratings.stream()
                .map(Rating::getSeriesRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 1, RoundingMode.HALF_UP);
        serie.setAvgRating(average);
    }
}