package com.sprint.mission.discodeit.common.dto;

import org.springframework.data.domain.Pageable;

public record MyPageRequest<T>(
    T t,
    Pageable pageable,
    Object currentCursor
) {

}
