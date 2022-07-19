/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class is used to append login success and fails to a log file
 * @author Admin
 */
public class Log {
    
    
    private static final String filename = "login_activity.txt";
    private static File file;
    
    /**
     * This Function creates a file called the contents of the filename variable.
     */
    public static void createLog(){
        try {
            file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                Log.write("Log file Created.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("File ERROR!!! " + e);
        }
    }
    
    /**
     * This Functions writes to the log file
     * @param str is the string written to log file the localDateTime will always be pre-appended
     */
    public static void write(String str){
        str = LocalDateTime.now().toString() + ": " + str;
        
        try {
            FileWriter myWriter = new FileWriter(filename, true);
            myWriter.append(str + "\n");
            myWriter.close();
            System.out.println("Log Appended: " + str);
        } catch (IOException e) {
            System.out.println("File ERROR!!! " + e);
        }
    }
    
}
