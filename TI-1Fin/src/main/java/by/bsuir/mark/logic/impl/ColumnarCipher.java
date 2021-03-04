package by.bsuir.mark.logic.impl;

import by.bsuir.mark.logic.CipherLogic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ColumnarCipher implements CipherLogic {

    private static final int RUS_NUM = 33;
    private static final char[] RUSSIAN_ALPHABET =
            {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л',
                    'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};
    private static final Map<Character, Integer> RUSSIAN_ALPHABET_MAP = new HashMap<>();

    public ColumnarCipher() {
        for (int i = 0; i < RUS_NUM; i++) {
            RUSSIAN_ALPHABET_MAP.put(RUSSIAN_ALPHABET[i], i);
        }
    }

    @Override
    public String encode(String plainText, String key) {
        char[] keyArr = key.toCharArray();
        char[] textArr = plainText.toCharArray();

        int keyLen = keyArr.length;
        int textLen = textArr.length;

        int rows = textLen / keyLen + 1;
        char[][] matrix = new char[rows][keyLen];

        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < keyLen; j++) {
                if (k < textLen) {
                    matrix[i][j] = textArr[k++];
                }
            }
        }

        SymbolPositionDto[] symbolPositionDtos = getSymbolPositionDtos(keyArr, keyLen);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keyLen; i++) {
            int col = symbolPositionDtos[i].pos;
            for (int j = 0; j < rows; j++) {
                if (matrix[j][col] != Character.MIN_VALUE) {
                    stringBuilder.append(matrix[j][col]);
                }
            }
        }

        return stringBuilder.toString();
    }

    private SymbolPositionDto[] getSymbolPositionDtos(char[] keyArr, int keyLen) {
        SymbolPositionDto[] symbolPositionDtos = new SymbolPositionDto[keyLen];
        for (int i = 0; i < keyLen; i++) {
            symbolPositionDtos[i] = new SymbolPositionDto(i, keyArr[i]);
        }

        Arrays.sort(symbolPositionDtos, (o1, o2) -> {
            Integer firstIndex = RUSSIAN_ALPHABET_MAP.get(o1.symbol);
            Integer secondIndex = RUSSIAN_ALPHABET_MAP.get(o2.symbol);
            int aCmp = firstIndex - secondIndex;
            return aCmp == 0 ? o1.pos - o2.pos : aCmp;
        });
        return symbolPositionDtos;
    }

    @Override
    public String decode(String encodedText, String key) {
        char[] keyArr = key.toCharArray();
        char[] textArr = encodedText.toCharArray();

        int keyLen = keyArr.length;
        int textLen = textArr.length;

        int rows = textLen / keyLen + 1;
        char[][] matrix = new char[rows][keyLen];

        for (int i = 0, k = 0; i < rows; i++) {
            for (int j = 0; j < keyLen; j++) {
                if (k < textLen) {
                    matrix[i][j] = '*';
                    k++;
                }
            }
        }

        SymbolPositionDto[] symbolPositionDtos = getSymbolPositionDtos(keyArr, keyLen);

        for (int i = 0, k = 0; i < keyLen; i++) {
            int pos = symbolPositionDtos[i].pos;
            for (int j = 0; j < rows; j++) {
                if (matrix[j][pos] != Character.MIN_VALUE) {
                    if (k < textLen) {
                        matrix[j][pos] = textArr[k++];
                    }
                }
            }
        }

        StringBuilder decodedTextBuilder = new StringBuilder();
        for (char[] chars : matrix) {
            for (char currCh : chars) {
                if (currCh != Character.MIN_VALUE) {
                    decodedTextBuilder.append(currCh);
                }
            }
        }

        return decodedTextBuilder.toString();
    }

    private static class SymbolPositionDto {
        int pos;
        char symbol;

        public SymbolPositionDto(int pos, char symbol) {
            this.pos = pos;
            this.symbol = symbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SymbolPositionDto that = (SymbolPositionDto) o;
            return pos == that.pos &&
                    symbol == that.symbol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, symbol);
        }
    }

}
