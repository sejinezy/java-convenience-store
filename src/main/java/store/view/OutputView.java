package store.view;

import java.util.List;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptItem;

public class OutputView {
    public void printWelcome() {
        System.out.println("\n안녕하세요. W편의점입니다.");
    }

    public void printProductList(List<String> formattedProducts) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        formattedProducts.forEach(System.out::println);
        System.out.println();
    }

    public void printOrderPrompt() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    }

    public void printPromotionSuggestion(String productName, int freeCount) {
        System.out.printf("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n",
                productName, freeCount);
    }

    public void printNormalPriceNotification(String productName, int quantity) {
        System.out.printf("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n",
                productName, quantity);
    }

    public void printReceipt(Receipt receipt) {
        System.out.println("\n==============W 편의점================");
        printPurchaseItems(receipt.getPurchaseItems());
        printFreeItemsIfPresent(receipt.getFreeItems());
        System.out.println("====================================");
        printAmountDetails(receipt);
    }

    private void printPurchaseItems(List<ReceiptItem> items) {
        System.out.println("상품명\t\t수량\t금액");
        for (ReceiptItem item : items) {
            String name = item.productName();
            name += "\t";

            System.out.printf("%s\t%d \t%,d\n",
                    name,
                    item.quantity(),
                    item.amount());
        }
    }

    private void printFreeItemsIfPresent(List<ReceiptItem> freeItems) {
        if (freeItems.isEmpty()) {
            return;
        }

        System.out.println("=============증\t정===============");
        freeItems.forEach(item -> System.out.printf("%s\t\t%d\n",
                item.productName(),
                item.quantity()));
    }

    private void printAmountDetails(Receipt receipt) {
        System.out.printf("총구매액\t\t%d\t%,d\n",
                getTotalQuantity(receipt.getPurchaseItems()),
                receipt.getTotalAmount());

        printPromotionDiscount(receipt);
        printMembershipDiscount(receipt);
        printFinalAmount(receipt);
    }

    private void printPromotionDiscount(Receipt receipt) {
        if (receipt.getPromotionDiscount() > 0) {
            System.out.printf("행사할인\t\t\t-%,d\n", receipt.getPromotionDiscount());
        }
    }

    private void printMembershipDiscount(Receipt receipt) {
        System.out.printf("멤버십할인\t\t\t-%,d\n", receipt.getMembershipDiscount());
    }

    private void printFinalAmount(Receipt receipt) {
        System.out.printf("내실돈\t\t\t %,d\n", receipt.getFinalAmount());
    }

    private int getTotalQuantity(List<ReceiptItem> items) {
        return items.stream()
                .mapToInt(ReceiptItem::quantity)
                .sum();
    }

}
