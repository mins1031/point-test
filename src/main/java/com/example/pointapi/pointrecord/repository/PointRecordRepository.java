package com.example.pointapi.pointrecord.repository;

import com.example.pointapi.pointrecord.domain.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {
    List<PointRecord> findByUserNum(Long userNum);
}
