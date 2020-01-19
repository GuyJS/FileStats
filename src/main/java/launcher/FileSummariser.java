package launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import logic.FileContentsAnalyser;
import logic.FileSummary;

/**
 * Object responsible for loading the file to process and triggering the processing.
 */
public class FileSummariser {

   private FileContentsAnalyser fileContentsAnalyser;
   private PrintStream printStream;

   /**
    * Constructs a {@link FileSummariser} with the default {@link PrintStream}.
    */
   public FileSummariser() {
      this(System.out);
   }

   /**
    * Constructor to provide the {@link PrintStream} to output file summaries to.
    *
    * @param printStream the {@link PrintStream} to output to.
    */
   FileSummariser(PrintStream printStream) {
      this.printStream = printStream;
      fileContentsAnalyser = new FileContentsAnalyser();
   }

   /**
    * Opens and reads the file at the given path if it exists, passes the contents to a {@link FileContentsAnalyser} to process and prints the summary
    * that's returned to the {@link PrintStream} provided on construction. Handles any errors associated with being unable to locate or open the file.
    *
    * @param filePath the path to the file to process, should be in a format accepted by {@link File}'s constructor.
    */
   public void summariseFile(String filePath) {
      try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(filePath)))) {
         FileSummary fileSummary = fileContentsAnalyser.analyseFileContentsAndPrintSummary(fileReader.lines());
         fileSummary.printSummaryTo(printStream);
      } catch (IOException ioException) {
         System.err.println("File not found, path: " + filePath);
         ioException.printStackTrace();
      }
   }
}
