package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputChecker;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConfig;

/**
 * Interface for the Mapper Component, which is responsible for executing an input.
 * <p>
 * Given an input symbol, the mapper should:
 * <ol>
 * <li> convert the input symbol to a protocol message
 * <li> send the protocol message to the SUL
 * <li> receive the protocol message response of the SUL
 * <li> convert the protocol message response to an output symbol
 * </ol>
 *
 * @param <I>  the type of inputs
 * @param <O>  the type of outputs
 * @param <E>  the type of execution context
 */
public interface Mapper<I, O, E> {

    /**
     * Executes an input and returns the corresponding output.
     *
     * @param input    the input symbol to be executed
     * @param context  the active execution context
     * @return         the corresponding output symbol
     */
    O execute(I input, E context);

    /**
     * Returns the configuration of the Mapper.
     *
     * @return  the configuration of the Mapper
     */
    MapperConfig getMapperConfig();

    /**
     * Returns the instance that builds the output symbols.
     *
     * @return  the instance that builds the output symbols
     */
    OutputBuilder<O> getOutputBuilder();

    /**
     * Returns the instance that checks the output symbols.
     *
     * @return  the instance that checks the output symbols
     */
    OutputChecker<O> getOutputChecker();
}
