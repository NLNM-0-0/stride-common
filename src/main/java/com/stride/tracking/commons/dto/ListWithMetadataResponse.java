package com.stride.tracking.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListWithMetadataResponse<T, F, M> {
	@JsonProperty("data")
	private List<T> data;

	@JsonProperty("filter")
	private F filter;

	@JsonProperty("metadata")
	private M metadata;
}
