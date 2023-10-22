package com.training.eshop.converter;

import com.training.eshop.dto.GoodDto;
import com.training.eshop.model.Good;

public interface GoodConverter {

    GoodDto convertToGoodDto(Good good);
}