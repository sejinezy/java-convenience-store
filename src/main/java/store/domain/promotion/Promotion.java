package store.domain.promotion;

import java.time.LocalDate;

public class Promotion {
    private static final String ERROR_NULL_TYPE = "프로모션 타입은 필수입니다.";
    private static final String ERROR_INVALID_COUNT = "수량은 0보다 커야 합니다.";
    private static final String ERROR_NULL_DATE = "날짜는 필수입니다.";
    private static final String ERROR_INVALID_END_DATE = "종료일은 시작일 이후여야 합니다.";

    private final PromotionType type;
    private final int buyCount;
    private final int freeCount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(
            final PromotionType type,
            final int buyCount,
            final int freeCount,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        this.type = validateNotNull(type, ERROR_NULL_TYPE);
        this.buyCount = validatePositive(buyCount, ERROR_INVALID_COUNT);
        this.freeCount = validatePositive(freeCount, ERROR_INVALID_COUNT);
        this.startDate = validateNotNull(startDate, ERROR_NULL_DATE);
        this.endDate = validateEndDate(startDate, endDate);
    }

    private <T> T validateNotNull(final T value, final String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    private int validatePositive(final int value, final String errorMessage) {
        if (value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    private LocalDate validateEndDate(final LocalDate startDate, final LocalDate endDate) {
        validateNotNull(endDate, ERROR_NULL_DATE);
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(ERROR_INVALID_END_DATE);
        }
        return endDate;
    }

    public PromotionType getType() {
        return type;
    }
}
