package com.rbc.archiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PackageManager {

    public void pack(String[] dividedString) {
        if(checkCommandParameters(dividedString, 3)){
            Package temporaryPackage = new Package(dividedString[1]);

            if(!temporaryPackage.doesSuchFileExistInPackage(dividedString[2])){
                PackageFile temporaryPackageFile = new PackageFile(dividedString[2], assembleComment(dividedString), PackageFile.PackageFileType.NEW);
                if(temporaryPackageFile.doesFileExist()){
                    temporaryPackage.addPackageFile(temporaryPackageFile);
                    temporaryPackage.savePackage();
                    //deleteFileAfterPacking(dividedString[2]); Not sure If this should be done cause instructions about task are unclear....
                    System.out.println("File has been packed.");
                }
            }else{
                System.out.println("Such file already exist inside the package");
            }
        }else{
            displayWrongCommandMessage();
        }
    }

    private String assembleComment(String[] dividedString){
        StringBuilder assembledString = new StringBuilder(dividedString[3]);
        if(dividedString.length > 4){
            for(int a = 4; a < dividedString.length; a++){
                assembledString.append(" ").append(dividedString[a]);
            }
        }
        return assembledString.toString();
    }

    private void deleteFileAfterPacking(String filePath){
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unpack(String[] dividedString) {
        if(checkCommandParameters(dividedString, 2)){
            Package temporaryPackage = new Package(dividedString[1]);

            if(temporaryPackage.isPackageLoaded()){
                temporaryPackage.removePackageFile(dividedString[2]);
                temporaryPackage.savePackage();
                System.out.println("File has been unpacked");
            }else{
                System.out.println("Can't unpack file from package which does not exist.");
            }
        }else{
            displayWrongCommandMessage();
        }
    }

    public void info(String[] dividedString) {
        if(dividedString.length > 1){
            String packageName = dividedString[1];
            if(packageName != null){
                Package temporaryPackage = new Package(packageName);
                if(temporaryPackage.doesPackageHaveAnyHeadersInIt()){
                    if(dividedString.length == 3){
                        String fileName = dividedString[2];
                        temporaryPackage.printCertainFileDesc(fileName);
                    }else{
                        temporaryPackage.printPackageContent();
                    }
                }else{
                    System.out.println("Given package is either empty or is not a file made with this software. OR DOES NOT EVEN EXIST sick!");
                }
            }
        }else {
            displayWrongCommandMessage();
        }
    }

    private boolean checkCommandParameters(String[] dividedString, int commandLength){
        if(dividedString.length > commandLength)
            return true;
        return false;
    }

    private void displayWrongCommandMessage(){
        System.out.println("Given command is wrong, please check command pattern.");
    }
}
