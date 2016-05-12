package at.beachcrew;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
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

    private HashMap<String,String> folder = new HashMap<>();

    //Folder where the printed files are copied to
    private String finishedFolder = null;


    /**
     * Adds a folder which should be watched for files
     * @param folder
     * @param action
     * @throws IOException
     */
    public void addFolderWatch(String folder, String action) throws IOException{
        this.folder.put(folder,action);

        Path dir =  Paths.get(folder);
        new WatchDir(dir, this).processEvents();
    }

    /**
     * Prints a file
     * @param file
     * @param size
     */
    public void printFile(String file, String size){

        try{
            FileInputStream fis = new FileInputStream(file);

            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            DocPrintJob printJob = myService.createPrintJob();

            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
            fis.close();
        }catch (Exception e){
            System.err.print(e);
        }

    }
}
