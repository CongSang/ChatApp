module com.client.chatwindow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;
    requires java.sql;
    requires AnimateFX;
    requires org.slf4j;

    opens com.client.chatwindow to javafx.fxml;
    exports com.client.chatwindow;
    exports com.user;
}
