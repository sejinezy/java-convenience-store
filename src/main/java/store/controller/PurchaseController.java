package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.product.ProductInventory;
import store.domain.product.ProductManager;
import store.domain.purchase.OrderItem;
import store.domain.purchase.PurchaseOption;
import store.domain.purchase.PurchaseProcessor;
import store.domain.purchase.PurchaseResult;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptGenerator;
import store.view.InputView;
import store.view.OutputView;

public class PurchaseController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ProductManager productManager;
    private final PurchaseProcessor purchaseProcessor;
    private final ReceiptGenerator receiptGenerator;

    public PurchaseController(InputView inputView, OutputView outputView,
                              ProductManager productManager,
                              PurchaseProcessor purchaseProcessor,
                              ReceiptGenerator receiptGenerator) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.productManager = productManager;
        this.purchaseProcessor = purchaseProcessor;
        this.receiptGenerator = receiptGenerator;
    }

    public void processShopping() {
        outputView.printProductList(productManager.getFormattedProductList());
        Receipt receipt = processTransaction();
        outputView.printReceipt(receipt);
    }

    private Receipt processTransaction() {
        List<PurchaseResult> purchaseResults = processOrders();
        boolean useMembership = askMembership();
        return receiptGenerator.generate(purchaseResults, productManager.getInventory(), useMembership);
    }

    private List<PurchaseResult> processOrders() {
        List<OrderItem> orderItems = inputView.readOrder();
        List<PurchaseResult> results = new ArrayList<>();
        for (OrderItem item : orderItems) {
            ProductInventory product = productManager.findProduct(item.productName());
            results.add(purchaseProcessor.process(product, item.quantity()));
        }
        return results;
    }

    private boolean askMembership() {
        outputView.printMembershipQuestion();
        return inputView.readPurchaseOption() == PurchaseOption.YES;
    }

    public boolean askContinueShopping() {
        outputView.printContinueQuestion();
        return inputView.readPurchaseOption() == PurchaseOption.YES;
    }
}

