package at.beachcrew;

import javax.print.attribute.standard.MediaSizeName;

public class Main {

    private static Printer printer = new Printer();

    public static void main(String[] args) {

        try{
            printer.addFolderWatch("/Applications/XAMPP/htdocs/selos/backend/core/manager/print/printA3_Raster", MediaSizeName.ISO_A3);
        }catch (Exception e){
            System.err.print(e);
        }


    }
}
