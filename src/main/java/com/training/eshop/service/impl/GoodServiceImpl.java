package com.training.eshop.service.impl;

import com.training.eshop.converter.GoodConverter;
import com.training.eshop.dto.GoodDto;
import com.training.eshop.service.GoodService;
import com.training.eshop.dao.GoodDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GoodServiceImpl implements GoodService {

    private static final String REGEX_LETTERS_FIGURES_POINT = "[^A-Za-z0-9.]";
    private static final String REGEX_ONLY_LETTERS = "[^A-Za-z]";
    private static final String REGEX_ONLY_FIGURES = "[A-Za-z]";
    private static final String ORDER_NOT_MADE = "Make your order\n";
    private static final String EMPTY_VALUE = "";

    private final GoodDAO goodDAO;
    private final GoodConverter goodConverter;

    @Override
    @Transactional
    public List<GoodDto> getAll() {
        return goodDAO.getAll()
                .stream()
                .map(goodConverter::convertToGoodDto)
                .collect(Collectors.toList());
    }

    @Override
    public String getStringOfOptionsForDroppingMenuFromGoodList(List<GoodDto> goods) {
        return goods.stream()
                .map(good -> "<option>" + good.getTitle() + " (" + good.getPrice() + ") </option>\n")
                .collect(Collectors.joining());
    }

    @Override
    public String getChoice(String chosenGoods) {
        if (chosenGoods != null) {
            return chosenGoods;
        }

        return ORDER_NOT_MADE;
    }

    @Override
    public String getStringOfNameAndPriceFromOptionMenu(String menu) {
        return menu.replaceAll(REGEX_LETTERS_FIGURES_POINT, EMPTY_VALUE);
    }

    @Override
    public GoodDto getGoodFromOption(String option) {
        String name = option.replaceAll(REGEX_ONLY_LETTERS, EMPTY_VALUE);
        String price = option.replaceAll(REGEX_ONLY_FIGURES, EMPTY_VALUE);

        return goodConverter.convertToGoodDto(goodDAO.getByTitleAndPrice(name, price));
    }
}
