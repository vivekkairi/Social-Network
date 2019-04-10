package sample;

import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;


public class SignUp implements Initializable {

    @FXML
    private TextField txtName,txtUsername,mobileNo;
    @FXML
    private PasswordField txtPassword,verifyPassword;
    @FXML
    private RadioButton male,female,others;
    @FXML
    private CheckBox terms;
    @FXML
    private Text error;
    @FXML
    private ImageView logo;
    @FXML
    private JFXDatePicker txtDob;

    public void checkDetails() {
        String name = txtName.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String verifyPwd = verifyPassword.getText();
        LocalDate dob = txtDob.getValue();
        LocalDate current = LocalDate.now();
        String mobile = mobileNo.getText();
        if(!name.equals("") && !username.equals("") && !password.equals("") &&
                (male.isSelected() || female.isSelected() || others.isSelected()) && terms.isSelected() && !dob.equals("") && !mobile.equals("") ) {
                if(username.length() < 3) {
                error.setText("Username should be above 3 characters in length.");
                }
                else if(password.length()<6){
                    error.setText("Enter password above 6 characters.");
                }
                else if(!password.equals(verifyPwd)) {
                    error.setText("Password and Verify Password doesn't match.");
                    txtPassword.setText("");
                    verifyPassword.setText("");
                }else if(!name.matches("[a-z A-Z]+")) {
                    error.setText("Name should contain only Alphabets");
                }else if(!username.matches("^[a-z A-Z 0-9 _]+$")) {
                    error.setText("User should contain only alphabets, numbers and underscore");
                }else  if(username.length() <3 && username.length()>10)
                    error.setText("Username should be in between 3 to 10 characters.");
                else if(!isValid(dob)) {
                    error.setText("Age should be above 18");
                }else if(Accounts.searchUsername(username) != null){
                    error.setText("Username already taken.");
                }else if(!mobile.matches("[0-9]+") || mobile.length()!=10){
                    error.setText("Enter valid mobile no");
                }
                else {
                    addUser();
                }
        }
        else {
            error.setText("Please fill all the details.");
        }
    }

    public void addUser() {
        String name = txtName.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String dob = txtDob.getValue().toString();
        String gender = male.isSelected()?"male":female.isSelected()?"female":"others";
        String mobile = mobileNo.getText();
        if(Accounts.searchUsername(username)!=null){
            loadLogin();
        }
        User u = new User(name,username,password,dob,gender,mobile);
        Accounts.accountList.add(u);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("finalSignUp.fxml"));
            Parent root = loader.load();
            FinalSignUp finalSignUp = loader.getController();
            finalSignUp.setUser(u);
            Main.returnStage().setScene(new Scene(root));
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static boolean isValid(LocalDate text) {
        LocalDate current = LocalDate.now();
        if(current.isBefore(text))
            return false;
        else if(current.getYear() - text.getYear() <18)
            return false;
        else
            return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logo.setImage(new Image("file:/Users/nubaf/Downloads/removebg.png"));
    }

    public void loadLogin() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.returnStage().setScene(new Scene(root));
    }
}
