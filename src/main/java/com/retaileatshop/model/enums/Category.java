package com.retaileatshop.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    HOTTER("Горячее"),
    COLD("Холодное"),
    VEGETERIAN("Вегетерианское"),
    DRINKS("Напитки"),
    SNACKS("Закуски"),
    DISHES_DAY("Блюда дня");

    private final String name;
}

