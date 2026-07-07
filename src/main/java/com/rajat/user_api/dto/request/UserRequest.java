package com.rajat.user_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

	@Schema(description = "User first name", example = "Aarav", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "First Name is required")
	private String firstName;

	@Schema(description = "User last name", example = "Sharma", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Last Name is required")
	private String lastName;

	@Schema(description = "User email address", example = "demo.user@example.test", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email")
	private String email;

	@Schema(description = "User age. Must be at least 18.", example = "29", minimum = "18", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "Age is required")
	@Min(value = 18, message = "Age should be at least 18")
	private Integer age;

	@Schema(description = "Whether user is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "Status is required")
	private Boolean isActive;
}