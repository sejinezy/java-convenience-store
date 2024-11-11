package store.domain.purchase;

import java.util.Arrays;

public enum PurchaseOption {
    YES("Y"),
    NO("N");

    private static final String ERROR_INVALID_INPUT = "잘못된 입력입니다. 다시 입력해 주세요.";
    private final String value;

    PurchaseOption(String value) {
        this.value = value;
    }

    public static PurchaseOption from(String input) {
        return Arrays.stream(values())
                .filter(option -> option.value.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_INVALID_INPUT));
    }
}
