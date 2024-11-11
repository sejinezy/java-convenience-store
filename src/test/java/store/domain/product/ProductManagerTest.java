package store.domain.product;

import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;

import java.time.LocalDate;
import java.util.List;
import store.domain.promotion.PromotionType;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {
    @Test
    void 상품_매니저_초기화_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 20, "탄산2+1"),
                new ProductInfo("Water", 1000, 50, "null")
        );
        Promotion promotion = new Promotion(PromotionType.CARBONATED_TWO_PLUS_ONE, 2, 1,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1));

        ProductManager manager = new ProductManager(productInfos, List.of(promotion));

        assertNotNull(manager);
    }

    @Test
    void 상품_검색_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 10, "탄산2+1")
        );
        ProductManager manager = new ProductManager(productInfos, List.of());

        ProductInventory product = manager.findProduct("Cola");

        assertEquals("Cola", product.getName());
    }

    @Test
    void 존재하지_않는_상품_검색_오류() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 10, "탄산2+1")
        );
        ProductManager manager = new ProductManager(productInfos, List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> manager.findProduct("Unknown"));
        assertEquals("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.", exception.getMessage());
    }
}

