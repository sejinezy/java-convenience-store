package store.config;

import java.util.List;
import store.controller.PurchaseController;
import store.domain.product.ProductInfo;
import store.domain.product.ProductManager;
import store.domain.promotion.Promotion;
import store.domain.purchase.PurchaseProcessor;
import store.domain.receipt.ReceiptGenerator;
import store.util.FileReader;
import store.view.InputView;
import store.view.OutputView;

public class Config {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final PurchaseController purchaseController;

    public Config() {
        FileReader fileReader = new FileReader(PRODUCTS_FILE_PATH, PROMOTIONS_FILE_PATH);
        inputView = new InputView();
        outputView = new OutputView();

        List<ProductInfo> productInfos = fileReader.readProducts();
        List<Promotion> promotions = fileReader.readPromotions();

        ProductManager productManager = new ProductManager(productInfos, promotions);
        PurchaseProcessor purchaseProcessor = new PurchaseProcessor(inputView, outputView);
        ReceiptGenerator receiptGenerator = new ReceiptGenerator();

        purchaseController = new PurchaseController(inputView, outputView, productManager, purchaseProcessor, receiptGenerator);
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public PurchaseController getPurchaseController() {
        return purchaseController;
    }
}

