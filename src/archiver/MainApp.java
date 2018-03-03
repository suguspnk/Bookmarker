/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver;

import archiver.commons.Alerts;
import archiver.commons.ArchiverCommons;
import archiver.commons.DAOs;
import commons.SQLiteDBHelper;
import archiver.controller.DashboardController;
import archiver.model.Bookmark;
import commons.Commons;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author anton_000
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ArchiverCommons.primaryStage = primaryStage;

        Platform.setImplicitExit(false);

        createSystemTray(primaryStage);

        primaryStage.setMinWidth(815);
        primaryStage.setMinHeight(640);
        primaryStage.setTitle("My Bookmarker");
        primaryStage.getIcons().add(new Image("res/images/icon.png"));

        createTables();

        showDashboard();

        primaryStage.show();
    }

    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/DashboardScene.fxml"));
            Scene scene = new Scene(loader.load());

            DashboardController dashboardController = loader.getController();
            dashboardController.updateTable(ArchiverCommons.data);
            dashboardController.setScene(scene);

            ArchiverCommons.primaryStage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void createTables() {
        try {
            // Bookmark table does not exist
            if (!SQLiteDBHelper.isTableExists(ArchiverCommons.DB, Bookmark.TABLENAME)) {
                DAOs.getBookmarkDAO().createBookmarkTable();
                insertDefaultBookmarks();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
            System.exit(1);
        }
    }

    private void insertDefaultBookmarks() {
        try {
            List<Bookmark> defaultBookmarks = new ArrayList<>();
            defaultBookmarks.add(new Bookmark("Facebook", "facebook.com", "Social Media", ArchiverCommons.WEB));
            defaultBookmarks.add(new Bookmark("Twitter", "twitter.com", "Social Media", ArchiverCommons.WEB));
            defaultBookmarks.add(new Bookmark("9Gag", "9gag.com", "Social Media", ArchiverCommons.WEB));
            defaultBookmarks.add(new Bookmark("Wuxiaworld", "wuxiaworld.com", "Novels", ArchiverCommons.WEB));
            defaultBookmarks.add(new Bookmark("Youtube", "youtube.com", "Social Media", ArchiverCommons.WEB));
            defaultBookmarks.add(new Bookmark("Program Files", "C:/Program Files", "Windows", ArchiverCommons.LOCAL));
            DAOs.getBookmarkDAO().addBookmarks(defaultBookmarks);
        } catch (SQLException ex) {
            Commons.printException(ex);
        }
    }

    private void createSystemTray(Stage primaryStage) {
//        primaryStage.setOnCloseRequest(event -> {
            //Check the SystemTray is supported
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();

            final URL resource = getClass().getResource("/res/images/tray-icon.png");
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(resource);

            final TrayIcon trayIcon = new TrayIcon(image);

            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            MenuItem showItem = new MenuItem("Display");
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> System.exit(0));

            //Add components to pop-up menu
            popup.add(showItem);
            popup.addSeparator();
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            
            ActionListener displayListener = (ActionEvent e) -> {
                Platform.runLater(() -> primaryStage.show());
            };
            
            trayIcon.addActionListener(displayListener);
            showItem.addActionListener(displayListener);
            
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }
//        });
    }
}
