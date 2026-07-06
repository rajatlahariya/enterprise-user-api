package com.rajat.user_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {
	
	@Schema(
		    description = "Updated first name",
		    example = "Rahul"
		)
		private String firstName;
   

    private String lastName;

    private String email;

    private Integer age;

    private Boolean isActive;
}