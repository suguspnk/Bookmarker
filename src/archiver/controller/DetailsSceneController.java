/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.controller;

import archiver.commons.ArchiverCommons;
import archiver.model.Bookmark;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author anton_000
 */
public class DetailsSceneController implements Initializable {

    @FXML
    private TextField tfPath;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfGroup;
    @FXML
    private Button btnBrowse;
    @FXML
    private ComboBox cmbxType;
    @FXML
    private Button btnOk;

    private FileChooser directoryAndFileChooser;

    private Stage dialogStage;

    private Bookmark data;

    private boolean okClicked;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfPath.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean isFocused) -> {
                    if (!isFocused) {
                        updateType();
                    }
                });

        directoryAndFileChooser = new FileChooser();
    }

    private void updateType() {
        final String TYPE = ArchiverCommons.getURIType(tfPath.getText());
        cmbxType.setValue(TYPE);
    }

    @FXML
    private void showFileChooser() {
        File file = directoryAndFileChooser.showOpenDialog(ArchiverCommons.primaryStage);
        if (file != null) {
            tfPath.setText(file.getAbsolutePath());
            updateType();
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            data.setPath(tfPath.getText());
            data.setName(tfName.getText());
            data.setGroup(tfGroup.getText());
            data.setType((String) cmbxType.getValue());

            okClicked = true;
            dialogStage.close();
        } else {
            highlightError();
        }
    }

    private boolean isInputValid() {
        return !tfPath.getText().trim().isEmpty() && !tfName.getText().trim().isEmpty();
    }

    public void initUI() {
        tfPath.setText("");
        ArchiverCommons.focus(tfPath);
        tfPath.getStyleClass().remove("bg-danger");

        tfName.setText("");
        tfName.getStyleClass().remove("bg-danger");

        tfGroup.setText("");
    }

    private void highlightError() {
        if (tfName.getText().trim().isEmpty()) {
            tfName.getStyleClass().add("bg-danger");
            ArchiverCommons.focus(tfName);
        } else {
            tfName.getStyleClass().remove("bg-danger");
        }
        
        if (tfPath.getText().trim().isEmpty()) {
            tfPath.getStyleClass().add("bg-danger");
            ArchiverCommons.focus(tfPath);
        } else {
            tfPath.getStyleClass().remove("bg-danger");
        }
    }

    public TextField getTfPath() {
        return tfPath;
    }

    public TextField getTfName() {
        return tfName;
    }

    public TextField getTfGroup() {
        return tfGroup;
    }

    public ComboBox getCmbxType() {
        return cmbxType;
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    public void setData(Bookmark data) {
        this.data = data;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void updateOkButton(String txt) {
        btnOk.setText(txt);
    }

    public void updateUI() {
        tfPath.setText(data.getPath());
        tfName.setText(data.getName());
        tfGroup.setText(data.getGroup());
        cmbxType.setValue(data.getType());
    }

    public void update(Bookmark bookmark) {
        setData(bookmark);
        updateUI();
    }
}
