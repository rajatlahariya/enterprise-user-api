package com.rajat.user_api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationMeta {

	@Schema(description = "Current page number", example = "0")
	private int page;

	@Schema(description = "Page size", example = "10")
	private int size;

	@Schema(description = "Total number of records", example = "125")
	private long totalElements;

	@Schema(description = "Total number of pages", example = "13")
	private int totalPages;

	@Schema(description = "Whether this is the first page", example = "true")
	private boolean first;

	@Schema(description = "Whether this is the last page", example = "false")
	private boolean last;
}