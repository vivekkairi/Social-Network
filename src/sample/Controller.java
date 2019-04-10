package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button signUpBtn;
    @FXML
    TextField txtUsername, txtPassword;
    @FXML
    Text error;
    @FXML
    ImageView logo;

    public void signUpWindow() {
        try {
            Main.returnStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("signUp.fxml"))));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void login() {
        System.out.println("Username: "+ txtUsername.getText());
        System.out.println("Password: " + txtPassword.getText());
        if (txtUsername.getText().equals("") || txtPassword.getText().equals("")) {
            error.setText("Username/Password field empty.");
            return;
        }
        User currentUser = Accounts.searchUsername(txtUsername.getText());
        if (currentUser == null) {
            error.setText("No such user found. Consider Signing Up");
        } else {
            if (currentUser.getPassword().equals(txtPassword.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    Parent root = loader.load();
                    Dashboard dashboard = loader.getController();
                    dashboard.setUser(currentUser);
                    Main.returnStage().setScene(new Scene(root));
                    Main.returnStage().setFullScreen(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            else {
                txtPassword.setText("");
                error.setText("Wrong Password.");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logo.setImage(new Image("file:/Users/nubaf/Downloads/removebg.png"));
    }
}
