/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.dao;

import archiver.model.Bookmark;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author anton_000
 */
public interface BookmarkDAO {
    
    public void createBookmarkTable() throws SQLException;
    
    public List<Bookmark> fetchBookmarks() throws Exception;
    
    public int addBookmark(Bookmark newBookmark) throws SQLException;
    
    public void updateBookmark(Bookmark bookmark) throws SQLException;
    
    public void deleteBookmarkById(int id) throws SQLException;

    public void addBookmarks(List<Bookmark> defaultBookmarks) throws SQLException;
    
}
