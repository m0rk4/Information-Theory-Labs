package by.bsuir.m0rk4.it.task.second.streamcipher;

import javafx.concurrent.Task;

import java.io.*;

public class XorCrypt {

    public static final int BUFFER_SIZE = 8192;
    private final LfsrKeyGen lfsrKeyGen;

    public XorCrypt(LfsrKeyGen lfsrKeyGen) {
        this.lfsrKeyGen = lfsrKeyGen;
    }

    public Task<Void> processFile(File selectedReadFile, File selectedWriteFile) throws FileCryptoException {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(selectedReadFile));
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(selectedWriteFile))) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead, totalRead = 0;
                    long length = selectedReadFile.length();
                    while ((bytesRead = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        processByteBlock(buffer, bytesRead);
                        bufferedOutputStream.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                        updateProgress(totalRead, length);
                    }
                } catch (IOException e) {
                    throw new FileCryptoException(e.getMessage(), e);
                }
                return null;
            }
        };
    }

    private void processByteBlock(byte[] buffer, int bytesRead) {
        byte[] nextNBytes = lfsrKeyGen.getNextNBytes(bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            buffer[i] ^= nextNBytes[i];
        }
    }

}
