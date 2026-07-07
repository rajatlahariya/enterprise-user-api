package com.rajat.user_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rajat.user_api.dto.request.UserPatchRequest;
import com.rajat.user_api.dto.request.UserRequest;
import com.rajat.user_api.dto.request.UserUpdateRequest;
import com.rajat.user_api.dto.response.UserResponse;
import com.rajat.user_api.response.ApiResponseBuilder;
import com.rajat.user_api.response.ApiSuccessResponse;
import com.rajat.user_api.response.PagedResponse;
import com.rajat.user_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for user CRUD, filtering, pagination, sorting and soft delete")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user and returns 201 Created.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "success": true,
                              "message": "User created successfully",
                              "data": {
                                "id": 101,
                                "firstName": "Aarav",
                                "lastName": "Sharma",
                                "email": "demo.user@example.test",
                                "age": 29,
                                "isActive": true,
                                "createdAt": "2026-07-05T18:00:00"
                              }
                            }
                            """))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "success": false,
                              "message": "Validation failed",
                              "errors": {
                                "email": "Invalid email",
                                "age": "Age should be at least 18"
                              }
                            }
                            """)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details required to create a new user",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                    {
                      "firstName": "Aarav",
                      "lastName": "Sharma",
                      "email": "demo.user@example.test",
                      "age": 29,
                      "isActive": true
                    }
                    """)))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse savedUser = userService.saveUser(request);
        URI location = URI.create("/users/" + savedUser.getId());

        return ResponseEntity.created(location)
                .body(ApiResponseBuilder.success("User created successfully", savedUser));
    }

    @Operation(summary = "Get users", description = "Returns paginated users with filtering, pagination and sorting. Supports firstName, username, email, role, isActive, minAge, maxAge, page, size and sort.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users fetched successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "success": true,
                              "message": "Users fetched successfully",
                              "data": [
                                {
                                  "id": 1,
                                  "firstName": "Aarav",
                                  "lastName": "Sharma",
                                  "email": "demo.user@example.test",
                                  "age": 29,
                                  "isActive": true,
                                  "createdAt": "2026-07-05T18:00:00"
                                }
                              ],
                              "pagination": {
                                "page": 0,
                                "size": 10,
                                "totalElements": 100,
                                "totalPages": 10,
                                "first": true,
                                "last": false
                              }
                            }
                            """)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<PagedResponse<List<UserResponse>>> getUsers(
            @Parameter(description = "Filter by first name", example = "raj")
            @RequestParam(required = false) String firstName,

            @Parameter(description = "Filter by username", example = "seed.aarav")
            @RequestParam(required = false) String username,

            @Parameter(description = "Filter by email", example = "enterprise.test")
            @RequestParam(required = false) String email,

            @Parameter(description = "Filter by role", example = "ROLE_MANAGER")
            @RequestParam(required = false) String role,

            @Parameter(description = "Filter by active status", example = "true")
            @RequestParam(required = false) Boolean isActive,

            @Parameter(description = "Minimum age", example = "25")
            @RequestParam(required = false) Integer minAge,

            @Parameter(description = "Maximum age", example = "35")
            @RequestParam(required = false) Integer maxAge,

            @Parameter(description = "Pagination and sorting. Example: page=0&size=10&sort=age,desc")
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<UserResponse> usersPage = userService.searchUsers(
                firstName,
                username,
                email,
                role,
                isActive,
                minAge,
                maxAge,
                pageable
        );

        return ResponseEntity.ok(ApiResponseBuilder.paged("Users fetched successfully", usersPage));
    }

    @Operation(summary = "Get user by ID", description = "Returns a single active user by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "success": false,
                              "message": "User not found with id: 100"
                            }
                            """)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success("User fetched successfully", userService.getUserById(id))
        );
    }

    @Operation(summary = "Search user by email", description = "Returns a single user matching the email address.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> getUserByEmail(
            @Parameter(description = "User email", example = "demo.user@example.test", required = true)
            @RequestParam String email) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success("User fetched successfully", userService.getUserByEmail(email))
        );
    }

    @Operation(summary = "Update user completely", description = "Updates all updatable fields. All required fields must be provided.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Complete user details required to update a user",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                    {
                      "firstName": "Aarav",
                      "lastName": "Sharma",
                      "email": "aarav.sharma.updated@example.test",
                      "age": 30,
                      "isActive": true
                    }
                    """)))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success("User updated successfully", userService.updateUser(id, request))
        );
    }

    @Operation(summary = "Partially update user", description = "Updates only the fields provided in the request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Partial user details to update",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                    {
                      "email": "aarav.sharma.patch@example.test"
                    }
                    """)))
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> patchUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @RequestBody UserPatchRequest request) {

        return ResponseEntity.ok(
                ApiResponseBuilder.success("User partially updated successfully", userService.patchUser(id, request))
        );
    }

    @Operation(summary = "Soft delete user", description = "Marks the user as inactive instead of deleting the DB record.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User soft deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}