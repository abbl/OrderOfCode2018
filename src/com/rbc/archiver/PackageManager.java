package com.rbc.archiver;

public class PackageManager {

    public void pack(String[] dividedString) {
        if(checkCommandParameters(dividedString)){
            Package temporaryPackage = new Package(dividedString[1]);
            PackageFile temporaryPackageFile = new PackageFile(dividedString[2], dividedString[3]);

            if(temporaryPackageFile.doesFileExist()){
                temporaryPackage.addPackageFile(temporaryPackageFile);
                temporaryPackage.savePackage();
            }else{
                System.out.println("Such file doesn't exist, check If it is in Archiver working directory or file name you inserted is correct.");
            }
        }
    }

    public void unpack(String[] dividedString) {
        if(checkCommandParameters(dividedString)){
            Package temporaryPackage = new Package(dividedString[1]);

            if(temporaryPackage.isPackageLoaded()){
                temporaryPackage.removePackageFile(dividedString[2]);
                temporaryPackage.savePackage();
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
