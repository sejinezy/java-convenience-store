package store.controller;

import org.junit.jupiter.api.Test;
import store.domain.product.ProductInfo;
import store.domain.product.ProductManager;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionType;
import store.domain.purchase.OrderItem;
import store.domain.purchase.PurchaseOption;
import store.domain.purchase.PurchaseProcessor;
import store.domain.receipt.ReceiptGenerator;
import store.view.InputView;
import store.view.OutputView;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseControllerTest {
    @Test
    void 쇼핑_프로세스_성공() {
        InputView inputView = new InputView() {
            @Override
            public List<OrderItem> readOrder() {
                return List.of(new OrderItem("Cola", 3));
            }

            @Override
            public PurchaseOption readPurchaseOption() {
                return PurchaseOption.YES;
            }
        };

        OutputView outputView = new OutputView();
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 10, "탄산2+1")
        );
        List<Promotion> promotions = List.of(
                new Promotion(PromotionType.CARBONATED_TWO_PLUS_ONE, 2, 1,
                        LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1))
        );

        ProductManager productManager = new ProductManager(productInfos, promotions);
        PurchaseProcessor purchaseProcessor = new PurchaseProcessor(inputView, outputView);
        ReceiptGenerator receiptGenerator = new ReceiptGenerator();

        PurchaseController controller = new PurchaseController(
                inputView,
                outputView,
                productManager,
                purchaseProcessor,
                receiptGenerator
        );

        assertDoesNotThrow(controller::processShopping);
    }

    @Test
    void 존재하지_않는_상품_오류발생() {
        InputView inputView = new InputView() {
            @Override
            public List<OrderItem> readOrder() {
                return List.of(new OrderItem("Unknown", 1));
            }
        };

        OutputView outputView = new OutputView();
        List<ProductInfo> productInfos = List.of(
                new ProductInfo("Cola", 1500, 10, "탄산2+1")
        );
        List<Promotion> promotions = List.of();

        ProductManager productManager = new ProductManager(productInfos, promotions);
        PurchaseProcessor purchaseProcessor = new PurchaseProcessor(inputView, outputView);
        ReceiptGenerator receiptGenerator = new ReceiptGenerator();

        PurchaseController controller = new PurchaseController(
                inputView,
                outputView,
                productManager,
                purchaseProcessor,
                receiptGenerator
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                controller::processShopping);
        assertEquals("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.", exception.getMessage());
    }
}
