package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.io.IOException;


public class SearchUI {
    @FXML
    private ImageView image;
    @FXML
    private Text username,name,dob,interests,mobile;
    @FXML
    private Button request;
    private User user,friend;

    public void setData(User friend, User user) {
        image.setImage(new Image(friend.getImg()));
        username.setText("@"+friend.getUsername());
        name.setText(friend.getName());
        interests.setText(friend.displayIntersts());
        this.user = user;
        this.friend = friend;
        User temp = Accounts.searchUsername(user.getUsername());
        User traversal = temp;
        while(traversal.friends != null) {
            traversal = traversal.friends;
            if(friend.getUsername().equals(traversal.getUsername())){
                request.setText("Unfollow");
                System.out.println(friend.getMobile());
                mobile.setText(friend.getMobile());
                dob.setText(friend.getDob());
               // request.setDisable(true);
                break;
            }
        }
    }

    public void addFriend() {
        if(request.getText().equals("Follow")) {
            user.addFriend(friend);
            request.setText("Unfollow");
        }
        else {
            user.removeFriend(friend);
            request.setText("Follow");
        }
        loadFeed();
    }

    public void loadFeed() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            Dashboard dashboard = loader.getController();
            dashboard.setUser(user);
         //   Main.returnStage().setFullScreen(false);
            Main.returnStage().setScene(new Scene(root));
        //    Main.returnStage().setFullScreen(true);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
