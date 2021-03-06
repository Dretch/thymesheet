/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.dom.internal;

import java.util.Set;

import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;

/**
 * An abstract base class for <em>checkers</em> that traverses nodes.
 * 
 * @author Christer Sandberg
 */
public abstract class NodeTraversalChecker {
    
    /**
     * Check the specified nodes and return a new {@code Set} containing
     * the nodes that passed the check.
     * 
     * @param nodes The nodes to check.
     * @param root The root node.
     * @return A {@link Set} of nodes that passed the check.
     * @throws NodeSelectorException If an error occurred while performing the check.
     */
    public abstract Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException;
    
}
