package com.nexusprocure.common.Response;

import org.springframework.data.domain.Page;

public final class PageResponseMapper {
    private PageResponseMapper(){}
    public static <T> PageResponse<T> toResponse(Page<T> page){
        return new PageResponse<>(page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }


}
