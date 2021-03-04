package by.bsuir.mark.validation.factory;

import by.bsuir.mark.validation.TextValidator;
import by.bsuir.mark.validation.impl.EnglishTextValidator;
import by.bsuir.mark.validation.impl.RussianTextValidator;

public class TextValidatorFactory {
    public TextValidator getTextValidator(LanguageType languageType) {
        switch (languageType) {
            case ENG:
                return new EnglishTextValidator();
            case RUS:
                return new RussianTextValidator();
            default:
                throw new EnumConstantNotPresentException(LanguageType.class, languageType.name());
        }
    }
}
