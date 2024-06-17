package com.github.protocolfuzzing.protocolstatefuzzer.components.learner.oracles;

/**
 * Exception used by the {@link ObservationTree}.
 */
public class RemovalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance from the given parameters.
     *
     * @param msg  the message related to the exception
     */
    public RemovalException(String msg) {
        super(msg);
    }
}
