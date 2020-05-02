package org.restaurant.salado.providers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public class Constants {

    public static final BigDecimal SHIPPING_FEES = BigDecimal.valueOf(50.00);
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif");
    public static final String INVALID_TOKEN = "Token does not belong to any user!";
    public static final String EXPIRED_TOKEN = "Token is expired!";
    public static final String EMAIL_NOT_FOUND = "Email does not belong to any user or email not enabled yet!";
    public static final String EMAIL_UPDATED = "Email has been updated successfully, please confirm your new email via received mail";
    public static final String EMAIL_ALREADY_USED = "Email Address is already used!";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset email has been sent to your email successfully";
    public static final String ACCOUNT_NOT_ENABLED = "Account not enabled yet!";
    public static final String ACCOUNT_ALREADY_ENABLED = "Account already enabled";
    public static final String ACCOUNT_ENABLED_SUCCESSFULLY = "Account is enabled successfully";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password has been updated successfully";
    public static final String VALID_TOKEN = "Token is valid!";
    public static final String ERROR = "An error occurred, please try again:";
    public static final String INVALID_USER_IMAGE = "Invalid user image!";
    public static final String USER_IMAGE_UPDATED_SUCCESSFULLY = "User image has been changed successfully";
    public static final String MEAL_ADDED_TO_PREFERENCES = "Meal has been added to your preferences successfully";
    public static final String MEAL_REMOVED_FROM_PREFERENCES = "Meal has been removed from your preferences successfully";
    public static final String MEAL_ADDED_TO_CART_SUCCESSFULLY = "Meal has been added to your cart successfully";
    public static final String MEAL_EXISTS_IN_CART = "Meal already exists in your cart!";
    public static final String PRODUCT_OUT_OF_STOCK = "Product out of stock!";
    public static final String PRODUCT_STOCK_INSUFFICIENT = "One or more products stock is insufficient!";
    public static final String PRODUCTS_DELETED = "One or more products does not exist any more!";
    public static final String PRODUCT_DELETED = "Product does not exists any more!";
    public static final String UNAVAILABLE_PRODUCT_STOCK = "Unavailable quantity in the stock, only AVAILABLE_STOCK is available";
    public static final String MEAL_ORDER_QUANTITY_UPDATED_SUCCESSFULLY = "Meal order has been removed from your cart successfully";
    public static final String NO_ORDER_IN_PROGRESS = "No order in place, please add products to your cart!";


    public static final String PDF_OUTPUT_DIRECTORY = "target/payment-ID.pdf";

    private Constants() {

    }

}
