package launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Small integration test to prove that it can load a file with a few words in and process them.
 * The file is created in a temporary folder at the beginning of the test to ensure it is cleaned up afterwards.
 */
class FileSummariserIT {

   @TempDir
   File temporaryFolder;
   private File testFile;
   @Mock
   private PrintStream mockedPrintStream;

   @BeforeEach
   public void setUp() throws IOException {
      MockitoAnnotations.initMocks(this);
      testFile = new File(temporaryFolder.getAbsolutePath() + "/testFile.txt");
      try (FileWriter fileWriter = new FileWriter(testFile)) {
         fileWriter.append("Here are some words for it to read.");
      }
   }

   @Test
   public void shouldParseFileAndPrintSummaryToGivenPrintStream() {
      new FileSummariser(mockedPrintStream).summariseFile(testFile.getAbsolutePath());
      InOrder inOrder = Mockito.inOrder(mockedPrintStream);
      inOrder.verify(mockedPrintStream).println("Word count = 8");
      inOrder.verify(mockedPrintStream).println("Average word length = 3.375");
      inOrder.verify(mockedPrintStream).println("Number of words of length 2 is 2");
      inOrder.verify(mockedPrintStream).println("Number of words of length 3 is 2");
      inOrder.verify(mockedPrintStream).println("Number of words of length 4 is 3");
      inOrder.verify(mockedPrintStream).println("Number of words of length 5 is 1");
      inOrder.verify(mockedPrintStream).println("The most frequently occurring word length is 3, for word length of 4");
   }

}