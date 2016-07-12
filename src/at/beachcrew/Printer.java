package at.beachcrew;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.*;

/**
 * Created by Yannick on Donnerstag12.05.16.
 */
public class Printer {

    private PrintService printerA3 = null;
    private PrintService printerA5 = null;

    private final static String PRINTER_A3 = "Officejet";
    private final static String PRINTER_A5 = "Brother";

    /**
     * Searches for the printer
     */
    public Printer(){

        while(printerA3 == null || printerA5 == null){
            System.out.println("--------------- SEARCHING FOR PRINTER ---------------");

            //Find printer
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
            if (ps.length == 0) {
                throw new IllegalStateException("No Printer found");
            }

            System.out.println("Available printers:");
            for (PrintService printService : ps) {
                System.out.println("- " + printService.getName());
            }
            System.out.println("-----------------------------------------------------");


            for (PrintService printService : ps) {

                if (printerA3 == null  && printService.getName().equals(PRINTER_A3)) {
                    printerA3 = printService;
                    continue;
                }

                if (printerA5 == null  && printService.getName().equals(PRINTER_A5)) {
                    printerA5 = printService;
                    continue;
                }
            }


            if (printerA3 == null) {
                System.err.println("--> A3 printer not found");
            }else{
                System.out.println("--> A3 printer found: " + PRINTER_A3);
            }

            if (printerA5 == null) {
                System.err.println("--> A5 printer not found");
            }else{
                System.out.println("--> A5 printer found: " + PRINTER_A5);
            }

            try{
                if(printerA3 == null || printerA5 == null){
                    Thread.sleep(5000);
                }
            }catch (InterruptedException e){
                System.out.println("Thread sleep interupted");
                System.out.println(e);
            }

        }

        System.out.println("---------------- Found both printers ---------------- ");
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
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PDF, null);
            DocPrintJob printJob = service.createPrintJob();
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();

            if(size != MediaSizeName.ISO_A5)attributes.add(OrientationRequested.LANDSCAPE);

            attributes.add(size);

            printJob.print(pdfDoc, attributes);
            fis.close();

            System.out.println("\t\tDruckauftrag gesendet: "+file);
            System.out.println("-----------------------------------------------------");
        }catch (Exception e){
            System.err.println("-----------------------------------------------------");
            System.err.println("\t\tFehler während Druck: "+file);
            System.err.print(e);
            System.err.println("-----------------------------------------------------");

        }
    }
}
