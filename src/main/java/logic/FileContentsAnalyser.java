package logic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileContentsAnalyser {

   private static final String PUNCTUATION = "[[\\p{Punct}]—\n\r\t]";
   private static final String SPLIT_REGEX = PUNCTUATION + "?[\n\r\t\\-—\\s]+" + PUNCTUATION + "?";
   private static final String ENDING_WITH_PUNCTUATION = "(.*?)" + PUNCTUATION;
   private static final String STARTING_WITH_PUNCTUATION = PUNCTUATION + "(.*?)";

   public FileSummary analyseFileContentsAndPrintSummary(Stream<String> fileLines) {
      Map<Integer, List<String>> wordMap = fileLines.parallel()
            .map(line -> line.split(SPLIT_REGEX))
            .flatMap(Stream::of)
            .map(this::stripPunctuation)
            .filter(word -> word.length() != 0)
            .collect(Collectors.groupingBy(String::length));

      return new FileSummary(wordMap);
   }

   private String stripPunctuation(String word) {
      while (word.matches(ENDING_WITH_PUNCTUATION)) {
         word = word.substring(0, word.length() - 1);
      }
      while (word.matches(STARTING_WITH_PUNCTUATION)) {
         word = word.substring(1);
      }
      return word;
   }

}
