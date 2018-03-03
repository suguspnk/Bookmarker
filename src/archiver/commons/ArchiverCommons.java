/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.commons;

import archiver.model.Bookmark;
import commons.Commons;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author anton_000
 */
public class ArchiverCommons {
    
    public static final String DB = "bookmarker.db";
    public static Stage primaryStage;
    public static ObservableList<Bookmark> data = FXCollections.observableArrayList();
    public static FilteredList<Bookmark> filteredData;
    public static final String LOCAL = "LOCAL", WEB = "WEB";
    public static final int LOCAL_IDX = 0, WEB_IDX = 1;
    public static final HashMap<String, Integer> URI_TYPES = new HashMap(){{
        put(LOCAL, LOCAL_IDX);
        put(WEB, WEB_IDX);
    }};
    
    public static void openURI(String type, String uri) throws IOException, URISyntaxException{
        openURI(URI_TYPES.get(type), uri);
    }
    
    public static void openURI(int type, String uri) throws IOException, URISyntaxException{
        switch(type){
            case WEB_IDX:
                Commons.openURI(new URI(uri));
                break;
            case LOCAL_IDX:
                Commons.openURI(new File(uri).toURI());
                break;
        }
    }

    public static String getURIType(String uri) {
        if(Commons.isMatch(Commons.DISK_REGEX, uri)){
            return LOCAL;
        } else {
            return WEB;
        }
    }

    public static String getStringType(int type) {
        Set<String> keySet = URI_TYPES.keySet();
        for(String key : keySet){
            if(URI_TYPES.get(key) == type){
                return key;
            }
        }
        return null;
    }

    public static void focus(Control tfPath) {
        Platform.runLater(() -> {
            tfPath.requestFocus();
        });
    }

}
