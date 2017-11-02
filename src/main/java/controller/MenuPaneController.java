package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.LICENSE;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuPaneController implements Initializable {

    @FXML
    private MenuItem aboutItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aboutItem.setOnAction(a -> displayAboutPopup());
    }

    private void displayAboutPopup() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("About");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setText(LICENSE.LICENSE_TEXT);

        Scene scene1 = new Scene(textArea, 500, 450);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }
}
