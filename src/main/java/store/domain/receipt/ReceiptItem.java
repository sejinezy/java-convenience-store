package store.domain.receipt;

public record ReceiptItem(
        String productName,
        int quantity,
        int amount
) {}
