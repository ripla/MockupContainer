package org.vaadin.risto.mockupcontainer;

/**
 * Interface for classes that wish to act as data sources for the
 * MockupContainer
 * 
 * @author Risto Yrjänä / Vaadin Ltd.
 * 
 */
public interface MockupDataSet {

    /**
     * <p>
     * Retrieve the next String that can be used as a property id.
     * </p>
     * 
     * <p>
     * <b>Make sure that the id's are "unique enough".</b> This means, that
     * while every id doesn't have to be unique, the the amount of unique id's
     * must always match or exceed the amount of properties/items.
     * </p>
     * 
     * @return
     */
    public String nextId();

    /**
     * Retrieve the next String that can be used as a property value
     * 
     * @return
     */
    public String nextValue();
}
