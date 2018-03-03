/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.model.mappers;

import archiver.commons.ArchiverCommons;
import archiver.model.Bookmark;
import commons.SQLiteDBHelper.Mapper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anton_000
 */
public class BookmarksMapper implements Mapper<List<Bookmark>>{

    @Override
    public List<Bookmark> map(ResultSet rs) throws Exception {
        if(rs == null){
            return null;
        }
        List<Bookmark> bookmarks = new ArrayList<>();
        while(rs.next()){
            Bookmark bookmark = new Bookmark();
            bookmark.setId(rs.getInt("rowId"));
            bookmark.setName(rs.getString(Bookmark.NAME));
            bookmark.setPath(rs.getString(Bookmark.PATH));
            bookmark.setGroup(rs.getString(Bookmark.GROUP));
            bookmark.setType(ArchiverCommons.getStringType(rs.getInt(Bookmark.TYPE)));
            bookmark.setLastUpdate(rs.getString(Bookmark.LAST_UPDATE));

            bookmarks.add(bookmark);
        }
        return bookmarks;
    }
    
}