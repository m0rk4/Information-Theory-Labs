package by.bsuir.mark.logic;

public interface CipherLogic {
    String encode(String plainText, String key);
    String decode(String encodedText, String key);
}
