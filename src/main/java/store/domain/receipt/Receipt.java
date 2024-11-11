package store.domain.receipt;

import java.util.List;

public class Receipt {
    private final List<ReceiptItem> purchaseItems;
    private final List<ReceiptItem> freeItems;
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public Receipt(
            List<ReceiptItem> purchaseItems,
            List<ReceiptItem> freeItems,
            int totalAmount,
            int promotionDiscount,
            int membershipDiscount
    ) {
        this.purchaseItems = purchaseItems;
        this.freeItems = freeItems;
        this.totalAmount = totalAmount;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }

    public List<ReceiptItem> getPurchaseItems() {
        return purchaseItems;
    }

    public List<ReceiptItem> getFreeItems() {
        return freeItems;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}