package store.domain.promotion;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PromotionTypeTest {
    @Test
    void 프로모션_타입_변환_성공() {
        assertEquals(PromotionType.CARBONATED_TWO_PLUS_ONE, PromotionType.fromName("탄산2+1"));
    }

    @Test
    void 프로모션_타입이_null일_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> PromotionType.fromName(null));
    }

    @Test
    void 프로모션_타입이_빈문자열일_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> PromotionType.fromName(" "));
    }
}

