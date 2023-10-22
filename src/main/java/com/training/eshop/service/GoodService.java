package com.training.eshop.service;

import com.training.eshop.dto.GoodDto;

import java.util.List;

public interface GoodService {

    List<GoodDto> getAll();

    String getStringOfOptionsForDroppingMenuFromGoodList(List<GoodDto> goods);

    String getChoice(String chosenGoods);

    String getStringOfNameAndPriceFromOptionMenu(String menu);

    GoodDto getGoodFromOption(String option);
}
