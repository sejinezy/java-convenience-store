package store.domain.product;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductStockTest {
    @Test
    void 재고_계산_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 20, "탄산2+1"),
                new ProductInfo("Cola", 1500, 10, "null")
        );
        Stock stock = ProductStock.createFrom(productInfos);
        assertEquals(10, stock.getNormalStock());
        assertEquals(20, stock.getPromotionStock());
    }
}

