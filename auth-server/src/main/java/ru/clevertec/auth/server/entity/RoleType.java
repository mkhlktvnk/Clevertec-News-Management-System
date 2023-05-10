package ru.clevertec.auth.server.entity;

/**
 * This enum represents the different types of roles that can be assigned to a user in the system.
 * The available roles are: subscriber, journalist, and admin.
 */
public enum RoleType {

    /**
     * Represents a subscriber role, which grants the user access to read news articles.
     */
    ROLE_SUBSCRIBER,

    /**
     * Represents a journalist role, which grants the user access to create and edit news articles.
     */
    ROLE_JOURNALIST,

    /**
     * Represents an admin role, which grants the user full access to the system.
     */
    ROLE_ADMIN

}

