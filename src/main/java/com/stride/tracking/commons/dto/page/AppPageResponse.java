package com.stride.tracking.commons.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppPageResponse {
	private int index;
	private int limit;
	private long totalElements;
	private long totalPages;
}
