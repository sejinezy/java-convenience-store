package store.view;

import java.util.List;

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

}
