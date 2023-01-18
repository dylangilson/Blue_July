/*
 * Dylan Gilson
 * dylan.gilson@outlook.com
 * April 29, 2021
 */

package Utilities;

import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// TODO figure out how to fix this without using java.io.File

public class FileManager {

    // if path == "" -> create directory in root directory
    public static boolean createDirectory(String path, String folderName) {
        String actualPath = path.equals("") ? path + folderName : "/" + path + folderName;

        return new File(actualPath).mkdir();
    }

    // if path == "" -> delete directory in root directory
    public static boolean deleteDirectory(String path, String folderName) {
        String actualPath = path.equals("") ? path + folderName : "/" + path + folderName;

        File dir = new File(actualPath);

        return dir.delete();
    }

    // fileType must include "." at the beginning
    public static boolean createFile(String directory, String fileName, String fileType) {
        File dir;
        File newFile;
        String fullName = fileName + fileType;

        try {
            dir = new File(directory + "/");
            newFile = new File(dir, fullName);

            return newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // fileType must include "." at the beginning
    public static boolean deleteFile(String directory, String fileName, String fileType) {
        File dir;
        File newFile;
        String fullName = fileName + fileType;
        dir = new File(directory + "/");
        newFile = new File(dir, fullName);

        return newFile.delete();
    }

    // fileType must include "." at the beginning
    public static boolean writeObjectToFile(String directory, String fileName, String fileType, Object obj) {
        File dir;
        File newFile;
        String fullName = fileName + fileType;
        dir = new File(directory + "/");
        newFile = new File(dir, fullName);

        try {
            FileOutputStream fileOS = new FileOutputStream(newFile);
            ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(obj);

            objectOS.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // fileType must include "." at the beginning
    public static ObjectInputStream readFromFile(String directory, String fileName, String fileType) {
        File dir;
        File newFile;
        String fullName = fileName + fileType;
        dir = new File(directory + "/");
        newFile = new File(dir, fullName);

        try {
            FileInputStream fileIS = new FileInputStream(newFile);

            return new ObjectInputStream(fileIS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Vector3f read3DVector(ObjectInputStream objectIS) {
        try {
            Vector3f vector = (Vector3f)objectIS.readObject();

            objectIS.close();

            return vector;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
