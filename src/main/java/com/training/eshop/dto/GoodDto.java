package com.training.eshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GoodDto {

    private String title;

    private BigDecimal price;
}
