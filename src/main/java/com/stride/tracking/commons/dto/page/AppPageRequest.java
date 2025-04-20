package com.stride.tracking.commons.dto.page;

import com.stride.tracking.commons.constants.Message;
import com.stride.tracking.commons.constants.StrideConstants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppPageRequest {
	@Min(
			value = 1,
			message = Message.PAGE_VALIDATE
	)
	private int page = StrideConstants.DEFAULT_PAGE;

	@Min(
			value = 1,
			message = Message.PAGE_LIMIT
	)
	private int limit = StrideConstants.DEFAULT_LIMIT;
}
