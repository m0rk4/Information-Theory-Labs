package by.bsuir.mark.logic.impl;

import by.bsuir.mark.logic.CipherLogic;

import java.util.PriorityQueue;

public class PlayfairCipher implements CipherLogic {

    private static class Pair {
        int i;
        int j;

        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    @Override
    public String encode(String plainText, String key) {

        char[][] keyMatrix = getKeyMatrix(key);

        StringBuilder plainBuilder = normalizeText(plainText);

        System.out.println(plainBuilder.toString());

        modifyText(plainBuilder, keyMatrix, 1);

        return plainBuilder.toString();
    }

    private void modifyText(StringBuilder plainBuilder, char[][] keyMatrix, int offset) {
        for (int i = 0; i < plainBuilder.length(); i += 2) {
            char first = plainBuilder.charAt(i);
            char second = plainBuilder.charAt(i + 1);
            Pair firstCoordinates = getCoordinates(keyMatrix, first);
            Pair secondCoordinates = getCoordinates(keyMatrix, second);
            if (firstCoordinates.i == secondCoordinates.i) {
                first = keyMatrix[firstCoordinates.i][(firstCoordinates.j + 5 + offset) % 5];
                second = keyMatrix[secondCoordinates.i][(secondCoordinates.j + 5 + offset) % 5];
            } else if (firstCoordinates.j == secondCoordinates.j) {
                first = keyMatrix[(firstCoordinates.i + 5 + offset) % 5][firstCoordinates.j];
                second = keyMatrix[(secondCoordinates.i + 5 + offset) % 5][secondCoordinates.j];
            } else {
                first = keyMatrix[firstCoordinates.i][secondCoordinates.j];
                second = keyMatrix[secondCoordinates.i][firstCoordinates.j];
            }
            plainBuilder.replace(i, i + 2, "" + first + second);
        }
    }

    private char[][] getKeyMatrix(String key) {
        PriorityQueue<Character> queue = new PriorityQueue<>();

        for (char c = 'A'; c <= 'Z'; c++) {
            queue.add(c);
        }

        char[][] keyMatrix = new char[5][5];

        // leave distinct ones
        StringBuilder keyBuilder = new StringBuilder(key);
        for (int i = 0; i < keyBuilder.length(); i++) {
            char curr = keyBuilder.charAt(i);
            for (int j = i + 1; j < keyBuilder.length(); j++) {
                if (curr == keyBuilder.charAt(j)) {
                    keyBuilder.replace(j, j + 1, "");
                    j--;
                }
            }
        }

        System.out.println(keyBuilder.toString());

        // remove j add i instead if possible
        int iInd = keyBuilder.indexOf("I");
        int jInd = keyBuilder.indexOf("J");

        if (iInd == -1 && jInd != -1) { // j
            keyBuilder.replace(jInd, jInd + 1, "I");
            queue.remove('I');
        } else if (iInd != -1 && jInd == -1) { // i
            queue.remove('I');
        } else if (iInd != -1 && jInd != -1) {
            int max = Math.max(iInd, jInd); // i j
            int min = Math.min(iInd, jInd);
            keyBuilder.replace(max, max + 1, "");
            keyBuilder.replace(min, min + 1, "I");
            queue.remove('I');
        }
        queue.remove('J');

        String normalKey = keyBuilder.toString();
        int keyLen = normalKey.length();

        System.out.println(normalKey);

        // fill matrix
        int k = 0;
        for (int i = 0; i < keyMatrix.length; i++) {
            for (int j = 0; j < keyMatrix[i].length; j++) {
                if (k < keyLen) {
                    char c = normalKey.charAt(k++);
                    keyMatrix[i][j] = c;
                    queue.remove(c);
                } else {
                    keyMatrix[i][j] = queue.poll();
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%4c", keyMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();

        return keyMatrix;
    }

    private Pair getCoordinates(char[][] keyMatrix, char ch) {
        for (int i = 0; i < keyMatrix.length; i++) {
            for (int j = 0; j < keyMatrix[i].length; j++) {
                if (keyMatrix[i][j] == ch) {
                    return new Pair(i, j);
                }
            }
        }
        return new Pair(-1, -1);
    }

    private StringBuilder normalizeText(String text) {
        text = text.replaceAll("(J)", "I");
        StringBuilder plainBuilder = new StringBuilder(text);
        for (int i = 0; i < plainBuilder.length() - 1; i += 2) {
            if (plainBuilder.charAt(i) == plainBuilder.charAt(i + 1)) {
                if (plainBuilder.charAt(i) != 'X') {
                    plainBuilder.insert(i + 1, 'X');
                } else {
                    plainBuilder.insert(i + 1, 'Q');
                }
            }
        }

        if (plainBuilder.length() % 2 != 0) {
            if (plainBuilder.charAt(plainBuilder.length() - 1) != 'X') {
                plainBuilder.append('X');
            } else {
                plainBuilder.append('Q');
            }
        }
        return plainBuilder;
    }

    @Override
    public String decode(String encodedText, String key) {
        char[][] keyMatrix = getKeyMatrix(key);

        StringBuilder textBuilder = normalizeText(encodedText);

        modifyText(textBuilder, keyMatrix, -1);

        return textBuilder.toString();
    }
}
