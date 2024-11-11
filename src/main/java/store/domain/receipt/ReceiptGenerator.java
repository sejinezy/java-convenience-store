package store.domain.receipt;

import java.util.List;
import java.util.stream.Collectors;
import store.domain.product.ProductInventory;
import store.domain.purchase.PurchaseResult;

public class ReceiptGenerator {
    private static final int MEMBERSHIP_DISCOUNT_RATE = 30;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8_000;
    private static final String ERROR_INVALID_PRODUCT = "존재하지 않는 상품입니다. 다시 입력해 주세요.";

    public Receipt generate(
            List<PurchaseResult> purchaseResults,
            List<ProductInventory> inventory,
            boolean useMembership
    ) {
        List<ReceiptItem> purchaseItems = createPurchaseItems(purchaseResults, inventory);
        List<ReceiptItem> freeItems = createFreeItems(purchaseResults);

        int totalAmount = calculateTotalAmount(purchaseItems);
        int promotionDiscount = calculatePromotionDiscount(purchaseResults, inventory);
        int membershipDiscount = calculateMembershipDiscount(useMembership, purchaseResults, inventory);

        return new Receipt(purchaseItems, freeItems, totalAmount, promotionDiscount, membershipDiscount);
    }

    private List<ReceiptItem> createPurchaseItems(
            List<PurchaseResult> results,
            List<ProductInventory> inventory
    ) {
        return results.stream()
                .map(result -> new ReceiptItem(
                        result.productName(),
                        result.purchasedQuantity(),
                        getProductPrice(inventory, result.productName()) * result.purchasedQuantity()
                ))
                .collect(Collectors.toList());
    }

    private List<ReceiptItem> createFreeItems(List<PurchaseResult> results) {
        return results.stream()
                .filter(result -> result.freeItemCount() > 0)
                .map(result -> new ReceiptItem(
                        result.productName(),
                        result.freeItemCount(),
                        0
                ))
                .collect(Collectors.toList());
    }

    private int calculatePromotionDiscount(
            List<PurchaseResult> results,
            List<ProductInventory> inventory
    ) {
        return results.stream()
                .mapToInt(result ->
                        getProductPrice(inventory, result.productName()) * result.freeItemCount()
                )
                .sum();
    }

    private int calculateMembershipDiscount(
            boolean useMembership,
            List<PurchaseResult> results,
            List<ProductInventory> inventory
    ) {
        if (!useMembership) {
            return 0;
        }

        int nonPromotionAmount = calculateNonPromotionAmount(results, inventory);
        int discountAmount = nonPromotionAmount * MEMBERSHIP_DISCOUNT_RATE / 100;
        return Math.min(discountAmount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private int calculateNonPromotionAmount(
            List<PurchaseResult> results,
            List<ProductInventory> inventory
    ) {
        return results.stream()
                .filter(result -> !isPromotionApplied(result))
                .mapToInt(result -> {
                    ProductInventory product = findProduct(inventory, result.productName());
                    return product.getPrice() * result.purchasedQuantity();
                })
                .sum();
    }

    private boolean isPromotionApplied(PurchaseResult result) {
        return result.promotionAppliedQuantity() > 0;
    }

    private int calculateTotalAmount(List<ReceiptItem> purchaseItems) {
        return purchaseItems.stream()
                .mapToInt(ReceiptItem::amount)
                .sum();
    }

    private int getProductPrice(List<ProductInventory> inventory, String productName) {
        return findProduct(inventory, productName).getPrice();
    }

    private ProductInventory findProduct(List<ProductInventory> inventory, String productName) {
        return inventory.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(ERROR_INVALID_PRODUCT));
    }
}

