package by.bsuir.mark.model;

import by.bsuir.mark.logic.CipherLogic;
import by.bsuir.mark.logic.factory.CipherLogicFactory;
import by.bsuir.mark.logic.factory.CipherType;
import by.bsuir.mark.validation.TextValidator;
import by.bsuir.mark.validation.factory.LanguageType;
import by.bsuir.mark.validation.factory.TextValidatorFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AppModel {
    private final CipherLogicFactory cipherLogicFactory;
    private final TextValidatorFactory textValidatorFactory;

    public AppModel() {
        this.cipherLogicFactory = new CipherLogicFactory();
        this.textValidatorFactory = new TextValidatorFactory();
    }

    public Optional<List<String>> loadFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return Optional.of(lines);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void writeFileToLogs(String data, String filename) {
        try {
            File dir = new File("./logs");
            if (dir.exists() || dir.mkdir()) {
                Files.write(Paths.get("./logs/", filename),
                        Collections.singletonList(data), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String data, String path) {
        try {
            Files.write(Paths.get(path), Collections.singletonList(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> decode(
            String encodedText, String key, CipherType selectedCipherType, LanguageType selectedLanguageType) {
        TextValidator textValidator = textValidatorFactory.getTextValidator(selectedLanguageType);
        if (textValidator.isValidKey(key)) {
            CipherLogic cipherLogic = cipherLogicFactory.getCipherLogic(selectedCipherType);

            String normalKey = textValidator.filter(key);
            String filter = textValidator.filter(encodedText);

            String decoded = cipherLogic.decode(filter, normalKey);
            return Optional.ofNullable(decoded);
        }
        return Optional.empty();
    }

    public Optional<String> encode(
            String plainText, String key, CipherType selectedCipherType, LanguageType selectedLanguageType) {
        TextValidator textValidator = textValidatorFactory.getTextValidator(selectedLanguageType);
        if (textValidator.isValidKey(key)) {
            CipherLogic cipherLogic = cipherLogicFactory.getCipherLogic(selectedCipherType);

            String normalKey = textValidator.filter(key);
            String normalPlainText = textValidator.filter(plainText);

            String encoded = cipherLogic.encode(normalPlainText, normalKey);
            return Optional.ofNullable(encoded);
        }
        return Optional.empty();
    }
}
