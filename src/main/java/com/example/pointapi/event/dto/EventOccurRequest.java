package com.example.pointapi.event.dto;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventOccurRequest {
    @NotNull
    private String eventType;
    @NotNull
    private String reviewAction;
    @NotBlank
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    @NotBlank
    private String userId;
    private String placeId;

    public EventOccurRequest(String eventType, String reviewAction, String reviewId, String content, List<String> attachedPhotoIds, String userId, String placeId) {
        this.eventType = eventType;
        this.reviewAction = reviewAction;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }
}
