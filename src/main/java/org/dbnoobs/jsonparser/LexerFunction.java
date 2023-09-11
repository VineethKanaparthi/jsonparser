package org.dbnoobs.jsonparser;

@FunctionalInterface
public interface LexerFunction {
    JsonToken apply(String input, Cursor cursor);
}
