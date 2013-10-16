/**
 * Copyright (c) 2009-2012, Christer Sandberg
 */
package com.connect_group.thymesheet.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.dom.DOMHelper;
import com.connect_group.thymesheet.css.selectors.specifier.PseudoClassSpecifier;
import com.connect_group.thymesheet.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node matches
 * the {@linkplain PseudoClassSpecifier pseudo-class specifier} set.
 * 
 * @author Christer Sandberg
 */
public class PseudoClassSpecifierChecker extends NodeTraversalChecker {
    
    /** The pseudo-class specifier to check against. */
    private final PseudoClassSpecifier specifier;
    
    /** The set of nodes to check. */
    private Set<Node> nodes;

    /** The root node. */
    private Node root;
    
    /** The result of the checks. */
    private Set<Node> result;
    
    /**
     * Create a new instance.
     * 
     * @param specifier The pseudo-class specifier to check against.
     */
    public PseudoClassSpecifierChecker(PseudoClassSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        Assert.notNull(root, "root is null!");
        this.nodes = nodes;
        this.root = root;
        result = new LinkedHashSet<Node>();
        String value = specifier.getValue();
        if ("empty".equals(value)) {
            addEmptyElements();
        } else if ("first-child".equals(value)) {
            addFirstChildElements();
        } else if ("first-of-type".equals(value)) {
            addFirstOfType();
        } else if ("last-child".equals(value)) {
            addLastChildElements();
        } else if ("last-of-type".equals(value)) {
            addLastOfType();
        } else if ("only-child".equals(value)) {
            addOnlyChildElements();
        } else if ("only-of-type".equals(value)) {
            addOnlyOfTypeElements();
        } else if ("root".equals(value)) {
            addRootElement();
        } else {
            throw new NodeSelectorException("Unknown pseudo class: " + value);
        }
        
        return result;
    }

    /**
     * Add {@code :empty} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#empty-pseudo"><code>:empty</code> pseudo-class</a>
     */
    private void addEmptyElements() {
        for (Node node : nodes) {
            boolean empty = true;
            if(node instanceof NestableNode) {
	            List<Node> nl = ((NestableNode) node).getChildren();
	            for (Node n : nl) {
	                if (n instanceof Element) {
	                    empty = false;
	                    break;
	                } else if (n instanceof Text) {
	                    // TODO: Should we trim the text and see if it's length 0?
	                    String value = ((Text) n).getContent();
	                    if (value.length() > 0) {
	                        empty = false;
	                        break;
	                    }
	                }
	            }
            }            
            if (empty) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :first-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#first-child-pseudo"><code>:first-child</code> pseudo-class</a>
     */
    private void addFirstChildElements() {
        for (Node node : nodes) {
            if (DOMHelper.getPreviousSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :first-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#first-of-type-pseudo"><code>:first-of-type</code> pseudo-class</a>
     */
    private void addFirstOfType() {
        for (Node node : nodes) {
            Node n = DOMHelper.getPreviousSiblingElement(node);
            while (n != null) {
                if (DOMHelper.getNodeName(n).equals(DOMHelper.getNodeName(node))) {
                    break;
                }
                
                n = DOMHelper.getPreviousSiblingElement(n);
            }
            
            if (n == null) {
                result.add(node);
            }
        }
    }

    /**
     * Add {@code :last-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#last-child-pseudo"><code>:last-child</code> pseudo-class</a>
     */
    private void addLastChildElements() {
        for (Node node : nodes) {
            if (DOMHelper.getNextSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :last-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#last-of-type-pseudo"><code>:last-of-type</code> pseudo-class</a>
     */
    private void addLastOfType() {
        for (Node node : nodes) {
            Node n = DOMHelper.getNextSiblingElement(node);
            while (n != null) {
                if (DOMHelper.getNodeName(n).equals(DOMHelper.getNodeName(node))) {
                    break;
                }
                
                n = DOMHelper.getNextSiblingElement(n);
            }
            
            if (n == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :only-child} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#only-child-pseudo"><code>:only-child</code> pseudo-class</a>
     */
    private void addOnlyChildElements() {
        for (Node node : nodes) {
            if (DOMHelper.getPreviousSiblingElement(node) == null &&
                    DOMHelper.getNextSiblingElement(node) == null) {
                result.add(node);
            }
        }
    }
    
    /**
     * Add {@code :only-of-type} elements.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#only-of-type-pseudo"><code>:only-of-type</code> pseudo-class</a>
     */
    private void addOnlyOfTypeElements() {
        for (Node node : nodes) {
            Node n = DOMHelper.getPreviousSiblingElement(node);
            while (n != null) {
                if (DOMHelper.getNodeName(n).equals(DOMHelper.getNodeName(node))) {
                    break;
                }
                
                n = DOMHelper.getPreviousSiblingElement(n);
            }
            
            if (n == null) {
                n = DOMHelper.getNextSiblingElement(node);
                while (n != null) {
                    if (DOMHelper.getNodeName(n).equals(DOMHelper.getNodeName(node))) {
                        break;
                    }
                    
                    n = DOMHelper.getNextSiblingElement(n);
                }
                
                if (n == null) {
                    result.add(node);
                }
            }
        }
    }

    /**
     * Add the {@code :root} element.
     * 
     * @see <a href="http://www.w3.org/TR/css3-selectors/#root-pseudo"><code>:root</code> pseudo-class</a>
     */
    private void addRootElement() {
        if (root instanceof Document) {
            // Get the single element child of the document node.
            // There could be a doctype node and comment nodes that we must skip.
            Element element = DOMHelper.getFirstChildElement(root);
            Assert.notNull(element, "there should be a root element!");
            result.add(element);
        } else {
            Assert.isTrue(root instanceof Element, "root must be a document or element node!");
            result.add(root);
        }
    }
    
}
