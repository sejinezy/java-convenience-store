package store.domain.product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {
    @Test
    void 재고_생성_성공() {
        Stock stock = new Stock(10, 20);
        assertEquals(10, stock.getNormalStock());
        assertEquals(20, stock.getPromotionStock());
    }

    @Test
    void 재고가_음수일_때_오류발생() {
        assertThrows(IllegalArgumentException.class, () -> new Stock(-10, 20));
    }
}

