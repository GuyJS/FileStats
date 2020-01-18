package logic;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileSummary {

   private Map<Integer, List<String>> wordMap;

   public FileSummary(Map<Integer, List<String>> wordMap) {
      this.wordMap = Collections.unmodifiableMap(wordMap);
   }

   public void printSummaryTo(PrintStream printStream) {
      printStream.println("Word count: " + calculateWordCount());
      printStream.println("Average word length: " + calculateAverageWordLength());
   }

   private double calculateAverageWordLength() {
      return wordMap.entrySet().stream()
            .mapToDouble(entry -> (entry.getKey() * entry.getValue().size()))
            .sum() / calculateWordCount();
   }

   private long calculateWordCount() {
      return wordMap.values().stream()
            .mapToLong(List::size)
            .sum();
   }

}
