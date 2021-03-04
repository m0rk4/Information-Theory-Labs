module by.bsuir.mark {
    requires javafx.controls;
    requires javafx.fxml;

    opens by.bsuir.mark.controller to javafx.fxml;
    exports by.bsuir.mark;
}