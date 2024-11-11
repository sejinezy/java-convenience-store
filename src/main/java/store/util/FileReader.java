package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.product.ProductInfo;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionType;

public class FileReader {
    private final String productsFile;
    private final String promotionsFile;
    private static final String DELIMITER = ",";
    private static final String ERROR_FILE_READ = "파일을 읽는 중 오류가 발생했습니다.";
    private static final String ERROR_EMPTY_FILE = "파일이 비어있습니다.";
    private static final String ERROR_NO_DATA = "파일에 데이터가 없습니다.";
    private static final String ERROR_INVALID_NUMBER = "숫자 형식이 올바르지 않습니다.";
    private static final String ERROR_PRODUCT_FORMAT = "상품 데이터 형식이 올바르지 않습니다.";
    private static final String ERROR_PROMOTION_FORMAT = "프로모션 데이터 형식이 올바르지 않습니다.";
    private static final String ERROR_DATE_FORMAT = "날짜 형식이 올바르지 않습니다.";

    public FileReader(String productsFile, String promotionsFile) {
        this.productsFile = productsFile;
        this.promotionsFile = promotionsFile;
    }

    public List<ProductInfo> readProducts() {
        return parseProductInfos(readLines(productsFile));
    }

    public List<Promotion> readPromotions() {
        return parsePromotions(readLines(promotionsFile));
    }

    private List<String> readLines(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            validateFileContent(lines);
            return lines.subList(1, lines.size());
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_FILE_READ);
        }
    }

    private void validateFileContent(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalStateException(ERROR_EMPTY_FILE);
        }
        if (lines.size() == 1) {
            throw new IllegalStateException(ERROR_NO_DATA);
        }
    }

    private List<ProductInfo> parseProductInfos(List<String> lines) {
        return lines.stream()
                .map(this::parseProductInfo)
                .collect(Collectors.toList());
    }

    private ProductInfo parseProductInfo(String line) {
        String[] parts = splitLine(line, ERROR_PRODUCT_FORMAT, 4);
        return new ProductInfo(
                parts[0],
                parsePositiveInteger(parts[1], "가격"),
                parsePositiveInteger(parts[2], "수량"),
                parts[3]
        );
    }

    private List<Promotion> parsePromotions(List<String> lines) {
        return lines.stream()
                .map(this::parsePromotion)
                .collect(Collectors.toList());
    }

    private Promotion parsePromotion(String line) {
        String[] parts = splitLine(line, ERROR_PROMOTION_FORMAT, 5);
        return new Promotion(
                PromotionType.fromName(parts[0]),
                parsePositiveInteger(parts[1], "구매수량"),
                parsePositiveInteger(parts[2], "증정수량"),
                parseDate(parts[3]),
                parseDate(parts[4])
        );
    }

    private String[] splitLine(String line, String errorMessage, int expectedParts) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != expectedParts) {
            throw new IllegalArgumentException(errorMessage);
        }
        return parts;
    }

    private int parsePositiveInteger(String value, String field) {
        try {
            int number = Integer.parseInt(value);
            if (number <= 0) {
                throw new IllegalArgumentException(
                        String.format("%s는 0보다 커야 합니다.", field));
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_INVALID_NUMBER);
        }
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(ERROR_DATE_FORMAT);
        }
    }
}
