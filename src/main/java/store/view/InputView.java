package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.purchase.OrderItem;
import store.domain.purchase.PurchaseOption;

public class InputView {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.*?)-(\\d+)\\]");
    private static final String ERROR_INVALID_INPUT = "잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_FORMAT = "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public List<OrderItem> readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        return parseOrder(input);
    }

    public PurchaseOption readPurchaseOption() {
        String input = Console.readLine();
        return PurchaseOption.from(input);
    }

    private List<OrderItem> parseOrder(String input) {
        validateInput(input);
        List<OrderItem> items = extractOrderItems(input);
        validateOrderItems(items);
        return items;
    }

    private void validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT);
        }
    }

    private List<OrderItem> extractOrderItems(String input) {
        Matcher matcher = ORDER_PATTERN.matcher(input);
        List<OrderItem> items = new ArrayList<>();

        while (matcher.find()) {
            items.add(createOrderItem(matcher));
        }

        return items;
    }

    private OrderItem createOrderItem(Matcher matcher) {
        String productName = matcher.group(1);
        int quantity = Integer.parseInt(matcher.group(2));
        return new OrderItem(productName, quantity);
    }

    private void validateOrderItems(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
        }
    }
}
