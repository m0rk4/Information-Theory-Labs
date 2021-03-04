package by.bsuir.mark.validation.impl;

import by.bsuir.mark.validation.TextValidator;

public class RussianTextValidator implements TextValidator {

    private static final String TEXT_TO_REMOVE_REG_EXP = "[^а-яА-ЯёЁ]";

    @Override
    public String filter(String text) {
        text = text.replaceAll(TEXT_TO_REMOVE_REG_EXP, "");
        return text.toUpperCase();
    }

    @Override
    public boolean isValidKey(String key) {
        key = key.replaceAll(TEXT_TO_REMOVE_REG_EXP, "");
        return !key.isEmpty();
    }

}
