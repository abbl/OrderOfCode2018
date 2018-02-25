package com.rbc.archiver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

public class Package {
    private ArrayList<PackageFile> packageContent;
    private File packageInstance;
    private String packageName;

    Package(String packageName){
        this.packageName = packageName;
        packageInstance = new File(packageName);
        packageContent = new ArrayList<>();

        if(isPackageLoaded())
            readPackageContentIntoArrayList();
    }

    private void readPackageContentIntoArrayList(){

    }

    private void writePackageContentIntoFile(){
        try {
            Files.write(packageInstance.toPath(), convertPacketContentToStringList(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> convertPacketContentToStringList(){
        ArrayList<String> files = new ArrayList<>();

        for(PackageFile packageFile : packageContent){
            files.add(packageFile.toString());
        }
        return files;
    }

    public void addPackageFile(PackageFile packageFile){
        packageContent.add(packageFile);
    }

    public void removePackageFile(String fileName){
        for(PackageFile packageFile : packageContent){
            if(packageFile.isFileNameEqual(fileName)){
                packageFile.createFileOutsidePackage();
                packageContent.remove(packageFile);
                return;
            }
        }
        System.out.println("Such file does not exist in this package.");
    }

    public void savePackage(){
        try {
            writePackageContentIntoFile();
            packageInstance.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPackageLoaded(){
        return packageInstance.exists() && !packageInstance.isDirectory();
    }
}
