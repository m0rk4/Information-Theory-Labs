package by.bsuir.mark.logic.impl;

import by.bsuir.mark.logic.CipherLogic;

import java.util.HashMap;
import java.util.Map;

public class VigenereCipher implements CipherLogic {

    private static final int RUS_NUM = 33;
    private static final char[] RUSSIAN_ALPHABET =
            {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л',
            'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};
    private static final Map<Character, Integer> RUSSIAN_ALPHABET_MAP = new HashMap<>();

    public VigenereCipher() {
        for (int i = 0; i < RUS_NUM; i++) {
            RUSSIAN_ALPHABET_MAP.put(RUSSIAN_ALPHABET[i], i);
        }
    }

    @Override
    public String encode(String plainText, String key) {
        System.out.println(plainText);
        System.out.println(key);
        int keyLen = key.length();
        int textLen = plainText.length();

        if (keyLen < textLen) {
            key += plainText.substring(0, textLen - keyLen);
        } else {
            key = key.substring(0, textLen);
        }


        StringBuilder encodedTextBuilder = new StringBuilder();

        for (int i = 0; i < textLen; i++) {
            char textCh = plainText.charAt(i);
            char keyCh = key.charAt(i);
            char toAppend = getResultedEncodingChar(textCh, keyCh);
            encodedTextBuilder.append(toAppend);
        }

        return encodedTextBuilder.toString();
    }

    @Override
    public String decode(String encodedText, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder keyBuilder = new StringBuilder(key);

        for (int i = 0; i < encodedText.length(); i++) {
            char textCh = encodedText.charAt(i);
            char keyCh = keyBuilder.charAt(i);
            char toAppend = getResultedDecodingChar(textCh, keyCh);
            stringBuilder.append(toAppend);
            keyBuilder.append(toAppend);
        }

        return stringBuilder.toString();
    }

    private char getResultedEncodingChar(char first, char second) {
        Integer firstIndex = RUSSIAN_ALPHABET_MAP.get(first);
        Integer secondIndex = RUSSIAN_ALPHABET_MAP.get(second);
        int resultedIndex = (firstIndex + secondIndex) % RUS_NUM;
        return RUSSIAN_ALPHABET[resultedIndex];
    }

    private char getResultedDecodingChar(char first, char second) {
        Integer firstIndex = RUSSIAN_ALPHABET_MAP.get(first);
        Integer secondIndex = RUSSIAN_ALPHABET_MAP.get(second);
        int resultedIndex = (firstIndex + RUS_NUM - secondIndex) % RUS_NUM;
        return RUSSIAN_ALPHABET[resultedIndex];
    }
}
