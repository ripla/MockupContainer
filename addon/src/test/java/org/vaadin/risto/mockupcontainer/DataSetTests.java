package org.vaadin.risto.mockupcontainer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DataSetTests {

    @Test
    public void testDefaultDataSetValuesNotNull() {
        DefaultDataSet dataSet = new DefaultDataSet();
        for (String value : dataSet.getInternalDataSet()) {
            assertNotNull(value);
        }
    }

    @Test
    public void testBaconDataSetValuesNotNull() {
        BaconDataSet dataSet = new BaconDataSet();
        for (String value : dataSet.getInternalDataSet()) {
            assertNotNull(value);
        }
    }

}
