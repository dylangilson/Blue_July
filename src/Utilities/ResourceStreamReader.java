/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 10, 2022
 */

package Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceStreamReader {

    public static BufferedReader getResourceReader(String path) {
        try {
            InputStream is = ResourceStreamReader.class.getResourceAsStream(path.substring(3)); // remove first 3 characters (res) from path
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            return reader;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can't find the resource file: " + path);
            System.exit(-1);
            return null;
        }
    }

    public static BufferedInputStream getResourceStream(String path) {
        try {
            InputStream is = ResourceStreamReader.class.getResourceAsStream(path.substring(3)); // remove first 3 (res) characters from path
            BufferedInputStream bis = new BufferedInputStream(is);
            return bis;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Can't find the resource file: " + path);
            System.exit(-1);
            return null;
        }
    }
}
