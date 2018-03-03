/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author anton_000
 */
public class Commons {
    
    public static final boolean DEBUG = false;
            
    public static final String URL_REGEX = "<\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
    public static final String DISK_REGEX = "^[a-zA-Z]:((/|\\\\).*)?";
    public static final String FILE_REGEX = ".*\\.[a-zA-Z0-9]+$";
    
    /**
     * Open the uri with its default application.
     * @param uri - the uri
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void openURI(URI uri) throws FileNotFoundException, IOException{
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            desktop.browse(uri);
        }
    }

    /**
     * Check whether txt matches the pattern.
     * @param pattern - the regex pattern.
     * @param txt - the txt to match.
     * @return true if txt matches with the pattern otherwise false.
     */
    public static boolean isMatch(String pattern, String txt) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(txt);
            boolean isMatched = matcher.matches();
            return isMatched;
        } catch (RuntimeException e) {
            Commons.printException(e);
            return false;
        }
    }

    /**
     * Print the stack trace of exception e.
     * @param e - the exception
     */
    public static void printException(Exception e) {
        if(DEBUG){
            Commons.print("Handled Exception!");
            e.printStackTrace();
        }
    }

    /**
     * Print the message in the console log.
     * @param message - the message to print.
     */
    public static void print(String message) {
        if(DEBUG){
            System.out.println(">>>Thread["+Thread.currentThread().getName()+"]:::"+message);
        }
    }
}
