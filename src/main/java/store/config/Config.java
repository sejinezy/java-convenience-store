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
        inputView = createInputView();
        outputView = createOutputView();
        purchaseController = createPurchaseController();
    }

    private InputView createInputView() {
        return new InputView();
    }

    private OutputView createOutputView() {
        return new OutputView();
    }

    private PurchaseController createPurchaseController() {
        FileReader fileReader = createFileReader();
        List<ProductInfo> productInfos = readProductInfos(fileReader);
        List<Promotion> promotions = readPromotions(fileReader);
        ProductManager productManager = createProductManager(productInfos, promotions);
        PurchaseProcessor purchaseProcessor = createPurchaseProcessor();
        ReceiptGenerator receiptGenerator = createReceiptGenerator();

        return new PurchaseController(inputView, outputView, productManager,
                purchaseProcessor, receiptGenerator
        );
    }

    private FileReader createFileReader() {
        return new FileReader(PRODUCTS_FILE_PATH, PROMOTIONS_FILE_PATH);
    }

    private List<ProductInfo> readProductInfos(FileReader fileReader) {
        return fileReader.readProducts();
    }

    private List<Promotion> readPromotions(FileReader fileReader) {
        return fileReader.readPromotions();
    }

    private ProductManager createProductManager(
            List<ProductInfo> productInfos,
            List<Promotion> promotions
    ) {
        return new ProductManager(productInfos, promotions);
    }

    private PurchaseProcessor createPurchaseProcessor() {
        return new PurchaseProcessor(inputView, outputView);
    }

    private ReceiptGenerator createReceiptGenerator() {
        return new ReceiptGenerator();
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public PurchaseController getPurchaseController() {
        return purchaseController;
    }
}
