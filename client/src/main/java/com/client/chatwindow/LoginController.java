package com.client.chatwindow;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import animatefx.animation.*;
import com.messages.SigninMessage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginController implements Initializable {

    @FXML
    public Button btnSignIn;

    @FXML
    private Label lblForgot;

    @FXML
    private Pane pnlSignIn;

    @FXML
    private Pane pnlSignUp;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPasswordSignIn;

    @FXML
    private TextField tfPasswordSignUp;

    @FXML
    private TextField tfUsernameSignIn;

    @FXML
    private TextField tfUsernameSignUp;

    public static RoomController con;
    private static LoginController instance;
    private Scene scene;

    public LoginController() {
        instance = this;
    }

    public static LoginController getInstance() {
        return instance;
    }

    public void loginButtonAction() throws IOException {
        String hostname = "localhost";
        int port = 9001;
        String username = tfUsernameSignIn.getText();
        String password = tfPasswordSignIn.getText();
        String picture = "user";
        
        if (checkLoginSuccess(username, password)) {
        
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("client/Room.fxml"));
            Parent window = (Pane) fxmlLoader.load();
            con = fxmlLoader.<RoomController>getController();

            Listener listener = new Listener(hostname, port, username, picture, con);
            Thread x = new Thread(listener);
            x.start();
            this.scene = new Scene(window);
        }
        else {
            this.showErrorDialog("Sai tk hoac mk!!");
        }
    }
    
    public boolean checkLoginSuccess(String username, String password) {
        String hostname = "localhost";
        int port = 9001;
        Socket socket;
        
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connecting...");
            SigninMessage msg = new SigninMessage(username, password);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(msg);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            int correct = (int) input.readInt();
            if (socket.isConnected()){
                System.out.println("Connected.");
                if(correct == 1) {
                    socket.close();
                    return true;
                }
                else {
                    socket.close();
                    return false;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {
            Stage stage = (Stage) tfUsernameSignIn.getScene().getWindow();
            stage.setResizable(true);

            stage.setOnCloseRequest((WindowEvent e) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setScene(this.scene);
            stage.centerOnScreen();
            con.setUsernameLabel(tfUsernameSignIn.getText());
            try {
                con.setAvatar();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @FXML
    void btnSwitchSignUpClick(ActionEvent event) {
        new FadeInRight(pnlSignUp).play();
        pnlSignUp.toFront();

        tfFirstName.setText("");
        tfLastName.setText("");
        tfEmail.setText("");
        tfUsernameSignUp.setText("");
        tfPasswordSignUp.setText("");
        tfPhone.setText("");
    }

    @FXML
    void btnBackClicked(MouseEvent event) {
        new FadeInRight(pnlSignIn).play();
        pnlSignIn.toFront();

        tfUsernameSignIn.setText("");
        tfPasswordSignIn.setText("");
    }
    
    @FXML
    void btnCreateAccountClicked(ActionEvent event) {
    
    }

    public void generateAnimation() {
        Random rand = new Random();
        int sizeOfSquare = rand.nextInt(50) + 1;
        int speedOfSquare = rand.nextInt(10) + 5;
        int startXPoint = rand.nextInt(420);
        int startYPoint = rand.nextInt(350);
        int direction = rand.nextInt(5) + 1;

        KeyValue moveXAxis = null;
        KeyValue moveYAxis = null;
        Rectangle r1 = null;

        switch (direction) {
            case 1:
                // MOVE LEFT TO RIGHT
                r1 = new Rectangle(0, startYPoint, sizeOfSquare, sizeOfSquare);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSquare);
                break;
            case 2:
                // MOVE TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSquare, sizeOfSquare);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSquare);
                break;
            case 3:
                // MOVE LEFT TO RIGHT, TOP TO BOTTOM
                r1 = new Rectangle(startXPoint, 0, sizeOfSquare, sizeOfSquare);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSquare);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSquare);
                break;
            case 4:
                // MOVE BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, 420 - sizeOfSquare, sizeOfSquare, sizeOfSquare);
                moveYAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 5:
                // MOVE RIGHT TO LEFT
                r1 = new Rectangle(420 - sizeOfSquare, startYPoint, sizeOfSquare, sizeOfSquare);
                moveXAxis = new KeyValue(r1.xProperty(), 0);
                break;
            case 6:
                // MOVE RIGHT TO LEFT, BOTTOM TO TOP
                r1 = new Rectangle(startXPoint, 0, sizeOfSquare, sizeOfSquare);
                moveXAxis = new KeyValue(r1.xProperty(), 350 - sizeOfSquare);
                moveYAxis = new KeyValue(r1.yProperty(), 420 - sizeOfSquare);
                break;

            default:
                System.out.println("default");
        }

        r1.setFill(Color.web("#84b4c8"));
        r1.setOpacity(0.1);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(speedOfSquare * 1000), moveXAxis, moveYAxis);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        pnlSignIn.getChildren().add(pnlSignIn.getChildren().size() - 1, r1);
    }

    public void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(message);
            alert.setContentText("ERROR");
            alert.showAndWait();
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            generateAnimation();
            numberOfSquares--;
        }
    }
}
