package launcher;

/**
 * Main class to launch the tool.
 */
public class Launcher {

   /**
    * Launches the tool to read a file and print a summary to the console.
    *
    * @param args program arguments, the first should be the file path, anything else is ignored.
    */
   public static void main(String[] args) {
      if (args.length == 0) {
         System.out.println("Specify the file to parse via command line argument.");
      } else {
         new FileSummariser().summariseFile(args[0]);
      }
   }
}
