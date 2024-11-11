package store.domain.purchase;

public record PurchaseResult(
        String productName,
        int purchasedQuantity,
        int freeItemCount,
        int promotionAppliedQuantity
) {}
