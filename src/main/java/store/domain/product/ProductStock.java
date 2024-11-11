package store.domain.product;

import java.util.List;
import store.domain.promotion.PromotionType;

public class ProductStock {
    public static Stock createFrom(List<ProductInfo> infos) {
        return new Stock(
                calculateNormalStock(infos),
                calculatePromotionStock(infos)
        );
    }

    private static int calculateNormalStock(List<ProductInfo> infos) {
        return infos.stream()
                .filter(info -> PromotionType.fromName(info.promotion()) == PromotionType.NONE)
                .mapToInt(ProductInfo::quantity)
                .sum();
    }

    private static int calculatePromotionStock(List<ProductInfo> infos) {
        return infos.stream()
                .filter(info -> PromotionType.fromName(info.promotion()) != PromotionType.NONE)
                .mapToInt(ProductInfo::quantity)
                .sum();
    }
}