package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols;

/**
 * Interface for building input symbols.
 *
 * @param <I>  the type of inputs
 */
public interface InputBuilder<I> {
    /**
     * Builds an input symbol given its string representation.
     *
     * @param   inputString  the string representation of the input symbol
     * @return               the input symbol
     */
    I buildInput(String inputString);
}
