/**
 * Copyright (c) 2009-2012, Christer Sandberg
 */
package com.connect_group.thymesheet.css.selectors;

/**
 * This interface represents a specifier. A specifier is either an
 * attribute specifier, a pseudo specifier or a negation specifier.
 * 
 * @author Christer Sandberg
 */
public interface Specifier {

    /** The specifier type. */
    public static enum Type {
        ATTRIBUTE, PSEUDO, NEGATION
    };

    /**
     * Get the specifier type.
     * 
     * @return The specifier type.
     */
    public Type getType();

}
