package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;

public class FinalSignUp {

    private User user;
    private String male="file:/Users/nubaf/Downloads/man.png";
    private String female="file:/Users/nubaf/Downloads/women.png";
    private String img="";
    @FXML
    private ImageView image;
    @FXML
    private CheckBox tech,politics,sports,entertainment;
    @FXML
    private Text error;
    public void selectPic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
                ,new FileChooser.ExtensionFilter("JPG/JPEG Files", "*.jp*")
        );
        File selectedFile = fileChooser.showOpenDialog(Main.returnStage());
        System.out.println(selectedFile.getName());
        Circle clip = new Circle(120,152,200);
       // image.setClip(clip);
        image.setImage(new Image("file:"+selectedFile.getAbsolutePath()));
        img = "file:"+selectedFile.getAbsolutePath();
    }

    public void setUser(User user) {
        this.user = user;
        img = user.getGender().equals("male")?male:female;
        image.setImage(new Image(img));
    }

    public void loadDashboard() {
        if(tech.isSelected() || politics.isSelected() || sports.isSelected() || entertainment.isSelected()) {
            try {
                Datasource.executeQuery("INSERT into users (name,username,password,dob,img,mobile) VALUES ('" + user.getName() + "','" + user.getUsername() + "','" + user.getPassword() + "','" + user.getDob() + "','" + img + "','" + user.getMobile()+"')");
                user.setImg(img);
                setCheckbox();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();
                Dashboard dashboard = loader.getController();
                dashboard.setUser(user);
                Main.returnStage().setScene(new Scene(root));
                //  Main.returnStage().setFullScreen(true);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }else {
            error.setText("Select atleast one interest.");
        }
    }

    public void setCheckbox() {
        if(tech.isSelected()){
            user.setInterests("tech");
        }
        if(politics.isSelected()){
            user.setInterests("politics");
        }if(sports.isSelected()){
            user.setInterests("sports");
        }if(entertainment.isSelected()){
            user.setInterests("entertainment");
        }
    }
}
