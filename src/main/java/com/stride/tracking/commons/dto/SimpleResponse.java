package com.stride.tracking.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleResponse {
    private boolean data;

    public SimpleResponse() {
        data = true;
    }
}
