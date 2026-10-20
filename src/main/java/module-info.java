module com.mycompany.liebreytortuga {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.liebreytortuga to javafx.fxml;
    exports com.mycompany.liebreytortuga;
}
