package com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core;

import com.github.protocolfuzzing.protocolstatefuzzer.components.learner.LearnerResult;

/**
 * Interface for the state fuzzing process.
 *
 * @param <M>  the type of machine model
 */
public interface StateFuzzer<M> {

    /** The filename, where the learned model will be stored. */
    String LEARNED_MODEL_FILENAME = "learnedModel.dot";

    /** The filename, where the statistics will be stored. */
    String STATISTICS_FILENAME = "statistics.txt";

    /** The filename, where the mapper connection configuration will be stored. */
    String MAPPER_CONNECTION_CONFIG_FILENAME = "mapper_connection.config";

    /** The filename, without the extension, where the alphabet will be stored. */
    String ALPHABET_FILENAME_NO_EXTENSION = "alphabet";

    /** The filename, where the error will be stored, if it occurs. */
    String ERROR_FILENAME = "error.msg";

    /** The filename, where the state status will be stored. */
    String LEARNING_STATE_FILENAME = "state.log";

    /**
     * Used to start the state fuzzing process.
     *
     * @return  the corresponding LearnerResult, which can be empty
     */
    LearnerResult<M> startFuzzing();
}
