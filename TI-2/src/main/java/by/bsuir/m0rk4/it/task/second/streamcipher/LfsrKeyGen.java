package by.bsuir.m0rk4.it.task.second.streamcipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class LfsrKeyGen {

    private final byte[] bytes = new byte[XorCrypt.BUFFER_SIZE];
    private long currentState;

    public LfsrKeyGen(long initialValue) {
        this.currentState = initialValue;
    }

    public byte[] getNextNBytes(int nBytes) {
        for (int i = 0; i < nBytes; i++) {
            for (int j = Byte.SIZE - 1; j >= 0; j--) {
                long highestBit = (currentState >> 35) & 1L;
                bytes[i] ^= (-highestBit ^ bytes[i]) & (1L << j);
                long tenthBit = (currentState >> 10) & 1L;
                currentState <<= 1;
                currentState ^= (-(highestBit ^ tenthBit) ^ currentState) & 1L;
            }
        }
        return bytes;
    }

    public void getCsv() {
        List<String> lines = new LinkedList<>();
        lines.add(";x^36 + x^11 + 1;\n");
        StringBuilder tmp = new StringBuilder();
        tmp.append(";;;;");
        for (int i = 36; i >= 1; i--) {
            tmp.append(i).append(";");
        }
        lines.add(tmp.toString());
        for (int j = 0; j < 57; j++) {
            StringBuilder stringBuilder = new StringBuilder();
            long highestBit = (currentState >> 35) & 1L;
            stringBuilder.append("#").append(j + 1).append(";;");
            stringBuilder.append(highestBit).append(';');
            stringBuilder.append("<-;");
            long tenthBit = (currentState >> 10) & 1L;
            long xored = highestBit ^ tenthBit;
            for (int k = 28; k < 64; k++) {
                long bit = (currentState >> (63 - k)) & 1L;
                stringBuilder.append(bit).append(';');
            }
            stringBuilder.append(String.format("%d xor %d = %d;", highestBit, tenthBit, highestBit ^ tenthBit));
            currentState <<= 1;
            currentState ^= (-(highestBit ^ tenthBit) ^ currentState) & 1L;
           lines.add(stringBuilder.toString());
        }
        try {
            Files.write(Paths.get("./debug.csv"), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
