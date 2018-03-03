/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.commons;

import archiver.dao.BookmarkDAO;
import archiver.dao.impl.sqlite.BookmarkDAOImpl;

/**
 *
 * @author anton_000
 */
public class DAOs {
    private static BookmarkDAO bookmarkDao;
    
    public static BookmarkDAO getBookmarkDAO(){
        if(bookmarkDao == null){
            bookmarkDao = new BookmarkDAOImpl();
        }
        return bookmarkDao;
    }
}
