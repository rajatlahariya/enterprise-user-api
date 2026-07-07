package com.rajat.user_api.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	@Schema(
		    description = "Unique identifier of the user",
		    example = "1",
		    accessMode = Schema.AccessMode.READ_ONLY
		)
		private Long id;
	
	@Schema(
		    description = "User first name",
		    example = "Aarav"
		)
		private String firstName;    
	
	@Schema(
		    description = "User last name",
		    example = "Sharma"
		)
       private String lastName;
	@Schema(
		    description = "User email address",
		    example = "demo.user@example.test"
		)
		private String email;
	@Schema(
		    description = "User age",
		    example = "27"
		)
    private Integer age;
	
    private Boolean isActive;
    @Schema(
    	    description = "Record creation timestamp",
    	    example = "2026-07-05T18:15:30",
    	    accessMode = Schema.AccessMode.READ_ONLY
    	)
    	private LocalDateTime createdAt;
}