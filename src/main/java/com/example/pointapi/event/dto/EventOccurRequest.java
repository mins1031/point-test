package com.example.pointapi.event.dto;

import com.example.pointapi.common.validatemessages.RequestValidatorMessages;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventOccurRequest {
    @NotNull(message = RequestValidatorMessages.EVENT_TYPE_NULL)
    private String eventType;
    @NotNull(message = RequestValidatorMessages.EVENT_REVIEW_ACTION_NULL)
    private String reviewAction;
    @NotBlank(message = RequestValidatorMessages.EVENT_REVIEW_ID_BLANK)
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    @NotBlank(message = RequestValidatorMessages.EVENT_USER_ID_NULL)
    private String userId;
    @NotBlank(message = RequestValidatorMessages.EVENT_PLACE_ID_NULL)
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

    public boolean checkExistContent() {
        if (this.content == null) {
            return false;
        }

        return !this.content.isBlank();
    }

    public boolean checkExistPhotos() {
        if (this.attachedPhotoIds == null) {
            return false;
        }

        return this.attachedPhotoIds.size() >= 1;
    }

}
