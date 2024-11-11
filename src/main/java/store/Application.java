package store;

import store.config.Config;
import store.controller.PurchaseController;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        Config config = new Config();
        OutputView outputView = config.getOutputView();
        PurchaseController controller = config.getPurchaseController();

        outputView.printWelcome();
        boolean continueShopping = true;

        while (continueShopping) {
            try {
                controller.processShopping();
                continueShopping = controller.askContinueShopping();
            } catch (IllegalArgumentException e) {
                outputView.printError(e.getMessage());
            }
        }
    }
}
