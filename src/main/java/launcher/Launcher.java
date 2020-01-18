package launcher;

public class Launcher {

   public static void main(String[] args) {
      if (args.length == 0) {
         System.out.println("Specify the file to parse via command line argument.");
      } else {
         new FileSummariser().summariseFile(args[0]);
      }
   }
}
