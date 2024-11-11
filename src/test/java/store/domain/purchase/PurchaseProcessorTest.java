package store.domain.purchase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.product.ProductInfo;
import store.domain.product.ProductInventory;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionManager;
import store.domain.promotion.PromotionType;
import store.view.InputView;
import store.view.OutputView;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseProcessorTest {
    private InputView inputView;
    private OutputView outputView;
    private PurchaseProcessor purchaseProcessor;

    @BeforeEach
    void setUp() {
        inputView = new InputView() {
            @Override
            public List<OrderItem> readOrder() {
                return List.of(new OrderItem("Cola", 9));
            }

            @Override
            public PurchaseOption readPurchaseOption() {
                return PurchaseOption.YES;
            }
        };

        outputView = new OutputView() {
            @Override
            public void printPromotionSuggestion(String productName, int freeCount) {
                System.out.printf("현재 %s는 %d개의 무료 증정 혜택이 있습니다.\n", productName, freeCount);
            }

            @Override
            public void printNormalPriceNotification(String productName, int quantity) {
                System.out.printf("현재 %s %d개는 프로모션 적용이 되지 않습니다.\n", productName, quantity);
            }
        };

        purchaseProcessor = new PurchaseProcessor(inputView, outputView);
    }

    @Test
    void 프로모션_적용_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 9, "탄산2+1")
        );
        Promotion promotion = new Promotion(PromotionType.CARBONATED_TWO_PLUS_ONE, 2, 1,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1));
        ProductInventory inventory = ProductInventory.createFromInfos("Cola", productInfos, new PromotionManager(List.of(promotion)));

        PurchaseResult result = purchaseProcessor.process(inventory, 9);

        assertEquals("Cola", result.productName());
        assertEquals(9, result.purchasedQuantity());
        assertEquals(3, result.freeItemCount()); // 프로모션으로 3개 증정
        assertEquals(6, result.promotionAppliedQuantity()); // 구매로 프로모션 적용된 6개
    }

    @Test
    void 프로모션_없는_구매_성공() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Water", 1000, 20, "null")
        );
        ProductInventory inventory = ProductInventory.createFromInfos("Water", productInfos, new PromotionManager(List.of()));

        PurchaseResult result = purchaseProcessor.process(inventory, 5);

        assertEquals("Water", result.productName());
        assertEquals(5, result.purchasedQuantity());
        assertEquals(0, result.freeItemCount()); // 무료 증정 없음
        assertEquals(0, result.promotionAppliedQuantity()); // 프로모션 적용 없음
    }

    @Test
    void 재고_초과_구매_오류발생() {
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Water", 1000, 5, "null")
        );
        ProductInventory inventory = ProductInventory.createFromInfos("Water", productInfos, new PromotionManager(List.of()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseProcessor.process(inventory, 10));
        assertEquals("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.", exception.getMessage());
    }
}

