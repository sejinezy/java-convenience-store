package store.domain.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionManager;

public class ProductManager {
    private final List<ProductInventory> inventory;
    private final PromotionManager promotionManager;

    public ProductManager(List<ProductInfo> products, List<Promotion> promotions) {
        this.promotionManager = new PromotionManager(promotions);
        Map<String, List<ProductInfo>> productGroups = groupProductsByName(products);
        this.inventory = initializeInventory(products, productGroups);
    }

    private Map<String, List<ProductInfo>> groupProductsByName(List<ProductInfo> products) {
        Map<String, List<ProductInfo>> productGroups = new HashMap<>();
        for (ProductInfo info : products) {
            productGroups.computeIfAbsent(info.name(), k -> new ArrayList<>()).add(info);
        }
        return productGroups;
    }

    private List<ProductInventory> initializeInventory(
            List<ProductInfo> originalProducts,
            Map<String, List<ProductInfo>> productGroups
    ) {
        List<ProductInventory> orderedInventory = new ArrayList<>();
        Set<String> processedProducts = new HashSet<>();

        for (ProductInfo product : originalProducts) {
            addProductToInventory(product, productGroups, processedProducts, orderedInventory);
        }

        return orderedInventory;
    }

    private void addProductToInventory(
            ProductInfo product,
            Map<String, List<ProductInfo>> productGroups,
            Set<String> processedProducts,
            List<ProductInventory> inventory
    ) {
        String productName = product.name();
        if (processedProducts.contains(productName)) {
            return;
        }

        List<ProductInfo> productInfos = productGroups.get(productName);
        inventory.add(ProductInventory.createFromInfos(productName, productInfos, promotionManager));
        processedProducts.add(productName);
    }

    public List<String> getFormattedProductList() {
        List<String> formattedList = new ArrayList<>();
        for (ProductInventory product : inventory) {
            formattedList.addAll(product.getFormattedInfos());
        }
        return formattedList;
    }

    public ProductInventory findProduct(String name) {
        return inventory.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
    }

    public List<ProductInventory> getInventory() {
        return Collections.unmodifiableList(inventory);
    }
}
