/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.dao.impl.sqlite;

import archiver.commons.ArchiverCommons;
import commons.SQLiteDBHelper;
import archiver.dao.BookmarkDAO;
import archiver.model.Bookmark;
import archiver.model.mappers.BookmarksMapper;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author anton_000
 */
public class BookmarkDAOImpl implements BookmarkDAO {

    @Override
    public List<Bookmark> fetchBookmarks() throws Exception {
        String sqlQuery = "select rowId, * from " + Bookmark.TABLENAME;
        return (List<Bookmark>) SQLiteDBHelper.query(ArchiverCommons.DB, sqlQuery, new BookmarksMapper());
    }

    @Override
    public int addBookmark(Bookmark newBookmark) throws SQLException {
        String sqlInsert
                = "insert into `" + Bookmark.TABLENAME + "`(`" + Bookmark.NAME + "`,`" + Bookmark.PATH + "`,`" + Bookmark.GROUP + "`,`" + Bookmark.TYPE + "`) values (?,?,?,?);";
        return SQLiteDBHelper.executeUpdate(ArchiverCommons.DB, sqlInsert,
                new Object[]{
                    newBookmark.getName(),
                    newBookmark.getPath(),
                    newBookmark.getGroup(),
                    (int) ArchiverCommons.URI_TYPES.get(newBookmark.getType())
                });
    }

    @Override
    public void updateBookmark(Bookmark bookmark) throws SQLException {
        String sqlUpdate
                = "update " + Bookmark.TABLENAME
                + " set "
                + "`" + Bookmark.NAME + "` = ?, "
                + "`" + Bookmark.PATH + "` = ?, "
                + "`" + Bookmark.GROUP + "` = ?, "
                + "`" + Bookmark.TYPE + "` = ?, "
                + "`" + Bookmark.LAST_UPDATE + "` = datetime('now') "
                + "where `rowId` = ?;";
        SQLiteDBHelper.executeUpdate(ArchiverCommons.DB, sqlUpdate,
                new Object[]{
                    bookmark.getName(),
                    bookmark.getPath(),
                    bookmark.getGroup(),
                    (int) ArchiverCommons.URI_TYPES.get(bookmark.getType()),
                    bookmark.getId()
                });
    }

    @Override
    public void deleteBookmarkById(int id) throws SQLException {
        String deleteSql = "delete from " + Bookmark.TABLENAME + " where `rowId` = ?";
        SQLiteDBHelper.executeUpdate(ArchiverCommons.DB, deleteSql, id);
    }

    @Override
    public void createBookmarkTable() throws SQLException {
        String sql = "CREATE TABLE " + Bookmark.TABLENAME + "("
                + "`" + Bookmark.NAME + "` TEXT NOT NULL, "
                + "`" + Bookmark.PATH + "` TEXT NOT NULL, "
                + "`" + Bookmark.GROUP + "` TEXT DEFAULT '', "
                + "`" + Bookmark.TYPE + "` INT DEFAULT " + ArchiverCommons.LOCAL_IDX + ", "
                + "`" + Bookmark.LAST_UPDATE + "` DATETIME DEFAULT CURRENT_TIMESTAMP );";
        SQLiteDBHelper.execute(ArchiverCommons.DB, sql);
    }

    @Override
    public void addBookmarks(List<Bookmark> bookmarks) throws SQLException {
        String sqlInsert
                = "insert into `" + Bookmark.TABLENAME + "`(`" + Bookmark.NAME + "`,`" + Bookmark.PATH + "`,`" + Bookmark.GROUP + "`,`" + Bookmark.TYPE + "`) values (?,?,?,?);";
        Object params[][] = new Object[bookmarks.size()][];
        for(int i = 0; i < params.length; i++){
            final int TYPE_IDX = 3;
            params[i] = bookmarks.get(i).toArray();
            params[i][TYPE_IDX] = (int) ArchiverCommons.URI_TYPES.get((String)params[i][TYPE_IDX]);
        }
        SQLiteDBHelper.executeBatch(ArchiverCommons.DB, sqlInsert, params);
    }

}
