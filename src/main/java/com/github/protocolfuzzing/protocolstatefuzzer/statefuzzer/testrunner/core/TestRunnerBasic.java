package com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.testrunner.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.AbstractSul;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.SulBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.SulWrapper;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.InputBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.testrunner.core.config.TestRunnerEnabler;
import com.github.protocolfuzzing.protocolstatefuzzer.utils.CleanupTasks;

import de.learnlib.oracle.MembershipOracle.MealyMembershipOracle;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.word.Word;
/**
 * Minimal implementation of the TestRunner Interface.
 *
 * @param <I>  the type of inputs
 * @param <O>  the type of outputs
 * @param <E>  the type of execution context
 */
public class TestRunnerBasic<I, O, E> implements TestRunner {
	private static final Logger LOGGER = LogManager.getLogger();

    /** Stores the constructor parameter. */
    protected TestRunnerEnabler testRunnerEnabler;

    /** The Oracle that contains the sul built via SulBuilder and wrapped via SulWrapper constructor parameters. */
    protected MealyMembershipOracle<I, O> sulOracle;

    /** Stores the cleanup tasks of the TestRunner. */
    protected CleanupTasks cleanupTasks;

    /** Builds input symbols. */
    protected InputBuilder<I> inputBuilder;

    /**
     * Constructs a new instance from the given parameters.
     * <p>
     * The {@link #sulOracle} contains the wrapped (and built) sul.
     * Invoke {@link #initialize()} afterwards.
     *
     * @param testRunnerEnabler        the configuration that enables the testing
     * @param inputBuilder             the builder of input symbols
     * @param sulBuilder               the builder of the sul
     * @param sulWrapper               the wrapper of the sul
     */
    public TestRunnerBasic(
        TestRunnerEnabler testRunnerEnabler,
        InputBuilder<I> inputBuilder,
        SulBuilder<I, O, E> sulBuilder,
        SulWrapper<I, O, E> sulWrapper
    ) {
        this.testRunnerEnabler = testRunnerEnabler;
        this.inputBuilder = inputBuilder;
        this.cleanupTasks = new CleanupTasks();

        AbstractSul<I, O, E> abstractSul = sulBuilder.build(testRunnerEnabler.getSulConfig(), cleanupTasks);
        this.sulOracle = new SULOracle<>(sulWrapper.wrap(abstractSul).getWrappedSul());
    }

    /**
     * Returns the SulConfig of the {@link #testRunnerEnabler}.
     *
     * @return  the SulConfig of the {@link #testRunnerEnabler}
     */
    public SulConfig getSulConfig() {
        return testRunnerEnabler.getSulConfig();
    }

    /**
     * Runs the tests using {@link #runTests()} and cleans up using {@link #terminate()}.
     */
    @Override
    public void run() {
        try {
            List<TestRunnerResult<Word<I>, Word<O>>> results = runTests();

            for (TestRunnerResult<Word<I>, Word<O>> result : results) {
                LOGGER.info(result.toString());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            terminate();
        }
    }

    /**
     * Executes the {@link #cleanupTasks}; should be called only after all the
     * desired tests have been executed.
     */
    public void terminate() {
        cleanupTasks.execute();
    }

    /**
     * Reads the tests provided in the TestRunnerConfig of {@link #testRunnerEnabler},
     * executes each one of them using {@link #runTest(Word)} and collects the results.
     *
     * @return  a list with the test results
     *
     * @throws IOException  if an error during reading occurs
     */
    protected List<TestRunnerResult<Word<I>, Word<O>>> runTests() throws IOException {
        TestParser<I> testParser = new TestParser<>();
        List<Word<I>> tests;
        String testFileOrTestString = testRunnerEnabler.getTestRunnerConfig().getTest();

        if (new File(testFileOrTestString).exists()) {
            tests = testParser.readTests(inputBuilder, testFileOrTestString);
        } else {
            LOGGER.info("File {} does not exist, interpreting argument as test", testFileOrTestString);
            String[] testStrings = testFileOrTestString.split("\\s+");
            tests = List.of(testParser.readTest(inputBuilder, Arrays.asList(testStrings)));
        }

        List<TestRunnerResult<Word<I>, Word<O>>> results = new ArrayList<>();
        for (Word<I> test : tests) {
            results.add(runTest(test));
        }
        return results;
    }

    /**
     * Runs a single test and collects the result.
     * <p>
     *
     * @param test  the test to be run against the stored {@link #sulOracle}
     * @return      the result of the test
     */
    protected TestRunnerResult<Word<I>, Word<O>> runTest(Word<I> test) {
        TestRunnerResult<Word<I>, Word<O>> result = TestRunner.runTest(test,
                testRunnerEnabler.getTestRunnerConfig().getTimes(), sulOracle);
        return result;
    }

}
