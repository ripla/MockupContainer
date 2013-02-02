/**
 * 
 */
package org.vaadin.risto.mockupcontainer;

import java.util.LinkedList;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.converter.Converter.ConversionException;

/**
 * Implementation of the {@link com.vaadin.data.Property} interface for the
 * {@link org.vaadin.risto.mockupcontainer.MockupContainer MockupContainer}. The
 * value can be set freely. Note that the MockupProperties from a
 * MockupContainer are always of type String.
 * 
 * @author Risto Yrjänä / Vaadin Ltd.
 * 
 */
public class MockupProperty<T> implements Property<T>, ValueChangeNotifier {

    private static final long serialVersionUID = -2472420731149739734L;

    private T value;

    private LinkedList<ValueChangeListener> propertyValueListeners;

    private boolean readOnly;

    private final Class<T> type;

    protected MockupProperty(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        this.readOnly = newStatus;
    }

    @Override
    public void setValue(T newValue) throws ReadOnlyException,
            ConversionException {
        if (readOnly) {
            throw new ReadOnlyException();
        }

        value = newValue;
    }

    @Override
    public String toString() {
        return (value != null ? value.toString() : "null");
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (propertyValueListeners == null) {
            propertyValueListeners = new LinkedList<ValueChangeListener>();
        }
        propertyValueListeners.add(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        if (propertyValueListeners == null) {
            return;
        }
        propertyValueListeners.remove(listener);
    }

    protected void fireValueChangeEvent() {
        if (propertyValueListeners != null) {
            for (ValueChangeListener listener : propertyValueListeners) {
                listener.valueChange(new Property.ValueChangeEvent() {

                    private static final long serialVersionUID = 1795990032860967900L;

                    @Override
                    public Property<T> getProperty() {
                        return MockupProperty.this;
                    }
                });
            }
        }
    }

    @Override
    @Deprecated
    public void addListener(
            com.vaadin.data.Property.ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    @Override
    @Deprecated
    public void removeListener(
            com.vaadin.data.Property.ValueChangeListener listener) {
        removeValueChangeListener(listener);
    }
}