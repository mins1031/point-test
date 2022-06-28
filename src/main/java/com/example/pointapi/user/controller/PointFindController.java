package com.example.pointapi.user.controller;

import com.example.pointapi.user.dto.PointFindResponse;
import com.example.pointapi.user.service.PointFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointFindController {
    private final PointFindService pointFindService;

    @GetMapping(PointFindControllerPath.POINT_FIND)
    public ResponseEntity<PointFindResponse> findUserPoint(@PathVariable String userId) {
        PointFindResponse pointFindResponse = pointFindService.findUserPoint(userId);
        return new ResponseEntity<PointFindResponse>(pointFindResponse, HttpStatus.OK);
    }

}
