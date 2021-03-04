package by.bsuir.mark.logic.factory;

import by.bsuir.mark.logic.CipherLogic;
import by.bsuir.mark.logic.impl.ColumnarCipher;
import by.bsuir.mark.logic.impl.PlayfairCipher;
import by.bsuir.mark.logic.impl.VigenereCipher;

public class CipherLogicFactory {
    public CipherLogic getCipherLogic(CipherType cipherType) {
        switch (cipherType) {
            case COLUMNAR:
                return new ColumnarCipher();
            case PLAYFAIR:
                return new PlayfairCipher();
            case VIGENERE:
                return new VigenereCipher();
            default:
                throw new EnumConstantNotPresentException(CipherType.class, cipherType.name());
        }
    }
}
