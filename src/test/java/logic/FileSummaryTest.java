package logic;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link FileSummary}.
 */
class FileSummaryTest {

   private FileSummary systemUnderTest;
   private Map<Integer, List<String>> wordMap = new LinkedHashMap<>();
   @Mock
   private PrintStream mockedPrintStream;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.initMocks(this);
      wordMap.put(3, Arrays.asList("The", "and", "but"));
      wordMap.put(5, Arrays.asList("Ahead", "there", "thing"));
      wordMap.put(7, Arrays.asList("Vehicle", "paradox"));
   }

   @Test
   public void shouldCorrectlyCalculateWordCountFromWordMap() {
      systemUnderTest = new FileSummary(wordMap);
      Assertions.assertEquals(8, systemUnderTest.calculateWordCount());
   }

   @Test
   public void shouldCorrectlyCalculateAverageWordLengthFromWordMap() {
      systemUnderTest = new FileSummary(wordMap);
      Assertions.assertEquals(38. / 8, systemUnderTest.calculateAverageWordLength());
   }

   @Test
   public void shouldCorrectlyPrintSummaryOfWordMapToGivenPrintStream() {
      systemUnderTest = new FileSummary(wordMap);
      systemUnderTest.printSummaryTo(mockedPrintStream);
      InOrder inOrder = Mockito.inOrder(mockedPrintStream);
      inOrder.verify(mockedPrintStream).println("Word count = 8");
      inOrder.verify(mockedPrintStream).println("Average word length = 4.750");
      inOrder.verify(mockedPrintStream).println("Number of words of length 3 is 3");
      inOrder.verify(mockedPrintStream).println("Number of words of length 5 is 3");
      inOrder.verify(mockedPrintStream).println("Number of words of length 7 is 2");
      inOrder.verify(mockedPrintStream).println("The most frequently occurring word length is 3, for word lengths of 3 & 5");
   }

   @Test
   public void shouldCorrectlyConcatenateMultipleMostFrequentWordLengthsTogether() {
      wordMap.put(7, Arrays.asList("Vehicle", "Paradox", "problem"));
      systemUnderTest = new FileSummary(wordMap);
      systemUnderTest.printSummaryTo(mockedPrintStream);
      Mockito.verify(mockedPrintStream).println("The most frequently occurring word length is 3, for word lengths of 3, 5 & 7");
   }

   @Test
   public void shouldAdjustGrammarForOnlyOneMostCommonWordLength() {
      wordMap.remove(3);
      systemUnderTest = new FileSummary(wordMap);
      systemUnderTest.printSummaryTo(mockedPrintStream);
      Mockito.verify(mockedPrintStream).println("The most frequently occurring word length is 3, for word length of 5");
   }

   @Test
   public void shouldPrintThatFileIsEmptyIfGivenMapIsEmpty() {
      wordMap.clear();
      systemUnderTest = new FileSummary(wordMap);
      systemUnderTest.printSummaryTo(mockedPrintStream);
      Mockito.verify(mockedPrintStream).println("File is empty.");
   }

}