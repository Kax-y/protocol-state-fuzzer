package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols;

/**
 * The interface needed by the input symbols of the Mapper.
 *
 * @param <O>  the type of outputs
 * @param <P>  the type of protocol messages
 * @param <E>  the type of execution context
 */
public interface MapperInput<O, P, E> {

    /**
     * Returns the name of the input.
     *
     * @return  the name of the input
     */
    String getName();

    /**
     * Returns {@code true} if this input symbol is enabled for execution.
     *
     * @param context  the active execution context
     * @return         {@code true} if this input symbol is enabled for execution
     */
    default boolean isEnabled(E context) {
        return true;
    }

    /**
     * Returns the extended wait time needed for this input symbol.
     *
     * @return  the extended wait time needed for this input symbol
     */
    Long getExtendedWait();

    /**
     * Sets the extended wait time needed for this input symbol.
     *
     * @param extendedWait  the additional waiting time before sending this input symbol
     */
    void setExtendedWait(Long extendedWait);

    /**
     * Updates the context before sending the input and before calling
     * {@link generateProtocolMessage  generateProtocolMessage}.
     *
     * @param context  the active execution context
     */
    abstract void preSendUpdate(E context);

    /**
     * Generates the corresponding concrete symbol (aka protocol message)
     * of the current abstract input symbol, providing this way the
     * functionality of abstract-to-concrete Mapper.
     *
     * @param context  the active execution context
     * @return         the corresponding protocol message
     */
    abstract P generateProtocolMessage(E context);

    /**
     * Updates the context after sending the input.
     *
     * @param context  the active execution context
     */
    abstract void postSendUpdate(E context);

    /**
     * Updates the context after receiving an output.
     *
     * @param output         the output obtained as the response
     * @param outputChecker  the output checker to check the output if needed
     * @param context        the active execution context
     */
    abstract void postReceiveUpdate(
        O output, OutputChecker<O> outputChecker, E context);
}
