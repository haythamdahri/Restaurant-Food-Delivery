package org.restaurant.salado.models;

/**
 * @author Haytham DAHRI
 */
public enum MessageType {
    INVALID_TOKEN("Token does not belong to any user!", false, ""),
    EXPIRED_TOKEN("Token is expired!", false, ""),
    EMAIL_NOT_FOUND("Email does not belong to any user or email not enabled yet!", false, ""),
    EMAIL_UPDATED("Email has been updated successfully, please confirm your new email via received mail", true, ""),
    PASSWORD_RESET_EMAIL_SENT("Password reset email has been sent to your email successfully", true, ""),
    ACCOUNT_NOT_ENABLED("Account not enabled yet!", false, ""),
    ACCOUNT_ALREADY_ENABLED("Account already enabled", false, ""),
    ACCOUNT_ENABLED_SUCCESSFULLY("Account is enabled successfully", true, ""),
    PASSWORD_CHANGED_SUCCESSFULLY("Password has been updated successfully", true, ""),
    VALID_TOKEN("Token is valid!", true, ""),
    ERROR("An error occurred, please try again:", false, ""),
    INVALID_USER_IMAGE("Invalid user image!", false, ""),
    USER_IMAGE_UPDATED_SUCCESSFULLY("User image has been changed successfully", true, ""),
    MEAL_ADDED_TO_PREFERENCES("Meal has been added to your preferences successfully", true, true),
    MEAL_REMOVED_FROM_PREFERENCES("Meal has been removed from your preferences successfully", true, false);


    private final String message;
    private final boolean status;
    private final Object extra;

    MessageType(String message, boolean status, Object extra) {
        this.status = status;
        this.message = message;
        this.extra = extra;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean getStatus() {
        return this.status;
    }

    public Object getExtra() {
        return this.extra;
    }
}