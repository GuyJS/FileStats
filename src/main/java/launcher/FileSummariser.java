package launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import logic.FileContentsAnalyser;
import logic.FileSummary;

public class FileSummariser {

   private FileContentsAnalyser fileContentsAnalyser;

   public FileSummariser() {
      fileContentsAnalyser = new FileContentsAnalyser();
   }

   public void summariseFile(String filePath) {
      try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(filePath)))) {
         FileSummary fileSummary = fileContentsAnalyser.analyseFileContentsAndPrintSummary(fileReader.lines());
         fileSummary.printSummaryTo(System.out);
      } catch (IOException ioException) {
         System.err.println("File not found, path: " + filePath);
         ioException.printStackTrace();
      }
   }
}
