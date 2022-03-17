
package com.client.chatwindow;

import com.user.User;
import com.user.UserService;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

/**
 *
 * @author CÔNG SANG
 */
public class TextController implements Initializable{
    @FXML
    private ComboBox<User> cbUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserService us = new UserService();
        try {
            this.cbUser.setItems(FXCollections.observableList(us.getUsers()));
        } catch (SQLException ex) {
            Logger.getLogger(TextController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
