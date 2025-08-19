package com.ai.spring.settings;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * 1XX for general errors.
 * 3XX for authentication errors.
 * 4XX for account-related errors.
 */
@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),

    INVALID_REQUEST_PARAM(101, BAD_REQUEST, "Invalid request parameter value."),
    ENTITY_CONFLICT(102, CONFLICT, "Entity has already been added to your list."),
    ENTITY_NOT_FOUND(103, NOT_FOUND, "Entity Not Found."),
    INVALID_HEADERS(104, BAD_REQUEST, "Entity Not Found."),

    BAD_CREDENTIALS(301, UNAUTHORIZED, "Invalid credentials."),
    RATE_LIMIT_EXCEED(302, TOO_MANY_REQUESTS, "You have exceed the maximum number of allowed requests. Please try again later."),

    ACCOUNT_LOCKED(401, FORBIDDEN, "User account is locked."),
    ACCOUNT_DISABLED(402, FORBIDDEN, "User account is disabled."),
    INVALID_PASSWORD(403, UNAUTHORIZED, "Invalid username and / or password."),
    ACCESS_DENIED(404, FORBIDDEN, "You don't have the permission to perform this action."),
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}