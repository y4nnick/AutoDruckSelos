package at.beachcrew;

public class Main {

    private static Printer printer = new Printer();

    public static void main(String[] args) {

        try{
            printer.addFolderWatch("/Applications/XAMPP/htdocs/selos/backend/core/manager/print/");
        }catch (Exception e){
            System.err.print(e);
        }
    }
}
