module com.example.swp_oppitz_ue07 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.swp_oppitz_ue07 to javafx.fxml;
    exports com.example.swp_oppitz_ue07;
}