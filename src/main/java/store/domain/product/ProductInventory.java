package store.domain.product;

import java.util.ArrayList;
import java.util.List;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionManager;
import store.domain.promotion.PromotionType;

public class ProductInventory {
    private final String name;
    private final int price;
    private final Stock stock;
    private final PromotionType promotionType;
    private final Promotion promotion;
    private static final String ERROR_MULTIPLE_PROMOTIONS = "동일 상품에 여러 프로모션이 적용될 수 없습니다.";

    public static ProductInventory createFromInfos(
            String name,
            List<ProductInfo> infos,
            PromotionManager promotionManager
    ) {
        return new ProductInventory(name, infos, promotionManager);
    }

    private ProductInventory(
            String name,
            List<ProductInfo> infos,
            PromotionManager promotionManager
    ) {
        validateInfos(infos);
        validatePromotionUniqueness(infos);
        this.name = name;
        this.price = infos.get(0).price();
        this.promotionType = findPromotionType(infos);
        this.stock = ProductStock.createFrom(infos);
        this.promotion = promotionManager.getPromotion(promotionType);
    }

    private void validateInfos(List<ProductInfo> infos) {
        if (infos == null || infos.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품 정보가 비어있습니다.");
        }
    }

    private void validatePromotionUniqueness(List<ProductInfo> infos) {
        long promotionCount = infos.stream()
                .map(ProductInfo::promotion)
                .filter(promotion -> !promotion.equals("null"))
                .distinct()
                .count();

        if (promotionCount > 1) {
            throw new IllegalArgumentException(ERROR_MULTIPLE_PROMOTIONS);
        }
    }

    private PromotionType findPromotionType(List<ProductInfo> infos) {
        return infos.stream()
                .map(info -> PromotionType.fromName(info.promotion()))
                .filter(type -> type != PromotionType.NONE)
                .findFirst()
                .orElse(PromotionType.NONE);
    }

    public List<String> getFormattedInfos() {
        List<String> infos = new ArrayList<>();

        if (promotionType != PromotionType.NONE) {
            infos.add(formatStockInfo(stock.getPromotionStock(), true));
        }
        infos.add(formatStockInfo(stock.getNormalStock(), false));
        return infos;
    }

    private String formatStockInfo(int stockQuantity, boolean isPromotion) {
        String promotionText = isPromotion ? " " + promotionType.getName() : "";

        if (stockQuantity == 0) {
            return String.format("- %s %,d원 재고 없음%s",
                    name,
                    price,
                    promotionText);
        }

        return String.format("- %s %,d원 %d개%s",
                name,
                price,
                stockQuantity,
                promotionText);
    }

    public String getName() {
        return name;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
