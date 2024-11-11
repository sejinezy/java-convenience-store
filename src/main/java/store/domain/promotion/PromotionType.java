package store.domain.promotion;

import java.util.Arrays;

public enum PromotionType {
    CARBONATED_TWO_PLUS_ONE("탄산2+1"),
    MD_RECOMMENDED("MD추천상품"),
    FLASH_SALE("반짝할인"),
    NONE("null");

    private static final String ERROR_INVALID_PROMOTION = "유효하지 않은 프로모션 타입입니다.";

    private final String name;

    PromotionType(final String name) {
        this.name = name;
    }

    public static PromotionType fromName(final String name) {
        validateName(name);
        return findPromotionType(name);
    }

    private static void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ERROR_INVALID_PROMOTION);
        }
    }

    private static PromotionType findPromotionType(final String name) {
        return Arrays.stream(values())
                .filter(type -> type.isMatchingType(name))
                .findFirst()
                .orElse(NONE);
    }

    private boolean isMatchingType(final String inputName) {
        return name.equals(inputName);
    }
}
