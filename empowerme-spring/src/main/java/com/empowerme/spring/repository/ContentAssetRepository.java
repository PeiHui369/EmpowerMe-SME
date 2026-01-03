package com.empowerme.spring.repository;

import com.empowerme.spring.domain.ContentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentAssetRepository extends JpaRepository<ContentAsset, Long> {
}
