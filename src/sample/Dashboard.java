package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Dashboard extends ListView<User>  implements Initializable {

    private User user;
    @FXML
    private ImageView image;
    @FXML
    private Text name,username,interests,dob;
    @FXML
    private TextField search;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView<User> friendList,suggestionList;
    @FXML
    private ImageView logo;

    static class Cell extends ListCell<User> {
        HBox hBox = new HBox();
        ImageView imageView = new ImageView();
        Text text = new Text("");
        AnchorPane pane = new AnchorPane();

        public Cell() {
            super();

            text.setTextAlignment(TextAlignment.CENTER);
            //vBox.setFillWidth(true);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            text.setFont(new Font(20));
            text.setStyle("-fx-fill:#B2B2B2;");
            hBox.setHgrow(text, Priority.ALWAYS);
            pane.setPrefWidth(10);
            hBox.getChildren().addAll(imageView,pane,text);
        }

        @Override
        protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                imageView.setImage(new Image(item.getImg()));
                text.setText(item.getName());
                setGraphic(hBox);
            }
        }
    }



    public void setUser(User user) {
        try {
            logo.setImage(new Image("file:/Users/nubaf/Downloads/removebg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user = user;
        image.setImage(new Image(this.user.getImg()));
        name.setText(user.getName());
        username.setText("@"+user.getUsername());
        interests.setText(user.displayIntersts());
        dob.setText(user.getDob());
        //mobile.setText(user.getMobile());
        ArrayList<String> names = new ArrayList<>();
        for(User u:Accounts.accountList) {
            if(!u.getUsername().equals(user.getUsername()))
                names.add(u.getName());
        }
        TextFields.bindAutoCompletion(search,names);
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)  {
                search();
            }
        });
        updateFriendlist();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListViewFeed.fxml"));
            Parent root = loader.load();
            ListViewFeed check = loader.getController();
            String interests= user.getInterests().toString();
            System.out.println(interests);
            String []interestList = interests.split(",");
            interestList[0] = interestList[0].substring(1);

            if(interestList.length>1)
                interestList[interestList.length-1] = interestList[interestList.length-1].substring(1,interestList[interestList.length-1].length()-1);
            else
                interestList[0] = interestList[0].substring(0,interestList[0].length()-1);
            for(String s: interestList)
                System.out.println(s);
            check.populate(interestList);
            borderPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        suggestions();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateFriendlist() {
        friendList.getItems().clear();
        ObservableList<String> friendsArray = FXCollections.observableArrayList();
        ObservableList<User> displayArray = FXCollections.observableArrayList();
        User temp = user;
        while(temp.friends!=null){
            friendsArray.add(temp.friends.getUsername());
            displayArray.add(Accounts.searchUsername(temp.friends.getUsername()));
            temp = temp.friends;
        }
        try {
            friendList.setItems(displayArray);
            String array = "[";
            friendList.setCellFactory(param -> new Cell());
            friendList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    User searchUser = friendList.getSelectionModel().getSelectedItem();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUI.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SearchUI searchUI = loader.getController();
                    User u = new User(searchUser);
                    searchUI.setData(u,user);
                    borderPane.setCenter(root);
                }
            });
            for(String name:friendsArray) {
                array = array + "\""+name+"\",";
            }
            array = array + "]";
            if(!array.equals("[]")) {
                String update = "UPDATE users SET friends='" + array + "' where username='" + user.getUsername() + "'";
                System.out.println(update);
                Datasource.executeQuery("UPDATE users SET friends='" + array + "' where username='" + user.getUsername() + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void suggestions() {
        ObservableList<User> friendsArray = FXCollections.observableArrayList();
        User temp = user;
        while(temp.friends!=null){
            friendsArray.add(Accounts.searchUsername(temp.friends.getUsername()));
            temp = temp.friends;
        }
        ObservableList<User> friendsFriend = FXCollections.observableArrayList();
        for(User u:friendsArray) {
            User temp2 = u;
            while(temp2.friends!=null){
                friendsFriend.add(Accounts.searchUsername(temp2.friends.getUsername()));
                temp2 = temp2.friends;
            }
        }
        ObservableList<User> toSuggest =FXCollections.observableArrayList();
        for(User u:friendsFriend){
            if(!toSuggest.contains(u) && !friendsArray.contains(u)) {
                toSuggest.add(u);
                System.out.println(u.getUsername());
            }
        }
        suggestionList.setItems(toSuggest);
        suggestionList.setCellFactory(param -> new Cell());
        suggestionList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User searchUser = suggestionList.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUI.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SearchUI searchUI = loader.getController();
                User u = new User(searchUser);
                searchUI.setData(u,user);
                borderPane.setCenter(root);
            }
        });
    }

    public void search() {
        User searchUser = Accounts.searchName(search.getText());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUI.fxml"));
            Parent root =  loader.load();
            SearchUI searchUI = loader.getController();
            User u = new User(searchUser);
            searchUI.setData(u,user);
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.returnStage().setScene(new Scene(root));
        Main.returnStage().setFullScreen(true);
    }
}


