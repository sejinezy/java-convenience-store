package store.domain.product;

import org.junit.jupiter.api.Test;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionManager;
import store.domain.promotion.PromotionType;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductInventoryTest {
    @Test
    void 상품_인벤토리_생성_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 20, "탄산2+1"),
                new ProductInfo("Cola", 1500, 10, "null")
        );
        Promotion promotion = new Promotion(PromotionType.CARBONATED_TWO_PLUS_ONE, 2, 1,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1));
        PromotionManager promotionManager = new PromotionManager(List.of(promotion));

        ProductInventory inventory = ProductInventory.createFromInfos("Cola", productInfos, promotionManager);

        assertEquals("Cola", inventory.getName());
        assertEquals(promotion, inventory.getPromotion());
        List<String> formattedInfos = inventory.getFormattedInfos();
        assertEquals(2, formattedInfos.size());
        assertTrue(formattedInfos.get(0).contains("탄산2+1"));
    }

    @Test
    void 상품_인벤토리_중복_프로모션_오류발생() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 20, "탄산2+1"),
                new ProductInfo("Cola", 1500, 10, "MD추천상품")
        );
        PromotionManager promotionManager = new PromotionManager(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProductInventory.createFromInfos("Cola", productInfos, promotionManager));
        assertEquals("동일 상품에 여러 프로모션이 적용될 수 없습니다.", exception.getMessage());
    }
}

