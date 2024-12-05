package com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.testrunner.core;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.InputBuilder;
import net.automatalib.word.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reads and writes tests from/to files.
 * <p>
 * Mutations of an input are encoded in the following way:
 * {@literal @} + input name + JSON encoding of the mutations.
 *
 * @param <I>  the type of inputs
 */
public class TestParser<I> {



    /**
     * Writes test to file given the filename.
     *
     * @param test      the test to be written
     * @param filename  the name of the destination file
     *
     * @throws IOException  if an error during writing occurs
     */
    public void writeTest(Word<I> test, String filename) throws IOException {
        writeTest(test, new File(filename));
    }

    /**
     * Writes test to file.
     *
     * @param test  the test to be written
     * @param file  the destination file
     *
     * @throws IOException  if an error during writing occurs
     */
    public void writeTest(Word<I> test, File file) throws IOException {
        if (!file.createNewFile()) {
            throw new IOException("Unable to create file at specified path. It already exists");
        }
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (I input : test) {
                pw.println(input.toString());
            }
        }
    }

    /**
     * Reads a single test from file.
     *
     * @param inputBuilder  the symbol parser
     * @param filename      the name of the source file
     * @return              the test as a word of inputs
     *
     * @throws IOException  if an error during reading occurs
     */
    public Word<I> readTest(InputBuilder<I> inputBuilder, String filename) throws IOException {
        return readTest(inputBuilder, parseTestFile(filename));
    }

    /**
     * Reads a single test from a list of input strings.
     *
     * @param inputBuilder      the symbol parser
     * @param testInputStrings  the list containing input strings
     * @return                  the test as a word of inputs
     */
    public Word<I> readTest(InputBuilder<I> inputBuilder, List<String> testInputStrings) {
        Word<I> inputWord = Word.epsilon();
        for (String inputString : testInputStrings) {
            I input = inputBuilder.buildInput(inputString);
            inputWord = inputWord.append(input);
        }

        return inputWord;
    }

    /**
     * Reads reset-separated tests from file.
     * <p>
     * It stops reading once it reaches EOF, or an empty (or blank) line.
     * A non-empty line may contain:
     * <ul>
     * <li> reset - marking the end of the current test, and the beginning of a new test
     * <li> space-separated regular inputs and resets
     * <li> a single mutated input (starts with @)
     * <li> commented line (starts with # or !)
     * </ul>
     *
     * @param inputBuilder  the symbol parser
     * @param filename      the name of the source file
     * @return          	the tests as a list of words of inputs, where each word
     *                 	    is a test specified in the source file
     *
     * @throws IOException  if an error during reading occurs
     */
    public List<Word<I>> readTests(InputBuilder<I> inputBuilder, String filename) throws IOException {
        List<String> inputStrings = parseTestFile(filename);
        List<String> flattenedInputStrings = inputStrings.stream()
                .map(i -> i.startsWith("@") ? new String[]{i} : i.split("\\s+"))
                .flatMap(Arrays::stream)
                .toList();

        List<Word<I>> tests = new ArrayList<>();
        List<String> currentTestStrings = new ArrayList<>();
        for (String inputString : flattenedInputStrings) {
            if (inputString.equals("reset")) {
                tests.add(readTest(inputBuilder, currentTestStrings));
                currentTestStrings.clear();
            } else {
                currentTestStrings.add(inputString);
            }
        }
        if (!inputStrings.isEmpty()) {
            tests.add(readTest(inputBuilder, currentTestStrings));
        }
        return tests;
    }

    /**
     * Parses the tests of a file into a String List.
     * <p>
     * Commented lines (starting with # or !) are ignored and not included
     * in the result and the parsing stops at the first empty/blank line.
     *
     * @param filename  the name of the source file
     * @return          the parsed and non-ignored lines of the file
     *
     * @throws IOException  if an error during reading occurs
     */
    protected List<String> parseTestFile(String filename) throws IOException {
        String line;
        List<String> trace = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while ((line = bfr.readLine()) != null) {

                // skip commented lines
                if (line.startsWith("#") || line.startsWith("!")) {
                    continue;
                }

                // stop on first blank line (empty or whitespace only line)
                if (line.isBlank()) {
                    break;
                }

                trace.add(line + System.lineSeparator());
            }
        }

        return trace;
    }
}
