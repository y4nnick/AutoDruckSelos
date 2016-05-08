package at.beachcrew;

public class Main {

    private static Printer printer = new Printer();

    public static void main(String[] args) {

        try{
           // printer.addFolderWatch("C:\\xampp\\htdocs\\");

            //  printer.addFolderWatch("C:/xampp/htdocs/selos/backend/core/manager/print/");
            printer.addFolderWatch("C:\\xampp\\htdocs\\selos\\backend\\core\\manager\\print\\");
        }catch (Exception e){
            System.err.print(e);
        }
    }
}
