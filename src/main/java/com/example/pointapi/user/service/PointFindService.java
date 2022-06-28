package com.example.pointapi.user.service;

import com.example.pointapi.common.exception.WrongRequesterException;
import com.example.pointapi.common.validator.ParameterValidator;
import com.example.pointapi.pointrecord.domain.PointRecord;
import com.example.pointapi.pointrecord.domain.WrongPointRecord;
import com.example.pointapi.pointrecord.repository.PointRecordRepository;
import com.example.pointapi.pointrecord.repository.WrongPointRecordRepository;
import com.example.pointapi.user.domain.User;
import com.example.pointapi.user.dto.PointFindResponse;
import com.example.pointapi.user.exception.NotFoundUserException;
import com.example.pointapi.user.exception.NotMatchPresentPoint;
import com.example.pointapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointFindService {
    private final UserRepository userRepository;
    private final PointRecordRepository pointRecordRepository;
    private final WrongPointRecordRepository wrongPointRecordRepository;

    @Transactional(readOnly = true)
    public PointFindResponse findUserPoint(String userId) {
        ParameterValidator.checkUuidParameter(userId);
        User user = userRepository.findByUuidIdentifier(userId).orElseThrow(NotFoundUserException::new);
        List<PointRecord> pointRecords = pointRecordRepository.findByUserNum(user.getNum());
        int presetPointInRecord = pointRecords.stream()
                .mapToInt(PointRecord::getUpdatedPoint)
                .sum();
        int presentPointInUser = user.getPoint().getPresentPoint();

        if (presentPointInUser != presetPointInRecord) {
            wrongPointRecordRepository.save(WrongPointRecord.createWrongPointRecord(presentPointInUser, presetPointInRecord));
            throw new NotMatchPresentPoint();
        }

        return new PointFindResponse(presetPointInRecord);
    }
}
