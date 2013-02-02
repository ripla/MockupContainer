package org.vaadin.risto.mockupcontainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.ContainerHelpers;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * <p>
 * MockupContainer is a container that contains dummy data. MockupContainer
 * implements both Container.Indexed and Container.Hierarchical, making it
 * suitable for most of UI components.
 * </p>
 * 
 * <p>
 * The usage of MockupContainer can be customised in many ways. The item count,
 * property count and the number of children per item can be dynamically
 * changed. The children per item setting also affects the number of items in
 * the root level.
 * </p>
 * 
 * <p>
 * The property values come from a
 * {@link org.vaadin.risto.mockupcontainer.MockupDataSet MockupDataSet}. The
 * default implementation
 * {@link org.vaadin.risto.mockupcontainer.DefaultDataSet DefaultDataSet}
 * contains random "Lorem ipsum" data.
 * </p>
 * 
 * <p>
 * The user can also set a delay for the retrieval of an item, simulating for
 * example a slow DB connection. The given delay is imposed on <i>every</i>
 * {@link #getItem(Object)} operation, so small values should be used.
 * </p>
 * 
 * @author Risto Yrjänä / Vaadin
 * 
 */
public class MockupContainer implements Container, Container.Indexed,
        Container.Hierarchical, Container.ItemSetChangeNotifier,
        Container.PropertySetChangeNotifier {

    private static final long serialVersionUID = 7609410632305455153L;

    private int propertyCount;

    private String[] containerPropertyArray;

    private final LinkedHashMap<Integer, MockupItem> itemMap;

    private int itemCount;

    private int numberOfChildren;

    private MockupDataSet dataSet;

    private int getItemDelay;

    private List<ItemSetChangeListener> itemSetChangeListeners;

    private List<PropertySetChangeListener> propertySetChangeListeners;

    /**
     * Default constructor. By default the MockupContainer contains 20 items,
     * each having 5 properties and 5 children. The default data set returns
     * Strings from the "Lorem ipsum".
     */
    public MockupContainer() {
        this(20, 5, 5, new DefaultDataSet());
    }

    public MockupContainer(int itemCount) {
        this(itemCount, 5, 10, new DefaultDataSet());
    }

    public MockupContainer(int itemCount, int propertyCount,
            int numberOfChildren) {
        this(itemCount, propertyCount, numberOfChildren, new DefaultDataSet());
    }

    /**
     * 
     * @param itemCount
     * @param propertyCount
     * @param numberOfChildren
     * @param dataSet
     */
    public MockupContainer(int itemCount, int propertyCount,
            int numberOfChildren, MockupDataSet dataSet) {
        if (dataSet == null) {
            throw new IllegalArgumentException("Dataset cannot be null");
        }

        this.itemCount = itemCount;
        this.propertyCount = propertyCount;
        this.numberOfChildren = numberOfChildren;
        itemMap = new LinkedHashMap<Integer, MockupItem>(itemCount);
        this.setDataSet(dataSet);

        generateProperties();
        generateItemIds();
        generateItems();
    }

    protected void generateItemIds() {
        itemMap.clear();
        for (int i = 0; i < itemCount; i++) {
            itemMap.put(Integer.valueOf(i), null);
        }
    }

    protected void generateProperties() {
        List<String> containerProperties = new ArrayList<String>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            String nextId = null;
            do {
                nextId = getDataSet().nextId();
            } while (containerProperties.contains(nextId));
            containerProperties.add(nextId);
        }

        containerPropertyArray = containerProperties
                .toArray(new String[propertyCount]);
        Arrays.sort(containerPropertyArray);
    }

    protected void generateItems() {
        int lastUnfullParent = 0;
        for (int i = 0; i < itemCount; i++) {

            MockupItem item = new MockupItem(containerPropertyArray,
                    getDataSet());
            itemMap.put(i, item);

            // set item parent properties
            if (i < numberOfChildren) {
                item.setParent(null); // root level
            } else {
                MockupItem parent = itemMap.get(lastUnfullParent);
                parent.getChildren().add(i);
                item.setParent(lastUnfullParent);

                if (parent.getChildren().size() == numberOfChildren) {
                    lastUnfullParent++;
                }
            }
        }
    }

    protected void rebuildContents() {
        generateProperties();
        firePropertySetChangeEvent();

        generateItemIds();
        generateItems();
        fireItemSetChangeEvent();
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean containsId(Object itemId) {
        checkItemIdType(itemId);

        return itemIdIsPossible((Integer) itemId);
    }

    @Override
    public Property<?> getContainerProperty(Object itemId, Object propertyId) {
        if (propertyId instanceof String
                && Arrays.binarySearch(containerPropertyArray, propertyId) < 0) {
            return null;
        }

        return getItem(itemId).getItemProperty(propertyId);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        return Collections.unmodifiableCollection(Arrays
                .asList(containerPropertyArray));
    }

    @Override
    public Item getItem(Object itemId) {
        if (itemId == null) {
            return null;
        }

        try {
            checkItemIdType(itemId);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (itemIdIsPossible((Integer) itemId)) {

            if (getGetItemDelay() > 0) {
                try {
                    Thread.sleep(getGetItemDelay());
                } catch (InterruptedException e) {
                    // ignored
                }
            }
            return internalGetItem(itemId);

        } else {
            return null;
        }
    }

    /**
     * This method doesn't have the set delay.
     * 
     * @param itemId
     * @return
     */
    protected Item internalGetItem(Object itemId) {
        Item item = itemMap.get(itemId);
        return item;
    }

    protected void checkItemIdType(Object itemId) {
        if (itemId == null || !(itemId instanceof Integer)) {
            throw new IllegalArgumentException(
                    "Item id's must be non-null and of type integer");
        }
    }

    protected boolean itemIdIsPossible(Integer itemId) {
        return ((itemId >= 0 && itemId < itemCount));
    }

    @Override
    public Collection<Integer> getItemIds() {
        return Collections.unmodifiableSet(itemMap.keySet());
    }

    @Override
    public List<?> getItemIds(int startIndex, int numberOfItems) {
        return ContainerHelpers.getItemIdsUsingGetIdByIndex(startIndex,
                numberOfItems, this);
    }

    @Override
    public Class<String> getType(Object propertyId) {
        return String.class;
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return itemCount;
    }

    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAt(int index, Object newItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getIdByIndex(int index) {
        if (index < 0 || index >= itemCount) {
            throw new IllegalArgumentException("Illegal index");
        }
        return Integer.valueOf(index);
    }

    @Override
    public int indexOfId(Object itemId) {
        checkItemIdType(itemId);
        itemIdIsPossible((Integer) itemId);

        return ((Integer) itemId).intValue();
    }

    @Override
    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object firstItemId() {
        return itemMap.get(0);
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return firstItemId().equals(itemId);
    }

    @Override
    public boolean isLastId(Object itemId) {
        return lastItemId().equals(itemId);
    }

    @Override
    public Object lastItemId() {
        return itemMap.get(itemCount - 1);
    }

    @Override
    public Object nextItemId(Object itemId) {
        checkItemIdType(itemId);
        itemIdIsPossible((Integer) itemId);

        return itemMap.get(((Integer) itemId) + 1);
    }

    @Override
    public Object prevItemId(Object itemId) {
        checkItemIdType(itemId);
        itemIdIsPossible((Integer) itemId);

        return itemMap.get(((Integer) itemId) - 1);
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        checkItemIdType(itemId);
        return itemIdIsPossible((Integer) itemId);
    }

    @Override
    public Collection<Integer> getChildren(Object itemId) {
        checkItemIdType(itemId);
        if (itemIdIsPossible((Integer) itemId)) {
            return ((MockupItem) getItem(itemId)).getChildren();
        } else {
            return null;
        }
    }

    @Override
    public Object getParent(Object itemId) {
        checkItemIdType(itemId);
        if (itemIdIsPossible((Integer) itemId)) {
            return ((MockupItem) getItem(itemId)).getParent();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasChildren(Object itemId) {
        checkItemIdType(itemId);
        if (itemIdIsPossible((Integer) itemId)) {
            return !((MockupItem) getItem(itemId)).getChildren().isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public boolean isRoot(Object itemId) {
        checkItemIdType(itemId);
        if (itemIdIsPossible((Integer) itemId)) {
            return ((MockupItem) getItem(itemId)).isRoot();
        } else {
            return false;
        }
    }

    @Override
    public Collection<Integer> rootItemIds() {
        ArrayList<Integer> rootIds = new ArrayList<Integer>(numberOfChildren);
        for (int i = 0; i < numberOfChildren; i++) {
            rootIds.add(i);
        }

        return rootIds;
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Set a delay (milliseconds) for all the get item operations. Can be used
     * to simulate slow containers.
     * 
     * @param getItemDelay
     */
    public void setItemDelay(int getItemDelay) {
        this.getItemDelay = getItemDelay;
    }

    public int getGetItemDelay() {
        return this.getItemDelay;
    }

    /**
     * Set the number of items. Setting this effectively rebuilds the whole
     * container.
     * 
     * @param itemCount
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        rebuildContents();
    }

    public int getItemCount() {
        return this.itemCount;
    }

    /**
     * Set the number of properties each item should have. Setting this
     * effectively rebuilds the whole container.
     * 
     * @param propertyCount
     */
    public void setPropertyCount(int propertyCount) {
        this.propertyCount = propertyCount;
        rebuildContents();
    }

    public int getPropertyCount() {
        return this.propertyCount;
    }

    /**
     * Set the number of children each item should have. Setting this
     * effectively rebuilds the whole container.
     * 
     * @param numberOfChildren
     */
    public void setNumberOfChildren(int numberOfChildren) {
        if (numberOfChildren > itemCount) {
            throw new IllegalArgumentException(
                    "Number of children per item cannot exceed the total number of items in the container.");
        }
        this.numberOfChildren = numberOfChildren;
        rebuildContents();
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    /**
     * Set a new {@link MockupDataSet} from which to generate data for this
     * container. Causes the container to regenerate all contents.
     * 
     * @param dataSet
     */
    public void setDataSet(MockupDataSet dataSet) {
        this.dataSet = dataSet;
        rebuildContents();
    }

    public MockupDataSet getDataSet() {
        return dataSet;
    }

    /* Listener methods */

    protected void fireItemSetChangeEvent() {
        if (itemSetChangeListeners != null) {
            for (ItemSetChangeListener listener : itemSetChangeListeners) {
                listener.containerItemSetChange(new ItemSetChangeEvent() {

                    private static final long serialVersionUID = -8976859511605227196L;

                    @Override
                    public Container getContainer() {
                        return MockupContainer.this;
                    }

                });
            }
        }
    }

    protected void firePropertySetChangeEvent() {
        if (propertySetChangeListeners != null) {

            for (PropertySetChangeListener listener : propertySetChangeListeners) {
                listener.containerPropertySetChange(new PropertySetChangeEvent() {

                    private static final long serialVersionUID = -2869862286777960682L;

                    @Override
                    public Container getContainer() {
                        return MockupContainer.this;
                    }
                });
            }
        }
    }

    @Override
    public void addPropertySetChangeListener(PropertySetChangeListener listener) {
        if (propertySetChangeListeners == null) {
            propertySetChangeListeners = new LinkedList<PropertySetChangeListener>();
        }
        propertySetChangeListeners.add(listener);
    }

    @Override
    public void removePropertySetChangeListener(
            PropertySetChangeListener listener) {
        if (propertySetChangeListeners == null) {
            return;
        }
        propertySetChangeListeners.remove(listener);
    }

    @Override
    public void addItemSetChangeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedList<ItemSetChangeListener>();
        }
        itemSetChangeListeners.add(listener);
    }

    @Override
    public void removeItemSetChangeListener(ItemSetChangeListener listener) {

        if (itemSetChangeListeners == null) {
            return;
        }
        itemSetChangeListeners.remove(listener);
    }

    @Override
    @Deprecated
    public void addListener(ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    @Override
    @Deprecated
    public void removeListener(ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }

    @Override
    @Deprecated
    public void addListener(PropertySetChangeListener listener) {
        addPropertySetChangeListener(listener);
    }

    @Override
    @Deprecated
    public void removeListener(PropertySetChangeListener listener) {
        removePropertySetChangeListener(listener);
    }

}
