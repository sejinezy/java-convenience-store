package store.domain.purchase;

import store.domain.product.ProductInventory;
import store.domain.product.Stock;
import store.domain.promotion.Promotion;
import store.view.InputView;
import store.view.OutputView;


public class PurchaseProcessor {
    private static final String ERROR_STOCK_SHORTAGE = "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    private static final String ERROR_PROMOTION_STOCK_SHORTAGE = "프로모션 재고가 부족합니다.";
    private static final int NO_FREE_ITEMS = 0;
    private static final int NO_PROMOTION_APPLIED = 0;

    private final InputView inputView;
    private final OutputView outputView;

    public PurchaseProcessor(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public PurchaseResult process(ProductInventory product, int requestedQuantity) {
        Promotion promotion = product.getPromotion();
        if (promotion == null || !promotion.isValidPeriod()) {
            return handleWithoutPromotion(product, requestedQuantity);
        }
        return handleWithPromotion(product, requestedQuantity, promotion);
    }

    private PurchaseResult handleWithoutPromotion(ProductInventory product, int quantity) {
        Stock stock = product.getStock();
        validateStock(stock.getNormalStock(), quantity);
        stock.decreaseNormalStock(quantity);
        return createPurchaseResult(product.getName(), quantity, NO_FREE_ITEMS, NO_PROMOTION_APPLIED);
    }

    private PurchaseResult handleWithPromotion(ProductInventory product, int quantity, Promotion promotion) {
        int setSize = promotion.getPromotionSetSize();
        Stock stock = product.getStock();
        int maxPromoQuantity = calculateMaxPromotionQuantity(stock, setSize);

        if (shouldSuggestAdditionalQuantity(quantity, maxPromoQuantity, setSize)) {
            return suggestAdditionalQuantity(product, quantity, promotion, setSize);
        }

        if (quantity <= maxPromoQuantity) {
            return applyPromotion(stock, product, quantity, setSize, promotion);
        }

        return handleStockShortage(product, quantity, maxPromoQuantity, setSize);
    }

    private int calculateMaxPromotionQuantity(Stock stock, int setSize) {
        return (stock.getPromotionStock() / setSize) * setSize;
    }

    private boolean shouldSuggestAdditionalQuantity(int quantity, int maxPromoQuantity, int setSize) {
        return quantity < maxPromoQuantity && quantity % setSize != 0;
    }

    private PurchaseResult suggestAdditionalQuantity(
            ProductInventory product,
            int quantity,
            Promotion promotion,
            int setSize
    ) {
        int additionalQuantity = calculateAdditionalQuantity(quantity, setSize);
        outputView.printPromotionSuggestion(product.getName(), promotion.getFreeCount());

        if (inputView.readPurchaseOption() == PurchaseOption.YES) {
            return processAdditionalQuantity(product, quantity, additionalQuantity, promotion, setSize);
        }

        return createPromotionResult(product, quantity, setSize, promotion);
    }

    private int calculateAdditionalQuantity(int quantity, int setSize) {
        return setSize - (quantity % setSize);
    }

    private PurchaseResult processAdditionalQuantity(
            ProductInventory product,
            int quantity,
            int additionalQuantity,
            Promotion promotion,
            int setSize
    ) {
        int newQuantity = quantity + additionalQuantity;
        validateStock(product.getStock().getPromotionStock(), newQuantity);
        product.getStock().decreasePromotionStock(newQuantity);
        return createPromotionResult(product, newQuantity, setSize, promotion);
    }

    private PurchaseResult applyPromotion(
            Stock stock,
            ProductInventory product,
            int quantity,
            int setSize,
            Promotion promotion
    ) {
        stock.decreasePromotionStock(quantity);
        return createPromotionResult(product, quantity, setSize, promotion);
    }

    private PurchaseResult handleStockShortage(
            ProductInventory product,
            int quantity,
            int maxPromoQuantity,
            int setSize
    ) {
        Stock stock = product.getStock();
        int remainingQuantity = quantity - maxPromoQuantity;
        validateStock(stock.getNormalStock(), remainingQuantity);

        outputView.printNormalPriceNotification(product.getName(), remainingQuantity);
        if (inputView.readPurchaseOption() == PurchaseOption.NO) {
            return finalizePartialPromotion(product, maxPromoQuantity, setSize);
        }

        return finalizeMixedPurchase(product, quantity, maxPromoQuantity, setSize);
    }

    private PurchaseResult finalizePartialPromotion(ProductInventory product, int maxPromoQuantity, int setSize) {
        product.getStock().decreasePromotionStock(maxPromoQuantity);
        return createPromotionResult(product, maxPromoQuantity, setSize, product.getPromotion());
    }

    private PurchaseResult finalizeMixedPurchase(
            ProductInventory product,
            int quantity,
            int maxPromoQuantity,
            int setSize
    ) {
        Stock stock = product.getStock();
        int normalStockUsed = quantity - maxPromoQuantity;
        stock.decreasePromotionStock(stock.getPromotionStock());
        stock.decreaseNormalStock(normalStockUsed);
        int freeItems = calculateFreeItems(maxPromoQuantity, setSize, product.getPromotion());
        return createPurchaseResult(product.getName(), quantity, freeItems, maxPromoQuantity);
    }

    private int calculateFreeItems(int maxPromoQuantity, int setSize, Promotion promotion) {
        return (maxPromoQuantity / setSize) * promotion.getFreeCount();
    }

    private void validateStock(int stock, int quantity) {
        if (stock < quantity) {
            throw new IllegalArgumentException(ERROR_STOCK_SHORTAGE);
        }
    }

    private PurchaseResult createPromotionResult(ProductInventory product, int quantity, int setSize, Promotion promotion) {
        int totalSets = quantity / setSize;
        int freeItems = totalSets * promotion.getFreeCount();
        int promoApplied = totalSets * promotion.getBuyCount();
        return createPurchaseResult(product.getName(), quantity, freeItems, promoApplied);
    }

    private PurchaseResult createPurchaseResult(String name, int quantity, int freeItems, int promoApplied) {
        return new PurchaseResult(name, quantity, freeItems, promoApplied);
    }
}
