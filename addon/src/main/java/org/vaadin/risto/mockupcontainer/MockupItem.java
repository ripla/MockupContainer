/**
 * 
 */
package org.vaadin.risto.mockupcontainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * Implementation of the {@link com.vaadin.data.Item} interface for the
 * {@link org.vaadin.risto.mockupcontainer.MockupContainer MockupContainer}.
 * Methods that modify the properties of this item are not supported.
 * 
 * MockupItem is meant to be used with String values from a
 * {@link MockupDataSet}
 * 
 * @author Risto Yrjänä / Vaadin
 * 
 */
public class MockupItem implements Item {

    private static final long serialVersionUID = -8932204044098491445L;
    private final String[] itemPropertyArray;
    private final Map<String, MockupProperty<String>> propertyMap;
    private Integer parent;
    private Collection<Integer> children;
    private final MockupDataSet dataSet;

    protected MockupItem(String[] propertyArray, MockupDataSet dataSet) {
        this.itemPropertyArray = propertyArray;
        this.dataSet = dataSet;
        propertyMap = new HashMap<String, MockupProperty<String>>();
        setChildren(new LinkedList<Integer>());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean addItemProperty(Object id, Property property)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Property<String> getItemProperty(Object id) {
        if (id == null || !(id instanceof String)) {
            throw new IllegalArgumentException(
                    "MockupItem property id's must be of type String");
        }

        MockupProperty<String> property = null;
        if (!propertyMap.containsKey(id)) {
            property = new MockupProperty<String>(dataSet.nextValue(),
                    String.class);
            propertyMap.put((String) id, property);
        } else {
            property = propertyMap.get(id);
        }

        return property;
    }

    @Override
    public Collection<String> getItemPropertyIds() {
        return Collections.unmodifiableCollection(Arrays
                .asList(itemPropertyArray));
    }

    @Override
    public boolean removeItemProperty(Object id)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    protected Collection<Integer> getChildren() {
        return children;
    }

    protected void setChildren(Collection<Integer> children) {
        this.children = children;
    }

    protected boolean isRoot() {
        return getParent() == null;
    }

    protected Integer getParent() {
        return parent;
    }

    protected void setParent(Integer id) {
        this.parent = id;
    }

}
