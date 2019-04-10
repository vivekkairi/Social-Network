package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;

public class ListViewFeed extends ListView<NewsFeed> implements Initializable {

    @FXML
    private ListView<NewsFeed> listView;
    @FXML
    private static AnchorPane anchor;

    ObservableList<NewsFeed> list = FXCollections.observableArrayList();

    static class Cell extends ListCell<NewsFeed> {
        VBox vBox = new VBox();
        Label label = new Label("");
        Text text = new Text();
        AnchorPane pane = new AnchorPane();

        public Cell() {
            super();
            pane.setPrefHeight(15);
            text.setFont(new Font(18));
            label.setFont(new Font(28));
            label.setTextFill(Color.web("#78cee5"));
            text.setStyle("-fx-fill: #B2B2B2");
            vBox.getChildren().addAll(label, text,pane);
        }

        @Override
        protected void updateItem(NewsFeed item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                label.setText(item.getHeading());
                text.setText(item.getContent());
                setGraphic(vBox);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     //   listView.setItems(list);

    }
    @FXML
    public void populate(String[] interests) throws SQLException{
        list.clear();
        for (String s : interests) {
            if(s.charAt(0) == ' '){
                s = s.substring(1);
            }
            System.out.println("Interest" + s);
            String query = "SELECT * from feeds WHERE interest = '"+s+"' ORDER BY RANDOM() LIMIT 5";
            ResultSet rs = Datasource.dbExecute(query);
            while (rs.next()) {
                try {
                    String heading  = rs.getString("heading");
                    String content = rs.getString("content");
                    NewsFeed news = new NewsFeed(heading,content);
                    list.add(news);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        Collections.shuffle(list);
        listView.setItems(list);
        listView.setCellFactory(param -> new Cell());
    }
}