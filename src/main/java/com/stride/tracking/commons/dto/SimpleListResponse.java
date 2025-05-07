package com.stride.tracking.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stride.tracking.commons.dto.page.AppPageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleListResponse<T> {
	@JsonProperty("data")
	private List<T> data;
}
