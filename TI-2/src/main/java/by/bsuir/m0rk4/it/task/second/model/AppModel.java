package by.bsuir.m0rk4.it.task.second.model;

import by.bsuir.m0rk4.it.task.second.streamcipher.FileCryptoException;
import by.bsuir.m0rk4.it.task.second.streamcipher.LfsrKeyGen;
import by.bsuir.m0rk4.it.task.second.streamcipher.StringToBitUtils;
import by.bsuir.m0rk4.it.task.second.streamcipher.XorCrypt;
import javafx.concurrent.Task;

import java.io.*;
import java.util.Arrays;

public class AppModel {

    public Task<Void> processFile(File selectedReadFile, File selectedWriteFile, String keyInitState) throws FileCryptoException {
        long bits = StringToBitUtils.getBits(keyInitState);
        LfsrKeyGen lfsrKeyGen = new LfsrKeyGen(bits);
        XorCrypt xorCrypt = new XorCrypt(lfsrKeyGen);
        return xorCrypt.processFile(selectedReadFile, selectedWriteFile);
    }

    public String getKeyContents(File selectedReadFile, String keyInitState) {
        long bits = StringToBitUtils.getBits(keyInitState);
        LfsrKeyGen lfsrKeyGen = new LfsrKeyGen(bits);
        long length = selectedReadFile.length();
        int bytesToReadCount = length > 25 ? 25 : (int) length;
        byte[] keyBytes = lfsrKeyGen.getNextNBytes(bytesToReadCount);
        return convertBytesToBinary(keyBytes, bytesToReadCount);
    }

    public String getFileContents(File selectedReadFile) {
        byte[] buf = new byte[25];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(selectedReadFile))) {
            int read = bufferedInputStream.read(buf, 0, 25);
            return convertBytesToBinary(buf, read);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String convertBytesToBinary(byte[] bytes, int read) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < read; i++) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        return stringBuilder.toString();
    }

    public void writeToCsv(String keyInitState) {
        long bits = StringToBitUtils.getBits(keyInitState);
        LfsrKeyGen lfsrKeyGen = new LfsrKeyGen(bits);
        lfsrKeyGen.getCsv();
    }
}
