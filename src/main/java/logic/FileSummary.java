package logic;

import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import static java.util.stream.Collectors.toCollection;

/**
 * Class holding a map of words from a file with the capability to print it to the console in a user friendly format.
 */
public class FileSummary {

   private static final String WORD_COUNT_MESSAGE = "Number of words of length %d is %d";
   private static final String MOST_COMMON_WORD_LENGTH_MESSAGE_BEGINNING = "The most frequently occurring word length is %d, for word length";
   private Map<Integer, List<String>> wordMap;
   private PrintStream printStream;

   /**
    * Constructs the {@link FileSummary} with the word map.
    *
    * @param wordMap map of word length to lists of words of that length.
    */
   public FileSummary(Map<Integer, List<String>> wordMap) {
      this.wordMap = Collections.unmodifiableMap(wordMap);
   }

   /**
    * Prints a user friendly summary of the word map to the given stream. This will include the word count, average word length, 
    * a break down of the frequency of each word length and the most common word length(s).
    * @param printStream the {@link PrintStream} to print the summary to.
    */
   public void printSummaryTo(PrintStream printStream) {
      this.printStream = printStream;
      if (wordMap.isEmpty()) {
         printStream.println("File is empty.");
         return;
      }
      printStream.println("Word count = " + calculateWordCount());
      printStream.println(String.format("Average word length = %.3f", calculateAverageWordLength()));
      wordMap.keySet().stream()
            .sorted()
            .forEach(this::printFrequencyForWordLength);
      calculateAndPrintMostFrequentWordLengthString();
   }

   /**
    * Calculates the average word length for the entire word map.
    *
    * @return the average word length.
    */
   public double calculateAverageWordLength() {
      return wordMap.entrySet().stream()
            .mapToDouble(entry -> (entry.getKey() * entry.getValue().size()))
            .sum() / calculateWordCount();
   }

   /**
    * Calculates the total word count in the word map.
    *
    * @return the sum of the list sizes in the word map.
    */
   public long calculateWordCount() {
      return wordMap.values().stream()
            .mapToLong(List::size)
            .sum();
   }

   /**
    * Prints the frequency.
    *
    * @param wordLength the word length to print the frequency for.
    */
   private void printFrequencyForWordLength(Integer wordLength) {
      printStream.println(String.format(WORD_COUNT_MESSAGE, wordLength, wordMap.get(wordLength).size()));
   }

   /**
    * Constructs a {@link String} to state the most frequency of the most common word lengths along which word lengths have that frequency.
    * Deals with pluralising the string appropriate if there are two or more word lengths with the same frequency and correctly strings
    * together the top word lengths using commas and an ampersand as appropriate. Once the string has been built it is then printed to {@link System#out}.
    */
   private void calculateAndPrintMostFrequentWordLengthString() {
      TreeMap<Integer, Queue<Integer>> frequencyToWordLength = new TreeMap<>();
      wordMap.forEach((wordLength, words) -> frequencyToWordLength.computeIfAbsent(words.size(), unused -> new LinkedList<>()).add(wordLength));
      Map.Entry<Integer, Queue<Integer>> mostFrequentWordLengths = frequencyToWordLength.lastEntry();

      StringBuilder sb = new StringBuilder(String.format(MOST_COMMON_WORD_LENGTH_MESSAGE_BEGINNING, mostFrequentWordLengths.getKey()));
      if (mostFrequentWordLengths.getValue().size() > 1) {
         sb.append('s');
      }
      sb.append(" of ");
      sb.append(convertValuesToString(mostFrequentWordLengths.getValue()));
      printStream.println(sb.toString());
   }

   /**
    * Joins together the given {@link Queue} of {@link Integer}s into a list as would be written in good english.
    * For example: 1, 2 & 3.
    *
    * @param values the values to join together into a {@link String}.
    * @return the {@link String} containing all of the given numbers, formatted nicely.
    */
   private String convertValuesToString(Queue<Integer> values) {
      if (values.size() == 1) {
         return values.poll().toString();
      } else {
         Queue<Integer> sortedValues = values.stream().sorted().collect(toCollection(LinkedList::new));
         return convertValuesToStringRecursive(values.poll().toString(), sortedValues);
      }
   }

   /**
    * Recursive method to build up a comma and ampersand separated string by repeatedly polling the {@link Queue}.
    * If only one item remains it will be concatenated using an ampersand, otherwise it will be concatenated with a comma.
    *
    * @param toAddTo     the {@link String} to build up.
    * @param valuesToAdd the values left to be added to the {@link String}.
    * @return within each recursion this returns the given string with the first values from the {@link Queue} added. Ultimately this returns
    * the given string with all values added in order.
    */
   private String convertValuesToStringRecursive(String toAddTo, Queue<Integer> valuesToAdd) {
      if (valuesToAdd.size() == 1) {
         return toAddTo + " & " + valuesToAdd.poll();
      }
      return convertValuesToStringRecursive(toAddTo + ", " + valuesToAdd.poll(), valuesToAdd);
   }

   /**
    * Gets an unmodifiable view of the word map.
    *
    * @return the word map.
    */
   Map<Integer, List<String>> getWordMap() {
      return Collections.unmodifiableMap(wordMap);
   }
}
