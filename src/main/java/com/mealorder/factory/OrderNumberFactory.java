package com.mealorder.factory;

import java.util.List;

public interface OrderNumberFactory {

    static List<String> generateOrderNumber(int quantity) {
        // TODO: 實作隨機產生訂單號碼，包含輕食餐和豐盛餐
        return List.of();
    }

}
