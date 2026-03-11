package com.sprint.mission.discodeit.global.dto;

import org.springframework.data.domain.Pageable;

public record MyPageRequest<T>(
    T t,
    Pageable pageable,
    Object currentCursor
) {

}
