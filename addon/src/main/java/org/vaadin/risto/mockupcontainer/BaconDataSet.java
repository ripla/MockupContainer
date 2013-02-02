package org.vaadin.risto.mockupcontainer;

/**
 * A more meaty implementation of the MockupDataSet interface. Uses a single
 * (long) "Bacon ipsum" -string as a source for the values and id's.
 * 
 * @author Risto Yrjänä / Vaadin Ltd.
 * 
 */
public class BaconDataSet extends DefaultDataSet {
    private static final String BACONDATASTRING = "Bacon ipsum dolor sit amet salami pork belly tail tongue pancetta pork loin tri-tip drumstick bresaola shankle Bacon ham hock pork belly sausage tri-tip tongue strip steak fatback Tail t-bone salami bacon Bresaola turkey ribeye hamburger meatball t-bone Turkey pancetta ground round pig sirloin tenderloin corned beef meatloaf venison sausage jerky pork loin shank bacon tail Pancetta beef ham hock jowl pork chop pork belly bacon venison rump shoulder shankle cow pastrami sausage Beef ribs drumstick meatball pancetta biltong swine bresaola ribeye jerky spare ribs ham chuck corned beef pork chop";

    public BaconDataSet() {
        super(BACONDATASTRING.split(" "));
    }

}
