package store.domain.promotion;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PromotionManagerTest {
    @Test
    void 프로모션_조회_성공() {
        Promotion promotion = new Promotion(PromotionType.CARBONATED_TWO_PLUS_ONE, 2, 1,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1));
        PromotionManager manager = new PromotionManager(List.of(promotion));

        Promotion retrievedPromotion = manager.getPromotion(PromotionType.CARBONATED_TWO_PLUS_ONE);

        assertEquals(promotion, retrievedPromotion);
    }

    @Test
    void 유효하지_않은_프로모션_조회() {
        PromotionManager manager = new PromotionManager(List.of());
        Promotion retrievedPromotion = manager.getPromotion(PromotionType.CARBONATED_TWO_PLUS_ONE);
        assertNull(retrievedPromotion);
    }
}

