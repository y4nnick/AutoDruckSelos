package at.beachcrew;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.nio.file.Path;

import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * Created by Yannick on Donnerstag12.05.16.
 */
public class Printer {

    private PrintService myService = null;

    /**
     * Searches for the printer
     */
    public Printer(){

        //Find printer
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        if (ps.length == 0) {
            throw new IllegalStateException("No Printer found");
        }
        System.out.println("Available printers: " + Arrays.asList(ps));

        for (PrintService printService : ps) {

            if (printService.getName().equals("Officejet 7500 E910")) {
                myService = printService;
                break;
            }
        }

        if (myService == null) {
            throw new IllegalStateException("Printer not found");
        }
    }

    //Folder where the printed files are copied to
    private String finishedFolder = null;


    /**
     * Adds a folder which should be watched for files
     * @param folder
     * @param size
     * @throws IOException
     */
    public void addFolderWatch(String folder, MediaSizeName size) throws IOException{
        Path dir =  Paths.get(folder);
        new WatchDir(dir, this,size).processEvents();
    }

    /**
     * Prints a file
     * @param file
     * @param size
     */
    public void printFile(String file, MediaSizeName size){
        try{
            FileInputStream fis = new FileInputStream(file);
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            DocPrintJob printJob = myService.createPrintJob();
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

            attributes.add(OrientationRequested.LANDSCAPE);
            attributes.add(size);

            printJob.print(pdfDoc, attributes);
            fis.close();
        }catch (Exception e){
            System.err.print(e);
        }
    }
}
