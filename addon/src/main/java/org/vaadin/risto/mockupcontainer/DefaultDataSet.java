package org.vaadin.risto.mockupcontainer;

import java.util.Random;

/**
 * Default implementation of the MockupDataSet interface. Uses a single (long)
 * "Lorem ipsum" -string as a source for the values and id's.
 * 
 * @author Risto Yrjänä / Vaadin Ltd.
 * 
 */
public class DefaultDataSet implements MockupDataSet {
    private static final String DEFAULTDATASTRING = "Lorem ipsum dolor sit amet consectetur adipiscing elit Aenean nunc metus auctor a euismod ac lacinia nec lectus Fusce elit nulla sagittis et cursus non sodales vitae mauris Phasellus sollicitudin aliquam commodo Donec sit amet mattis lacus Maecenas ut elit lectus vel mattis enim Suspendisse metus arcu sagittis et gravida sed semper ut nunc Ut magna tortor luctus ut hendrerit sed dapibus vel ligula Donec ac arcu vel purus bibendum vulputate Cras eu velit ligula Aenean eget sem magna Donec purus risus fermentum nec dapibus eget molestie at ligula Etiam viverra est nec est molestie placerat Donec est ante blandit vitae fringilla a porttitor vitae magna Quisque a felis vel nulla feugiat accumsan Suspendisse fringilla accumsan enim sit amet mattis Nulla at commodo est In hac habitasse platea dictumst Vivamus quis turpis sapien Proin bibendum leo sollicitudin urna egestas adipiscing Vivamus varius mauris eget eros vulputate ut aliquam sapien dignissim Praesent hendrerit justo sit amet elit interdum auctor Nam nec nibh arcu Suspendisse potenti Sed enim eros rhoncus a iaculis suscipit gravida porttitor tortor Praesent pretium auctor ultrices Nunc bibendum metus facilisis justo ullamcorper eget ornare elit gravida Aliquam tortor ante volutpat vel elementum nec hendrerit a odio Aenean bibendum nisi vitae magna elementum et malesuada lacus adipiscing Maecenas adipiscing euismod nibh vel consequat neque condimentum at Donec fermentum aliquam bibendum Nam eleifend purus orci Aenean gravida lectus id varius feugiat enim nisl consectetur mauris at fermentum elit ante eget mauris Nulla in metus sed lorem porta laoreet Nunc lobortis imperdiet lorem nec vestibulum ";
    private final Random r;
    private final String[] dataSet;

    public DefaultDataSet() {
        this(DEFAULTDATASTRING.split(" "));
    }

    /**
     * @param dataSet
     *            the ids and values will be randomised from this array
     */
    public DefaultDataSet(String[] dataSet) {
        r = new Random(System.currentTimeMillis());
        this.dataSet = dataSet;
    }

    public String nextId() {
        return getInternalDataSet()[r.nextInt(getInternalDataSet().length)];
    }

    public String nextValue() {
        return getInternalDataSet()[r.nextInt(getInternalDataSet().length)];
    }

    protected String[] getInternalDataSet() {
        return dataSet;
    }
}