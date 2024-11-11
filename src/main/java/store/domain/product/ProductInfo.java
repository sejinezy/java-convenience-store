package store.domain.product;

public record ProductInfo(
        String name,
        int price,
        int quantity,
        String promotion
) {}

