package store.domain.promotion;

import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PromotionTest {

    @Test
    void 프로모션_타입이_널일_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> new Promotion(
                null,
                2,
                1,
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 12, 1)
        ));
    }

    @Test
    void 프로모션_구매수량이_0보다_작을_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> new Promotion(
                PromotionType.CARBONATED_TWO_PLUS_ONE,
                -1,
                1,
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 12, 1)
        ));
    }

    @Test
    void 프로모션_증정수량이_0보다_작을_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> new Promotion(
                PromotionType.CARBONATED_TWO_PLUS_ONE,
                2,
                0,
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 12, 1)
        ));
    }

    @Test
    void 프로모션_종료일이_시작일_이전일_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> new Promotion(
                PromotionType.CARBONATED_TWO_PLUS_ONE,
                2,
                1,
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 11, 1)
        ));
    }
}

