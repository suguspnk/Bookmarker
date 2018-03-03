/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.controller;

import archiver.commons.Alerts;
import archiver.commons.ArchiverCommons;
import archiver.commons.DAOs;
import archiver.model.Bookmark;
import archiver.view.DetailsDialog;
import commons.Commons;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author anton_000
 */
public class DashboardController implements Initializable {

    @FXML // fx:id="tableArchives"
    private TableView<Bookmark> tableArchives;

    @FXML // fx:id="colName"
    private TableColumn<Bookmark, String> colName;

    @FXML // fx:id="colPath"
    private TableColumn<Bookmark, String> colPath;

    @FXML // fx:id="colGroup"
    private TableColumn<Bookmark, String> colGroup;

    @FXML // fx:id="colGroup"
    private TableColumn<Bookmark, String> colType;

    @FXML // fx:id="colGroup"
    private TableColumn<Bookmark, String> colActions;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnClear;

    private Scene scene;

    public DashboardController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final float NAME_WIDTH = 0.2f, PATH_WIDTH = 0.35f, OTHERS_WIDTH = 0.15f;

        try {
            // load the database
            ArchiverCommons.data = FXCollections.observableArrayList(DAOs.getBookmarkDAO().fetchBookmarks());
        } catch (Exception ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
        }

        // Initialize the person table
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.prefWidthProperty().bind(tableArchives.widthProperty().multiply(NAME_WIDTH));

        colPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        colPath.prefWidthProperty().bind(tableArchives.widthProperty().multiply(PATH_WIDTH));
        Callback<TableColumn<Bookmark, String>, TableCell<Bookmark, String>> cellFactory
                = (final TableColumn<Bookmark, String> param) -> {
                    final TableCell<Bookmark, String> cell = new TableCell<Bookmark, String>() {

                Node node;

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setButton();
                        setGraphic(node);
                    }
                }

                private void setButton() {
                    Bookmark row = (Bookmark) getTableRow().getItem();
                    if (row == null) {
                        return;
                    }
                    node = new Hyperlink(row.getPath());
                    ((Hyperlink) node).setTooltip(new Tooltip(row.getPath()));
                    ((Hyperlink) node).setOnAction((ActionEvent e) -> {
                        open(row.getPath(), row.getType());
                    });
                }
            };
                    return cell;
                };
        colPath.setCellFactory(cellFactory);

        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colGroup.prefWidthProperty().bind(tableArchives.widthProperty().multiply(OTHERS_WIDTH));

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.prefWidthProperty().bind(tableArchives.widthProperty().multiply(OTHERS_WIDTH));

        colActions.setCellValueFactory(new PropertyValueFactory<>("actions"));
        colActions.prefWidthProperty().bind(tableArchives.widthProperty().multiply(OTHERS_WIDTH));
        colActions.setCellFactory((final TableColumn<Bookmark, String> param) -> {
            final TableCell<Bookmark, String> cell = new TableCell<Bookmark, String>() {

                Node node;

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setButton();
                        setGraphic(node);
                    }
                }

                private void setButton() {
                    node = new Hyperlink("Delete");
                    ((Hyperlink) node).setOnAction((ActionEvent e) -> {
                        Bookmark row = (Bookmark) getTableRow().getItem();
                        // confirm delete
                        Optional<ButtonType> result = Alerts.showConfirmation(
                                "Confirm Delete",
                                "Are you sure you want to delete " + row.getName() + "?",
                                "This bookmark will be deleted and no information about this bookmark will be recorded. ");
                        if (result.get() == ButtonType.OK) {
                            removeTableRow(row);
                        }
                    });
                }
            };
            return cell;
        });
        colActions.getStyleClass().add("center-align");

        tableArchives.setSelectionModel(null);
        tableArchives.setRowFactory(tv -> {
            TableRow<Bookmark> row = new TableRow<>();
            
            row.setOnMouseEntered(event -> {
                if(!row.isEmpty()){
                    row.setCursor(Cursor.HAND);
                    row.getStyleClass().add("bg-info");
                }
            });
            
            row.setOnMouseExited(event -> {
                row.setCursor(Cursor.DEFAULT);
                row.getStyleClass().remove("bg-info");
            });
            
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Bookmark bookmarkRow = row.getItem();
                    if (bookmarkRow != null) {
                        try {
                            boolean okClicked = DetailsDialog.getInstance().showEditBookmarkDialog(bookmarkRow);
                            if (okClicked) {
                                updateTableRow(bookmarkRow);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                            Alerts.showException(ex);
                        }
                    }
                }
            });
            return row;
        });

        ArchiverCommons.focus(tfSearch);
    }

    @FXML
    private void handleBtnAddClicked() {
        try {
            Bookmark tmpEntry = new Bookmark();
            boolean okClicked = DetailsDialog.getInstance().showAddBookmarkDialog(tmpEntry);
            if (okClicked) {
                addTableRow(tmpEntry);
            }
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Commons.printException(ex);
        }
    }

    @FXML
    private void clearSearchTF() {
        tfSearch.setText("");
    }

    @FXML
    private void handleTFSearchKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            tfSearch.setText("");
        }
    }

    @FXML
    private void openShownRows() {
        String searchStr = tfSearch.getText().trim();
        if (!searchStr.isEmpty()) {
            for (Bookmark bookmark : ArchiverCommons.filteredData) {
                open(bookmark.getPath(), bookmark.getType());
            }
        }
    }

    @FXML
    private void handleKeyTyped(KeyEvent event) {
        Commons.print("handleKeyTyped");
        tfSearch.requestFocus();
        tfSearch.fireEvent(event);
    }

    private void open(String path, String type) {
        try {
            if (ArchiverCommons.WEB.equalsIgnoreCase(type)
                    && !(path.startsWith("http://") || path.startsWith("https://"))) {
                path = "http://" + path;
            }
            ArchiverCommons.openURI(type, path);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.toString().contains("cannot find the file")) {
                Alerts.showError("Path Error", "Cannot open the specified path.", "Please make sure that \"" + path + "\" exists.");
            } else {
                Alerts.showException(ex);
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
        }
    }

    public void updateTable(ObservableList<Bookmark> masterData) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        ArchiverCommons.filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            ArchiverCommons.filteredData.setPredicate(row -> {
                return filter(row, newValue);
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Bookmark> sortedData = new SortedList<>(ArchiverCommons.filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableArchives.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableArchives.setItems(sortedData);
        tableArchives.refresh();
    }

    public void addTableRow(Bookmark bookmark) {
        try {
            int newId = DAOs.getBookmarkDAO().addBookmark(bookmark);
            bookmark.setId(newId);

            ArchiverCommons.data.add(0, bookmark);
            updateTable(ArchiverCommons.data);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
        }
    }

    public void updateTableRow(Bookmark bookmark) {
        try {
            DAOs.getBookmarkDAO().updateBookmark(bookmark);
            tableArchives.refresh();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
        }
    }

    public void removeTableRow(Bookmark bookmark) {
        try {
            DAOs.getBookmarkDAO().deleteBookmarkById(bookmark.getId());
            ArchiverCommons.data.remove(bookmark);
            updateTable(ArchiverCommons.data);
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showException(ex);
        }
    }

    public boolean filter(Bookmark row, String newValue) {
        // If filter text is empty, display all persons.
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }

        // Compare first name and last name of every person with filter text.
        String lowerCaseFilter = newValue.toLowerCase();

        if (row.getName().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (row.getPath().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (row.getGroup().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (row.getType().toLowerCase().contains(lowerCaseFilter)) {
            return true;
        }
        return false; // Does not match.
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
