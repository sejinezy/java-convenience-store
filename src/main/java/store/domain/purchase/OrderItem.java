package store.domain.purchase;

import store.util.ValidationUtils;

public record OrderItem(
        String productName,
        int quantity
) {
    public OrderItem {
        ValidationUtils.validatePositiveQuantity(quantity);
    }
}
