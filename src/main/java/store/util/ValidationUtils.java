package store.util;

public class ValidationUtils {

    public static void validatePositiveQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_QUANTITY);
        }
    }
}
