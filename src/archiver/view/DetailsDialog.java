/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.view;

import archiver.commons.ArchiverCommons;
import archiver.controller.DetailsSceneController;
import archiver.model.Bookmark;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author anton_000
 */
public class DetailsDialog extends Stage{
    
    private static DetailsDialog instance;
    private DetailsSceneController controller;
    
    private DetailsDialog() throws IOException{
        setTitle("Add Bookmark");
        getIcons().add(new Image("res/images/icon.png"));
        initModality(Modality.WINDOW_MODAL);
        initOwner(ArchiverCommons.primaryStage);
        setResizable(false);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsScene.fxml"));
        setScene(new Scene(loader.load()));

        controller = loader.getController();
        controller.setDialogStage(this);
    }
    
    public DetailsSceneController getController(){
        return controller;
    }
    
    public static DetailsDialog getInstance() throws IOException{
        if(instance == null){
            instance = new DetailsDialog();
        }
        return instance;
    }

    public boolean isOkClicked() {
        return controller.isOkClicked();
    }

    public boolean showAddBookmarkDialog(Bookmark tmpEntry) {
        setTitle("Add Bookmark");
        controller.updateOkButton("Add");
        controller.setData(tmpEntry);
        showAndWait();
        return controller.isOkClicked();
    }
    
    public boolean showEditBookmarkDialog(Bookmark bookmark) {
        setTitle("Update Bookmark");
        controller.updateOkButton("Update");
        controller.setData(bookmark);
        showAndWait();
        return controller.isOkClicked();
    }
    
    @Override
    public void showAndWait(){
        controller.initUI();
        controller.updateUI();
        super.showAndWait();
    }

}
