package io.github.haiphamcoder.trino.client.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a column in a Trino query result set.
 * This class contains column metadata including name, type, and detailed type
 * signature
 * information for complex types.
 * 
 * @author Hai Pham Ngoc
 * @version 1.0.0
 */
public class TrinoColumn {
    /** Name of the column */
    private String name;
    /** Simple type name of the column (e.g., "varchar", "bigint", "array") */
    private String type;
    /** Detailed type signature for complex types including nested structures */
    private TypeSignature typeSignature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TypeSignature getTypeSignature() {
        return typeSignature;
    }

    public void setTypeSignature(TypeSignature typeSignature) {
        this.typeSignature = typeSignature;
    }

    /**
     * Represents the type signature of a column, including nested and complex
     * types.
     * This class provides detailed information about parameterized types like
     * arrays,
     * maps, and user-defined types.
     */
    public static class TypeSignature {
        /** The raw/base type name (e.g., "array", "map", "varchar") */
        private String rawType;
        /** List of type arguments for parameterized types */
        private List<TypeArgument> arguments;

        public String getRawType() {
            return rawType;
        }

        public void setRawType(String rawType) {
            this.rawType = rawType;
        }

        public List<TypeArgument> getArguments() {
            return arguments;
        }

        public void setArguments(List<TypeArgument> arguments) {
            this.arguments = arguments;
        }
    }

    /**
     * Represents an argument to a parameterized type.
     * Used to define nested types within complex types like array&lt;varchar&gt; or
     * map&lt;string, int&gt;.
     */
    public static class TypeArgument {
        /** The kind of argument (e.g., "TYPE", "NAMED_TYPE") */
        private String kind;
        /** The type signature value for this argument */
        private TypeSignature value;

        /** The type signature representation */
        @SerializedName("typeSignature")
        private TypeSignature typeSignature;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public TypeSignature getValue() {
            return value;
        }

        public void setValue(TypeSignature value) {
            this.value = value;
        }

        public TypeSignature getTypeSignature() {
            return typeSignature;
        }

        public void setTypeSignature(TypeSignature typeSignature) {
            this.typeSignature = typeSignature;
        }
    }
}
