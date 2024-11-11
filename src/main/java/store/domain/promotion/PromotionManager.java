package store.domain.promotion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromotionManager {
    private final Map<PromotionType, Promotion> promotions;

    public PromotionManager(List<Promotion> promotions) {
        this.promotions = promotions.stream()
                .collect(Collectors.toMap(Promotion::getType, p -> p));
    }

    public Promotion getPromotion(PromotionType type) {
        return promotions.getOrDefault(type, null);
    }

    public boolean hasPromotion(PromotionType type) {
        return promotions.containsKey(type);
    }
}
