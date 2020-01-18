package logic;


import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileContentsAnalyserTest {

   private FileContentsAnalyser systemUnderTest = new FileContentsAnalyser();

   @Test
   public void shouldCorrectlySummariseSimpleString() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("This is a test string. It's an easy one.")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(1).size());
      Assertions.assertEquals(2, wordMap.get(2).size());
      Assertions.assertEquals(1, wordMap.get(3).size());
      Assertions.assertEquals(4, wordMap.get(4).size());
   }

   @Test
   public void shouldSplitApartDoubleBarrelledWords() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("Double-barrelled")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(6).size());
      Assertions.assertEquals(1, wordMap.get(9).size());
   }

   @Test
   public void shouldHandleLotsOfPunctuationByIgnoringIt() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("!!I - love,,,.?/ 'punctuation'?")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(1).size());
      Assertions.assertEquals(1, wordMap.get(4).size());
      Assertions.assertEquals(1, wordMap.get(11).size());
   }

   @Test
   public void shouldHandleOtherWhiteSpaceCharactersThanSpace() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("One\nword\rper\tline.")).getWordMap();
      Assertions.assertEquals(2, wordMap.get(3).size());
      Assertions.assertEquals(2, wordMap.get(4).size());
   }

   @Test
   public void shouldCorrectlyHandleNumericStrings() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("205 3.142 3E08 11/4/95")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(3).size());
      Assertions.assertEquals(1, wordMap.get(4).size());
      Assertions.assertEquals(1, wordMap.get(5).size());
      Assertions.assertEquals(1, wordMap.get(7).size());
   }

   /**
    * Test cases built using online keyboards for french, arabic and japanese characters.
    * Words are most likely gibberish as I didn't translate anything.
    */
   @Test
   public void shouldCorrectlyHandleCharactersFromOtherLanguages() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("èèÆÇÙ ôœ Œ")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(1).size());
      Assertions.assertEquals(1, wordMap.get(2).size());
      Assertions.assertEquals(1, wordMap.get(5).size());

      wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("بلفغا بقعت")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(4).size());
      Assertions.assertEquals(1, wordMap.get(5).size());

      wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("つふし ぎじぐぜ")).getWordMap();
      Assertions.assertEquals(1, wordMap.get(3).size());
      Assertions.assertEquals(1, wordMap.get(4).size());
   }
   
   @Test
   public void shouldStripOutEmptyLines() {
      Map<Integer, List<String>> wordMap = systemUnderTest.analyseFileContentsAndPrintSummary(
            Stream.of("Hello, ", "", "world!")).getWordMap();
      Assertions.assertNull(wordMap.get(0));
      Assertions.assertEquals(2, wordMap.get(5).size());
   }

}