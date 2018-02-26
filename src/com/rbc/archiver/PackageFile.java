package com.rbc.archiver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class PackageFile {
    public static final char[] HEADER_MARKINGS = {'<', 'a', 'r', 'c', 'h', 'i', 'v', 'e', 'r', '>'};
    public enum PackageFileType{
        NEW, EXISTING
    }

    private String fileName;
    private String comment;
    private byte[] file;

    public PackageFile(String filePath, String comment, PackageFileType packageFileType){
        if(packageFileType == PackageFileType.NEW){
            Path path = Paths.get(filePath);
            this.fileName = path.getFileName().toString();
            this.comment = comment;
            this.file = readFile(path);
        }else{
            this.fileName = filePath;
            this.comment = comment;
        }
    }

    private byte[] readFile(Path filePath){
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(filePath);
        } catch (NoSuchFileException noSuchFileException){
            System.out.println("No file found, don't forget to include file extension e.g. .exe, .jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String toString(){
        String output;
        output = "*" + fileName + ";" + comment + ";" + file.length + "*\n"; //Tworzy nagłówek pliku
        output += new String(file, StandardCharsets.UTF_8);
        return output;
    }

    public void createFileOutsidePackage(){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(file);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName(){
        return fileName;
    }

    public String getHeader(){
        return String.valueOf(HEADER_MARKINGS)
                + fileName + ";" + comment + ";" + file.length +
                String.valueOf(HEADER_MARKINGS);
    }

    public byte[] getFileBytes(){
        return file;
    }

    public void setFileContent(byte[] bytes){
        this.file = bytes;
    }

    public boolean doesFileExist(){
        return file != null;
    }

    public boolean isFileNameEqual(String otherFileName){
        return fileName.equals(otherFileName);
    }
}
