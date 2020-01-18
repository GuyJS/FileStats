package logic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Object responsible for analysing the contents of a file to determine things like word count and the frequency of different word lengths.
 */
public class FileContentsAnalyser {

   /**
    * Regex to match a single piece of punctuation.
    */
   private static final String PUNCTUATION = "[\\p{Punct}]";
   /**
    * The regex used to split words. This is broadly looking for any whitespace.
    * But also includes dashes in order to split apart double-barrelled words.
    */
   private static final String SPLIT_REGEX = "[\n\r\t\\-—\\s]+";

   private static final String ENDING_WITH_PUNCTUATION = "(.*?)" + PUNCTUATION;
   private static final String STARTING_WITH_PUNCTUATION = PUNCTUATION + "(.*?)";

   /**
    * Parses the given file contents to identify each word and produce a {@link FileSummary} of those contents.
    * Words are identified by splitting using a regex that includes any whitespace character along with optional punctuation either before
    * or after the whitespace. Additional punctuation is removed via regex to handle punctuation at the beginning or end of each line which
    * {@link String#split(String)} will miss out. Zero length words are also filtered out to handle entirely blank lines.
    * @param fileLines the lines of the file to analyse in a {@link Stream}.
    * @return a new {@link FileSummary} object containing the information about the file. 
    */
   public FileSummary analyseFileContentsAndPrintSummary(Stream<String> fileLines) {
      Map<Integer, List<String>> wordMap = fileLines.parallel()
            .map(line -> line.split(SPLIT_REGEX))
            .flatMap(Stream::of)
            .map(this::stripPunctuation)
            .filter(word -> word.length() != 0)
            .collect(Collectors.groupingBy(String::length));

      return new FileSummary(wordMap);
   }

   /**
    * Strips punctuation from the start and end of a word. Repeatedly checks whether the word starts or ends with a punctuation character
    * defined via the {@link #PUNCTUATION} regex and removes it using {@link String#substring(int, int)} until all punctutation has been removed.
    * @param word the word to remove punctuation from.
    * @return the word with any punctuation at the start or end removed.
    */
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
