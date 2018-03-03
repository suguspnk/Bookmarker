/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archiver.model;

/**
 *
 * @author anton_000
 */
public class Bookmark {

    public static final String TABLENAME = "bookmark";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String GROUP = "group";
    public static final String TYPE = "type";
    public static final String LAST_UPDATE = "last_update";
    
    /**
     * Model Variables
     */
    private int mId = -1;
    private String mName;
    private String mPath;
    private String mGroup;
    private String mType;
    private String lastUpdate;

    public Bookmark() {
        this.mName = "";
        this.mPath = "";
        this.mGroup = "";
        this.mType = "";
    }
    
    public Bookmark(int id, String name, String path, String group, String type) {
        this.mId = id;
        this.mName = name;
        this.mPath = path;
        this.mGroup = group;
        this.mType = type;
    }
    
    public Bookmark(String name, String path, String group, String type){
        this.mName = name;
        this.mPath = path;
        this.mGroup = group;
        this.mType = type;
    }

    public int getId() {
        return mId;
    }
    
    public void setId(int id){
        this.mId = id;
    }
    
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
    
    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getGroup() {
        return mGroup;
    }
    
    public void setGroup(String group) {
        this.mGroup = group;
    }

    public String getType() {
        return mType;
    }
    
    public void setType(String type){
        this.mType = type;
    }
    
    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "ArchiveEntry:[Name="+getName()+", Path="+getPath()+", Group="+getGroup()+", Type="+getType()+"]";
    }

    public Object[] toArray() {
        return new Object[]{
            getName(), getPath(), getGroup(), getType()
        };
    }
    
}
