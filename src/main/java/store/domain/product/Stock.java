package store.domain.product;

public class Stock {
    private int normalStock;
    private int promotionStock;
    private static final String ERROR_NEGATIVE_STOCK = "재고는 0보다 작을 수 없습니다.";

    public Stock(int normalStock, int promotionStock) {
        validateInitialStock(normalStock);
        validateInitialStock(promotionStock);
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
    }

    private void validateInitialStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_STOCK);
        }
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

}

