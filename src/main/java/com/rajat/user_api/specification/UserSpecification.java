package com.rajat.user_api.specification;

import org.springframework.data.jpa.domain.Specification;

import com.rajat.user_api.entity.User;

public class UserSpecification {

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> hasActiveStatus(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<User> ageGreaterThanOrEqual(Integer minAge) {
        return (root, query, criteriaBuilder) -> {
            if (minAge == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge);
        };
    }

    public static Specification<User> ageLessThanOrEqual(Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            if (maxAge == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge);
        };
    }
}