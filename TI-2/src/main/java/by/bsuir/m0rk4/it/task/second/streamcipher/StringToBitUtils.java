package by.bsuir.m0rk4.it.task.second.streamcipher;

public class StringToBitUtils {
    public static long getBits(String bitsStr) {
        long result = 0;
        for (int i = 0; i < 36; i++) {
            long bit = bitsStr.charAt(i) - '0';
            result ^= (-bit ^ result) & (1L << (35 - i));
        }
        return result;
    }
}
