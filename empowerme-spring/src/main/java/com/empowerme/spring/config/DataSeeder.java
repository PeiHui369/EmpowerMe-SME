package com.empowerme.spring.config;

import com.empowerme.spring.domain.ContentAsset;
import com.empowerme.spring.domain.ContentStatus;
import com.empowerme.spring.domain.UserEntity;
import com.empowerme.spring.domain.UserRole;
import com.empowerme.spring.repository.ContentAssetRepository;
import com.empowerme.spring.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(ContentAssetRepository contentAssetRepository, UserRepository userRepository) {
        return args -> {
            if (contentAssetRepository.count() == 0) {
                System.out.println("Seeding Content Assets...");
                contentAssetRepository.save(ContentAsset.builder()
                        .title("Seed Asset 1 - Image")
                        .type("IMAGE")
                        .status(ContentStatus.LIVE)
                        .fileSize(1024L)
                        .fileExtension(".jpg")
                        .productionCost(50.0)
                        .build());

                contentAssetRepository.save(ContentAsset.builder()
                        .title("Seed Asset 2 - Video")
                        .type("VIDEO")
                        .status(ContentStatus.DRAFT)
                        .fileSize(2048000L)
                        .fileExtension(".mp4")
                        .productionCost(100.0)
                        .build());

                contentAssetRepository.save(ContentAsset.builder()
                        .title("Seed Asset 3 - Banner")
                        .type("IMAGE")
                        .status(ContentStatus.FLAGGED)
                        .fileSize(500L)
                        .fileExtension(".png")
                        .productionCost(50.0)
                        .build());
            }

            if (userRepository.count() == 0) {
                 System.out.println("Seeding Users...");
                 UserEntity entrepreneur = UserEntity.builder()
                         .name("Demo Entrepreneur")
                         .email("demo@empowerme.com")
                         .role(UserRole.ENTREPRENEUR)
                         .walletBalance(500.0)
                         .riskScore(10)
                         .build();
                 userRepository.save(entrepreneur);
            }
        };
    }
}
