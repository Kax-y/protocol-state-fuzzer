package com.github.protocolfuzzing.protocolstatefuzzer.utils;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.InputBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;
import net.automatalib.common.util.Pair;

/**
 * Implementation of Mealy Machine input and output pair processor.
 *
 * @param <I>  the type of inputs
 * @param <O>  the type of outputs
 *
 */
public class MealyIOProcessor<I, O> implements MealyDotParser.MealyInputOutputProcessor<I, O> {

    /** Stores the constructor parameter. */
    protected OutputBuilder<O> outputBuilder;

    protected InputBuilder<I> inputBuilder;

    /**
     * Constructs a new instance from the given parameter.
     *
     * @param inputBuilder   the builder for input symbols
     * @param outputBuilder  the builder for output symbols
     */
    public MealyIOProcessor(InputBuilder<I> inputBuilder, OutputBuilder<O> outputBuilder) {
        this.inputBuilder = inputBuilder;
        this.outputBuilder = outputBuilder;
    }

    @Override
    public Pair<I, O> processMealyInputOutput(String inputName, String outputName) {
        String inputNameCleaned = inputName.trim();
        I input = inputBuilder.buildInput(inputNameCleaned);
        String outputNameCleaned = outputName.trim();
        O output = outputBuilder.buildOutput(outputNameCleaned);

        return Pair.of(input, output);
    }
}
