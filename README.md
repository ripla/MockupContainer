#MockupContainer

MockupContainer is a container that implements Container.Indexed and Container.Hierarchical and is intended for testing Vaadin components. The contents of the container are automatically generated. 

The container allows some parametrisation for the generation of the content: item count, property count, number of children and the delay can be easily changed on the fly. The delay affects the time it takes to retrieve items, or item related attributes (such as the parent and children of an item). A factory class is provided for easier testing.

The generated contents can also be specified by the user by implementing a data set interface. DefaultDataSet and BaconDataSet are included in the package as example implementations. (You can enable bacon mode in the demo with the "bacon" parameter.)

MockupContainer could be used for example in creating quick UI-mockups, or for testing any component that uses a datasource.