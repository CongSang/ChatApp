package com.client.chatwindow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.messages.Message;
import com.messages.dialogbox.DialogLabel;
import com.messages.dialogbox.DialogPos;
import com.user.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import animatefx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class RoomController implements Initializable {

    @FXML
    private ListView<HBox> MessageView;

    @FXML
    private ListView<User> userList;

    @FXML
    private AnchorPane apActivity;

    @FXML
    private AnchorPane apHead;

    @FXML
    private Button btnChat;

    @FXML
    private Button btnChoose;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnSaveProfile;

    @FXML
    private ImageView btnSendMess;

    @FXML
    private TextField fileChoosePath;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPhone;

    @FXML
    private Label lblUserName;

    @FXML
    private Label onlineCountLabel;

    @FXML
    private HBox onlineUsersHbox;

    @FXML
    private Pane pnlChat;

    @FXML
    private Pane pnlProfile;

    @FXML
    private ImageView showAvatar;

    @FXML
    private TextField tfMessage;
    
    @FXML
    private ScrollPane scrollPane;

    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private FileChooser fileChooser;
    private File filePath;
    public boolean saveControl = false;

    public void sendMess() throws IOException {
        String msg = tfMessage.getText();
        if (!tfMessage.getText().isEmpty()) {
            Listener.send(msg);
            tfMessage.clear();
        }
    }

    @FXML
    void btnChatClick(ActionEvent event) {
        new FadeInRight(pnlChat).play();
        pnlChat.toFront();
    }

    @FXML
    void btnChooseClick(ActionEvent event) {
        Stage stage = (Stage) btnChoose.getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    @FXML
    void btnLogoutClick(ActionEvent event) {
        logout();
    }

    @FXML
    void btnProfileClick(ActionEvent event) {
        new FadeInRight(pnlProfile).play();
        pnlProfile.toFront();
    }

    @FXML
    void btnSaveProfileClick(ActionEvent event) {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imgAvatar.setImage(image);
                showAvatar.setImage(image);
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @FXML
    void btnSendClick(MouseEvent event) throws IOException {
        sendMess();
    }

    @FXML
    void sendMessByKey(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            sendMess();
        }
    }

    public synchronized void addToChat(Message msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader()
                        .getResource("icons/" + msg.getPicture().toLowerCase() + ".png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(30);
                profileImage.setFitWidth(30);
                profileImage.getStyleClass().add("imageView");

                DialogLabel dl = new DialogLabel();
                dl.setText(msg.getName() + ": " + msg.getMsg());
                dl.getStyleClass().add("shadow");
                
                HBox x = new HBox();
                dl.setDialogPos(DialogPos.FACE_LEFT_CENTER);
                x.setPadding(new Insets(5,5,5,2));
                
                x.getChildren().addAll(profileImage, dl);
                logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserList().size()));
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            MessageView.getItems().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = showAvatar.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(30);
                profileImage.setFitWidth(30);
                profileImage.getStyleClass().add("imageView");

                DialogLabel dl = new DialogLabel();
                dl.setText(msg.getMsg());
                dl.getStyleClass().add("dialogBox");
                
                HBox x = new HBox();
                x.setMaxWidth(MessageView.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                x.setPadding(new Insets(5,2,5,5));
                
                dl.setDialogPos(DialogPos.FACE_RIGHT_CENTER);
                x.getChildren().addAll(dl, profileImage);

                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> {
            MessageView.getItems().add(yourMessages.getValue());
        });

        if (msg.getName().equals(lblUserName.getText())) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
    }

    public synchronized void addAsServer(Message msg) {
        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                DialogLabel dl = new DialogLabel();
                dl.setText(msg.getName() + " " + msg.getMsg());
                dl.getStyleClass().add("note");
                
                HBox x = new HBox();
                dl.setDialogPos(DialogPos.FACE_BOTTOM);
                x.setAlignment(Pos.CENTER);
                x.setPadding(new Insets(5,5,5,5));
                
                x.getChildren().addAll(dl);
                return x;
            }
        };
        task.setOnSucceeded(event -> {
            MessageView.getItems().add(task.getValue());
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUsernameLabel(String username) {
        this.lblUserName.setText(username);
    }

    public void setAvatar() throws IOException {
        this.showAvatar.setImage(new Image(getClass().getClassLoader().getResource("icons/user.png").toString()));
    }

    public void setUserList(Message msg) {
        logger.info("setUserList() method Enter");
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            userList.setItems(users);
            userList.setCellFactory(new CellRender());
            setOnlineLabel(String.valueOf(msg.getUserList().size()));
        });
        logger.info("setUserList() method Exit");
    }
    
    /* Displays Notification when a user joins */
    public void notificationSounds(Message msg) {
        Platform.runLater(() -> {
            try {
                Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            };
        });
    }

    public void logout() {
        try {
            Stage stage;
            Parent root;

            stage = App.getPrimaryStage();
            root = FXMLLoader.load(this.getClass().getClassLoader().getResource("client/Login.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            setAvatar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Added to prevent the enter from adding a new line to inputMessageBox */
        tfMessage.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendMess();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });
    }
}
