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

    private PrintService printerA3 = null;
    private PrintService printerA5 = null;

    /**
     * Searches for the printer
     */
    public Printer(){

        System.out.println("####### SEARCHING FOR PRINTER ###########");

        //Find printer
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        if (ps.length == 0) {
            throw new IllegalStateException("No Printer found");
        }
        System.out.println("Available printers: " + Arrays.asList(ps));

        for (PrintService printService : ps) {

            if (printService.getName().equals("Officejet 7500 E910")) {
                printerA3 = printService;
                break;
            }
        }

        if (printerA3 == null) {
            System.err.println("--> A3 printer not found");
            throw new IllegalStateException("Printer not found");
        }else{
            System.out.println("--> A3 printer found");
        }

        if (printerA5 == null) {
            System.err.println("--> A5 printer not found");
        }else{
            System.out.println("--> A5 printer found");
        }

    }

    //Folder where the printed files are copied to
    private String finishedFolder = null;


    /**
     * Adds a folder which should be watched for files
     * @param baseFolder
     * @throws IOException
     */
    public void addFolderWatch(String baseFolder) throws IOException{
        new WatchDir(baseFolder,printerA3,printerA5, this).processEvents();
    }

    /**
     * Prints a file
     * @param file
     * @param size
     */
    public void printFile(PrintService service, String file, MediaSizeName size){
        try{
            FileInputStream fis = new FileInputStream(file);
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            DocPrintJob printJob = service.createPrintJob();
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

            attributes.add(OrientationRequested.LANDSCAPE);
            attributes.add(size);

            printJob.print(pdfDoc, attributes);
            fis.close();

            System.out.format("%s: Druckauftrag gesendet: ",file);
        }catch (Exception e){
            System.err.format("%s: Fehler während Druck: ",file);
            System.err.print(e);

        }
    }
}
