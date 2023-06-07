package com.retaileatshop.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("HR"),
    SELLER("Шеф-повар"),
    BUYER("Сотрудник");

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}

