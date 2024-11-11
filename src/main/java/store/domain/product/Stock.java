package store.domain.product;

public class Stock {
    private int normalStock;
    private int promotionStock;
    private static final String ERROR_NEGATIVE_STOCK = "재고는 0보다 작을 수 없습니다.";
    private static final String ERROR_EXCEED_STOCK = "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_QUANTITY = "수량은 0보다 커야 합니다.";

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

    public void decreaseNormalStock(int quantity) {
        validateQuantity(quantity);
        validateStockAvailable(quantity, normalStock);
        normalStock -= quantity;
    }

    public void decreasePromotionStock(int quantity) {
        validateQuantity(quantity);
        validateStockAvailable(quantity, promotionStock);
        promotionStock -= quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ERROR_INVALID_QUANTITY);
        }
    }

    private void validateStockAvailable(int quantity, int currentStock) {
        if (quantity > currentStock) {
            throw new IllegalArgumentException(ERROR_EXCEED_STOCK);
        }
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

}

