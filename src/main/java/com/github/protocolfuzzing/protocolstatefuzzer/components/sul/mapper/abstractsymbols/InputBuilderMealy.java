package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols;

import net.automatalib.alphabet.Alphabet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Input builder for a standard (non-parametric) input alphabet, which assumes inputs are serialized via {@link java.lang.Object#toString()}.
 *
 * @param <I> input symbol class
 */
public class InputBuilderMealy<I> implements InputBuilder<I> {

    /**
     * Maps strings to inputs.
     */
    private Map<String, I> inputMap;

    public InputBuilderMealy(Alphabet<I> alphabet) {
        inputMap = new LinkedHashMap<>();
        alphabet.forEach(i -> inputMap.put(i.toString(), i));
    }

    /**
     * Builds an input symbol given its name.
     *
     * @param name  the name of the input symbol
     * @return      the input symbol
     */
    @Override
    public I buildInput(String inputString) {
        if (!inputMap.containsKey(inputString)) {
            throw new RuntimeException("Input \"" + inputString + "\" is missing from the alphabet");
        }
        return inputMap.get(inputString);
    }

}
