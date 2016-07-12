package at.beachcrew;

import javax.print.PrintService;
import javax.print.attribute.standard.MediaSizeName;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.io.*;
import java.util.*;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private static int counterA5 = 0;
    private static int counterA3 = 0;
    private static int counterA4 = 0;
    private static int counterSum = 0;


    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private boolean trace = false;

    private PrintService printerA3_A4;
    private PrintService printerA5;

    private Printer printer;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        System.out.println("Watching for new files in: "+dir);

        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDir(String baseFolder, PrintService printerA3_A4, PrintService printerA5, Printer printer) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.printerA3_A4 = printerA3_A4;
        this.printerA5 = printerA5;
        this.printer = printer;

        //A3
        Path dirA3 =  Paths.get(baseFolder+"printA3_Raster");
        register(dirA3);

        //A4
        Path dirA4 =  Paths.get(baseFolder+"printA4_Raster");
        register(dirA4);

        //A5
        Path dirA5 =  Paths.get(baseFolder+"printA5");
        register(dirA5);

        System.out.println("-----------------------------------------------------");

        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind != ENTRY_CREATE) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                //Check welche Größe gedruckt werden soll und auf welchen Drucker

                String file = child.toString();

                PrintService printerService;
                MediaSizeName size;

                if(file.contains("printA3_Raster")){
                    printerService = printerA3_A4;
                    size = MediaSizeName.ISO_A3;
                    counterA3++;
                }else if(file.contains("printA4_Raster")){
                    printerService = printerA3_A4;
                    size = MediaSizeName.ISO_A4;
                    counterA4++;
                }else{ //(file.contains("printA5"))
                    printerService = printerA5;
                    size = MediaSizeName.ISO_A5;
                    counterA5++;
                }

                counterSum++;

                // print out event
                System.out.println("#"+counterSum+": \tFound new file: " + child);

                printer.printFile(printerService, child.toString(), size);
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
}