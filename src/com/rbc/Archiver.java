package com.rbc;

import com.rbc.archiver.PackageManager;

import java.io.*;

public class Archiver {
    private static boolean isRunning = true;
    private static PackageManager packetManager = new PackageManager();

    public static void main(String[] args) {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        displayAvailableOptions();
        while(isRunning){
            System.out.println("Select option:");
            try {
                String selectedOption = userInput.readLine();
                useFunctionSelectedByUser(selectedOption);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void displayAvailableOptions(){
        System.out.println("File Archiver:");
        System.out.println("-pack PACZKA PLIK KOMENTARZ");
        System.out.println("-info PACZKA PLIK");
        System.out.println("-info PACZKA");
        System.out.println("-unpack PACZKA PLIK");
        System.out.println("-exit");
        System.out.println("-displayOptions");
    }

    private static void useFunctionSelectedByUser(String selectedOption){
        String[] dividedString = selectedOption.split(" ");

        switch(dividedString[0]){
                case "-pack":
                    packetManager.pack(dividedString);
                    break;
                case "-unpack":
                    packetManager.unpack(dividedString);
                    break;
                case "-info":
                    packetManager.info(dividedString);
                    break;
                case "-displayOptions":
                    displayAvailableOptions();
                    break;
                case "-exit":
                    isRunning = false;
                    break;
            default:
                System.out.println("Given command is wrong, try again.");
        }
    }
}



