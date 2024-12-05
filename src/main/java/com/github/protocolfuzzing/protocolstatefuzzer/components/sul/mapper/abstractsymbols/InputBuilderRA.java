package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols;

import de.learnlib.ralib.words.PSymbolInstance;
import de.learnlib.ralib.words.ParameterizedSymbol;

/**
 * Builder of PSymbolInstances.
 */
public class InputBuilderRA implements InputBuilder<PSymbolInstance> {

    private PSymbolInstanceSerializer serializer;

    public InputBuilderRA(ParameterizedSymbol [] symbols) {
        serializer = new PSymbolInstanceSerializer(symbols);
    }

    @Override
    public PSymbolInstance buildInput(String inputString) {
        return serializer.deserializeSymbolInstance(inputString);
    }

}
