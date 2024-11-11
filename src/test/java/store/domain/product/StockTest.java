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

    @Test
    void 재고_감소_성공() {
        Stock stock = new Stock(20, 10);

        stock.decreaseNormalStock(5);
        stock.decreasePromotionStock(3);

        assertEquals(15, stock.getNormalStock());
        assertEquals(7, stock.getPromotionStock());
    }

    @Test
    void 재고_감소_초과_오류발생() {
        Stock stock = new Stock(20, 10);

        assertThrows(IllegalArgumentException.class, () -> stock.decreaseNormalStock(25));
        assertThrows(IllegalArgumentException.class, () -> stock.decreasePromotionStock(15));
    }


}

