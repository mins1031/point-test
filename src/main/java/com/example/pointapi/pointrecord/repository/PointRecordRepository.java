package com.example.pointapi.pointrecord.repository;

import com.example.pointapi.pointrecord.domain.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {
}
