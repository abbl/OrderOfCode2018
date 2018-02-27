package com.rbc.archiver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Stallman Im sorry for this, don't smack me
     */
    private void readPackageContentIntoArrayList(){
        try {
            long start = System.currentTimeMillis();

            FileInputStream fileInputStream = new FileInputStream(packageName);
            final InputStream in = new BufferedInputStream(fileInputStream);
            final byte[] buf = new byte[1];

            boolean wasHeaderFound = false;
            boolean isReadingHeaderNow = false;
            byte[] fileBytes = null;
            char[] lastCharacters = new char[PackageFile.HEADER_MARKINGS.length];
            int fileBytesIndex = 0;
            int content;
            int headerIndex = 0;
            String fileName = "";
            String comment = "";
            String fileBytesSize = "";

            while (in.read(buf) != -1){
                content = buf[0];
                lastCharacters = appendCharArray(lastCharacters, (char) content);
                //Check if reader found header special character.
                if(checkIfHeaderWasFound(lastCharacters) && !isReadingHeaderNow){
                    //System.out.println("Header has been found");
                    isReadingHeaderNow = true;
                    if(fileBytes != null){
                        addPackageFileToCollection(fileName, comment, fileBytes);
                        //Clearing variables to make sure that there won't be any collisions with previously loaded file.
                        wasHeaderFound = false;
                        headerIndex = 0;
                        fileBytesIndex = 0;
                        fileBytes = null;
                        fileName = "";
                        comment = "";
                        fileBytesSize = "";
                    }
                    continue;
                } else if(checkIfHeaderWasFound(lastCharacters) && isReadingHeaderNow){
                    isReadingHeaderNow = false;
                    wasHeaderFound = true;
                    fileBytesSize = fileBytesSize.substring(0, fileBytesSize.length() - (PackageFile.HEADER_MARKINGS.length - 1));
                    fileBytes = new byte[Integer.parseInt(fileBytesSize)];
                }
                if(isReadingHeaderNow && checkIfHeaderNextValueSymbolWasFound(content)){
                    headerIndex++;
                    continue;
                }
                if(isReadingHeaderNow){
                    switch (headerIndex){
                        case 0:
                            fileName += (char) content;
                            break;
                        case 1:
                            comment += (char) content;
                            break;
                        case 2:
                            fileBytesSize += (char) content;
                            break;
                    }
                }else if(wasHeaderFound){

                    while ((fileBytesIndex < fileBytes.length) && (in.read(buf) != -1)){
                        fileBytes[fileBytesIndex] = buf[0];
                        fileBytesIndex += 1;
                    }
                    wasHeaderFound = false;
                }
            }
            in.close();
            fileInputStream.close();

            System.out.println("Reading package content time: " + (System.currentTimeMillis() - start) + "ms");

            addPackageFileToCollection(fileName, comment, fileBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char[] appendCharArray(char[] targetArray, char characterToAppend){
        char[] appendedArray = new char[targetArray.length];

        for(int a = 0; a < targetArray.length; a++){
            if(a == targetArray.length - 1) //Insert a character at the and of array.
                appendedArray[targetArray.length - 1] = characterToAppend;
            else{
                appendedArray[a] = targetArray[a + 1];
            }
        }
        return appendedArray;
    }

    private boolean checkIfHeaderWasFound(char[] lastCharacters){
        return Arrays.equals(lastCharacters, PackageFile.HEADER_MARKINGS);
    }

    private boolean checkIfHeaderNextValueSymbolWasFound(int content){
        return ((char) content) == ';';
    }

    private void addPackageFileToCollection(String fileName, String comment, byte[] fileBytes){
        if(fileName != null && comment != null && fileBytes != null){
            PackageFile packageFile = new PackageFile(fileName, comment, PackageFile.PackageFileType.EXISTING);
            packageFile.setFileContent(fileBytes);
            packageContent.add(packageFile);
        }
    }

    private void writePackageContentIntoFile(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(packageName);

            for(PackageFile packageFile : packageContent){
                fileOutputStream.write(Integer.toString(10, 2).getBytes());
                fileOutputStream.write(Integer.toString(2, 2).getBytes());
                fileOutputStream.write(packageFile.getHeader().getBytes());
                fileOutputStream.write(packageFile.getFileBytes());
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public boolean doesSuchFileExistInPackage(String fileName){
        if(findPackageFile(fileName) != null)
            return true;
        return false;
    }

    private PackageFile findPackageFile(String fileName){
        for(PackageFile packageFile : packageContent){
            if(packageFile.isFileNameEqual(fileName))
                return packageFile;
        }
        return null;
    }

    public boolean doesPackageHaveAnyHeadersInIt(){
        return !packageContent.isEmpty();
    }

    public void printPackageContent(){
        for(int a = 0; a < packageContent.size(); a++){
            PackageFile packageFile = packageContent.get(a);
            System.out.println((a + 1) + "." + packageFile.getFileDesc());
        }
    }

    public void printCertainFileDesc(String fileName){
        PackageFile packageFile = findPackageFile(fileName);
        if(packageFile != null)
            System.out.println(packageFile.getFileDesc());
        else
            System.out.println("Such file does not exist in this package");
    }
}
