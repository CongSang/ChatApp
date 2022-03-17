package com.client.chatwindow;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import com.user.User;
import javafx.geometry.Insets;

public class CellRender implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>() {

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();
                    hBox.setPadding(new Insets(10));
                    
                    Text name = new Text("  " + user.getFirstName());
                    name.setStyle("-fx-font-size: 18px");
                    
                    ImageView onlineImageView = new ImageView();
                    Image statusImage = new Image(
                            getClass().getClassLoader()
                                    .getResource("icons/online.png").toString(), 16, 16,true,true);
                    onlineImageView.setImage(statusImage);

                    ImageView pictureImageView = new ImageView();
                    Image image = new Image(
                            getClass().getClassLoader()
                                    .getResource("icons/" + user.getPicture().toLowerCase() + ".png").toString(),
                            50, 50, true, true);
                    pictureImageView.setImage(image);

                    hBox.getChildren().addAll(onlineImageView, pictureImageView, name);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}
