/**
 * 
 */
package org.vaadin.risto.mockupcontainer;

/**
 * Factory methods for for some common MockupContainer use cases. All the
 * properties that are generated are of type String, unless otherwise noted.
 * 
 * @author Risto Yrjänä / Vaadin Ltd.
 * 
 */
public class MockupFactory {

    private static MockupDataSet defaultDataSet;

    private MockupFactory() {
        // for static use only
    }

    /**
     * @return a property of type boolean, with the value true
     */
    public static MockupProperty<Boolean> getTrueBooleanProperty() {
        return new MockupProperty<Boolean>(Boolean.TRUE, Boolean.class);
    }

    /**
     * @return a property of type boolean, with the value false
     */
    public static MockupProperty<Boolean> getFalseBooleanProperty() {
        return new MockupProperty<Boolean>(Boolean.FALSE, Boolean.class);
    }

    /**
     * @return a string property with a random value
     */
    public static MockupProperty<String> getStringProperty() {
        return getStringProperty(getDataSetOrDefault().nextValue());
    }

    public static MockupProperty<String> getStringProperty(String defaultValue) {
        return new MockupProperty<String>(defaultValue, String.class);
    }

    public static MockupItem getOnePropertyItem() {
        return getPropertyItem(1);
    }

    public static MockupItem getFivePropertyItem() {
        return getPropertyItem(5);
    }

    public static MockupItem getTwentyPropertyItem() {
        return getPropertyItem(20);
    }

    public static MockupItem getPropertyItem(int amountOfProperties) {
        MockupDataSet dataSet = getDataSetOrDefault();
        String[] properties = getPropertyArray(amountOfProperties, dataSet);

        return new MockupItem(properties, dataSet);
    }

    /**
     * @return a MockupContainer with the default values of 20 items, 5
     *         properties and 5 children
     */
    public static MockupContainer getDefaultContainer() {
        MockupContainer container = new MockupContainer();
        container.setDataSet(getDataSetOrDefault());
        return container;
    }

    /**
     * @return a MockupContainer with default size, and an item retrieval delay
     *         of 100ms
     */
    public static MockupContainer getSlowDefaultContainer() {
        MockupContainer container = new MockupContainer();
        container.setItemDelay(100);

        return container;
    }

    /**
     * @return a medium-sized MockupContainer, with 1000 items that have 10
     *         properties and 10 children each
     */
    public static MockupContainer getMediumContainer() {
        MockupContainer container = new MockupContainer(1000, 10, 5);
        container.setDataSet(getDataSetOrDefault());
        return container;
    }

    /**
     * @return a large MockupContainer, with 20000 items that have 20 properties
     *         and 10 children each
     */
    public static MockupContainer getLargeContainer() {
        MockupContainer container = new MockupContainer(20000, 20, 10);
        container.setDataSet(getDataSetOrDefault());
        return container;
    }

    /**
     * @return a gigantic MockupContainer, with 500000 items that have 30
     *         properties and 20 children each
     */
    public static MockupContainer getGiganticContainer() {
        MockupContainer container = new MockupContainer(500000, 30, 20);
        container.setDataSet(getDataSetOrDefault());
        return container;
    }

    /**
     * @param size
     *            size of the created array
     * @return an array filled with id values from the dataset
     */
    private static String[] getPropertyArray(int size, MockupDataSet dataSet) {
        String[] properties = new String[size];
        for (int i = 0; i < size; i++) {
            properties[i] = dataSet.nextId();
        }

        return properties;
    }

    /**
     * @return singleton instance of the default MockupDataSet
     */
    private static MockupDataSet getDataSetOrDefault() {
        if (getDefaultDataSet() == null) {
            setDefaultDataSet(new DefaultDataSet());
        }

        return getDefaultDataSet();
    }

    public static void setDefaultDataSet(MockupDataSet defaultDataSet) {
        MockupFactory.defaultDataSet = defaultDataSet;
    }

    public static MockupDataSet getDefaultDataSet() {
        return defaultDataSet;
    }
}
