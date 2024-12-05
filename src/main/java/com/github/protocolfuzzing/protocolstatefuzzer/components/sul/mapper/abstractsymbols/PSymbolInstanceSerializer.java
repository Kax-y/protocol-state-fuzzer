package com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols;

import de.learnlib.ralib.data.DataType;
import de.learnlib.ralib.data.DataValue;
import de.learnlib.ralib.words.PSymbolInstance;
import de.learnlib.ralib.words.ParameterizedSymbol;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Serializes and deserializes {@link PSymbolInstance} instances used in RAs.
 */
@SuppressWarnings("unchecked")
public class PSymbolInstanceSerializer {

    public static <T> T valueOf(String serializedParameter, Class<T> cls) {
        if (cls == Integer.class)
            return cls.cast(Integer.valueOf(serializedParameter));
        else if (cls == Double.class)
            return cls.cast(Double.valueOf(serializedParameter));
        else if (cls == Long.class)
            return cls.cast(Long.valueOf(serializedParameter));

        throw new RuntimeException("Deserialization not supported for " + cls);
    }

    private Map<String, ParameterizedSymbol> symbolLookup;
    private Map<String, DataType> typeLookup;

    public PSymbolInstanceSerializer(ParameterizedSymbol [] symbols) {
        symbolLookup = new LinkedHashMap<>();
        typeLookup = new LinkedHashMap<>();
        for (ParameterizedSymbol inputSymbol : symbols) {
            symbolLookup.put(inputSymbol.getName(), inputSymbol);
            for (DataType type : inputSymbol.getPtypes()) {
                typeLookup.put(type.getName(), type);
            }
        }
    }

    public String serializeSymbolInstance(PSymbolInstance symbol) {
        StringBuilder sb = new StringBuilder();
        String methodName = symbol.getBaseSymbol().getName();
        sb.append(methodName);
        sb.append("{");

        for (DataValue<?> dv : symbol.getParameterValues()) {
            sb.append(dv.getType().getName()).append("=").append(dv.getId());
            sb.append(",");
        }

        if (symbol.getParameterValues().length > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("}");

        return sb.toString();
    }

    private ParameterizedSymbol getParameterizedSymbol(String symbolString) {
        ParameterizedSymbol symbol = symbolLookup.get(symbolString);
        if (symbol == null) {
            throw new RuntimeException("Undefined symbol: " + symbolString);
        }
        return symbol;
    }

    @SuppressWarnings("rawtypes")
    public PSymbolInstance deserializeSymbolInstance(String symbolString) {
        ParameterizedSymbol symbol = null;
        DataValue [] values;
        int startParam = symbolString.indexOf("[");
        if (startParam == -1) {
            symbol = getParameterizedSymbol(symbolString);
            values = new DataValue [] {};
        } else {
            String actionName = symbolString.substring(startParam);
            symbol = getParameterizedSymbol(actionName);
            if (!symbolString.endsWith("]")) {
                throw new RuntimeException("Symbol instance: " + symbolString);
            }
            String paramString = symbolString.substring(startParam+1, symbolString.length() - 1);
            values = parseParamValueStrings(symbolString, paramString);
        }

        if (values.length != symbol.getArity()) {
            throw new RuntimeException("Arrity mismatch in symbol instance " + symbolString);
        }

        PSymbolInstance symbolInstance = new PSymbolInstance(symbol, values);
        return symbolInstance;
    }


    @SuppressWarnings("rawtypes")
    private DataValue [] parseParamValueStrings(String symbol, String paramString) {
        if (paramString.isEmpty()) {
            return new DataValue [] {};
        }
        paramString = paramString.replaceAll("\\}\\,\\{", ",");
        if (!paramString.startsWith("{") || !paramString.endsWith("}")) {
            throw new RuntimeException("Missing begin and end brackets in parameterized symbol " + symbol);
        }

        if (paramString.substring(1, paramString.length()-1).isEmpty()) {
            return new DataValue [] {};
        }
        String[] paramValues = paramString.substring(1, paramString.length()-1).split("\\,");

        DataValue [] valList = new DataValue [paramValues.length];
        int i = 0;
        for (String paramValue : paramValues) {
            if (paramValue.isEmpty()) {
                continue;
            }
            String[] parts = paramValue.split("\\=");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid parameter encoding in output " + symbol);
            }
            String typeStr = parts[0];
            DataType type = typeLookup.get(typeStr);
            if (type == null) {
                throw new RuntimeException(String.format("Instance %s refers to unknown type %s", symbol, typeStr));
            }
            valList[i++] = new DataValue(type, valueOf(parts[1], type.getClass()));
        }

        return valList;
    }

    protected static int uLongString2Int(String num){
        return Long.valueOf(num).intValue();
    }
}
