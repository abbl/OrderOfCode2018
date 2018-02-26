package com.rbc.archiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PackageManager {

    public void pack(String[] dividedString) {
        if(checkCommandParameters(dividedString)){
            Package temporaryPackage = new Package(dividedString[1]);

            if(!temporaryPackage.doesSuchFileExistInPackage(dividedString[2])){
                PackageFile temporaryPackageFile = new PackageFile(dividedString[2], dividedString[3], PackageFile.PackageFileType.NEW);
                if(temporaryPackageFile.doesFileExist()){
                    temporaryPackage.addPackageFile(temporaryPackageFile);
                    temporaryPackage.savePackage();
                    deleteFileAfterPacking(dividedString[2]);
                }else{
                    System.out.println("Such file doesn't exist, check If it is in Archiver working directory or file name you inserted is correct.");
                }
            }else{
                System.out.println("Such file already exist inside the package");
            }
        }
    }

    private void deleteFileAfterPacking(String filePath){
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unpack(String[] dividedString) {
        if(checkCommandParameters(dividedString)){
            Package temporaryPackage = new Package(dividedString[1]);

            if(temporaryPackage.isPackageLoaded()){
                temporaryPackage.removePackageFile(dividedString[2]);
                temporaryPackage.savePackage();
                System.out.println("File has been unpacked");
            }else{
                System.out.println("Can't unpack file from package which does not exist.");
            }
        }
    }

    public void info(String[] dividedString) {
    }

    private boolean checkCommandParameters(String[] dividedString){
        return dividedString[1] != null && dividedString[2] != null;
    }
}
