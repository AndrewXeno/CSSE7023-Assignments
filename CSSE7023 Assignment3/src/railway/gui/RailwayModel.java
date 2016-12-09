package railway.gui;

import java.util.*;

import railway.*;

/**
 * The model for the Railway Manager.
 */
public class RailwayModel {

    // The track loaded from the track.txt.
    private Track track;
    // A list containing all trains added to the program model.
    private ArrayList<Train> trains;

    /*
     * invariant:
     *
     * trains != null
     *
     * && each element of trains != null
     * 
     * && each train t in trains satisfies:
     *      - t.getRoute().onTrack(track) == true
     *      - && the sub-route allocated to t does not intersect with any of
     *        the sub-routes currently allocated to other elements in trains.
     *
     */

    /**
     * Initialises the model for the Railway Manager.
     */
    public RailwayModel() {
        track = null;
        trains = new ArrayList<Train>();
    }

    /**
     * Sets the instance variable track to the given track object.
     * 
     * @param track
     *            the track on which the trains will be managed.
     */
    public void setTrack(Track track) {
        this.track = track;
    }

    /**
     * Returns the track on which the trains will be managed.
     * 
     * @return the track on which the trains will be managed.
     */
    public Track getTrack() {
        return this.track;
    }

    /**
     * Returns the whole list of Trains that have been added to the program
     * model.
     * 
     * @return the list of Trains that have been added to the program model.
     */
    public ArrayList<Train> getTrains() {
        return trains;
    }

    /**
     * Returns the train with the given index in the train list.
     * 
     * @require 0 <= index && index < this.trains.size().
     * @ensure the returned train is at the given index of the train list.
     * @param index
     *            The given index of the train.
     * @return the train with the given index.
     */
    public Train getTrain(int index) {
        return trains.get(index);
    }

    /**
     * Add a train with the given parameters
     * 
     * @require identifier >= 0 &&
     *          route != null &&
     *          route.onTrack(this.track) == true &&
     *          0 <= startOffset < endOffset <= route.getLength().
     * @ensure the correct train is added to the end of the trains list.
     * @param identifier
     *            the unique identifier of the train to be added.
     * @param route
     *            the route that the train follows.
     * @param startOffset
     *            the start offset of the sub-route that the train is
     *            allocated to.
     * @param endOffset
     *            the end offset of the sub-route that the train is
     *            allocated to.
     */
    public void addTrain(int identifier, Route route, int startOffset,
            int endOffset) {
        // the new train to be added
        Train train = new Train(identifier, route, startOffset, endOffset);
        this.getTrains().add(train);
    }

    /**
     * Returns the last element in the train list.
     * 
     * @require trains.size() > 0.
     * @ensure the correct last train is returned.
     * @return the last train in the train list.
     */
    public Train getTheLastTrain() {
        return trains.get(trains.size() - 1);
    }

    /**
     * The class to represent a train.
     */
    public class Train {
        // the unique identifier of the train
        private int identifier;
        // the route that the train is following
        private Route route;
        // the start offset of the sub-route that the train is allocated to.
        private int startOffset;
        // the end offset of the sub-route that the train is allocated to.
        private int endOffset;

        /*
         * invariant:
         *
         * identifier >= 0
         *
         * && route != null
         * 
         * && 0 <= startOffset < endOffset <= route.getLength()
         *
         */

        /**
         * Construct the train object from the given parameters.
         * 
         * @param identifier
         *            The given unique identifier of the train.
         * @param route
         *            The given route that the train is following.
         * @param startOffset
         *            The given start offset of the sub-route that the train
         *            is allocated to.
         * @param endOffset
         *            The given end offset of the sub-route that the train
         *            is allocated to.
         */
        public Train(int identifier, Route route, int startOffset,
                int endOffset) {
            this.identifier = identifier;
            this.route = route;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        /**
         * Returns the unique identifier assigned to the train.
         * 
         * @return the unique identifier of the train.
         */
        public int getIdentifier() {
            return identifier;
        }

        /**
         * Returns the route that the train is following.
         * 
         * @return the route that the train is following.
         */
        public Route getRoute() {
            return route;
        }

        /**
         * Returns the start offset of the sub-route that the train is
         * allocated to.
         * 
         * @return the start offset of the train.
         */
        public int getStartOffset() {
            return startOffset;
        }

        /**
         * Returns the end offset of the sub-route that the train is
         * allocated to.
         * 
         * @return the end offset of the train.
         */
        public int getEndOffset() {
            return endOffset;
        }

        /**
         * Returns the sub-route that the train is allocated to.
         * 
         * @return the sub-route that the train is allocated to.
         */
        public Route getAllocation() {
            return this.getRoute().getSubroute(startOffset, endOffset);
        }

        /**
         * Set the train's start offset to the given value
         * 
         * @param startOffset
         *            The new start offset of the train.
         */
        public void setStartOffset(int startOffset) {
            this.startOffset = startOffset;
        }

        /**
         * Set the train's end offset to the given value.
         * 
         * @param endOffset
         *            The new end offset of the train.
         */
        public void setEndOffset(int endOffset) {
            this.endOffset = endOffset;
        }

        /**
         * Returns the string representation of the train.
         * The String representation contains following lines: the first line
         * is the unique identifier of the train. The second and the third line
         * are the start and end offsets of the train, respectively. The rest
         * of lines are the string representation of the whole route that the
         * train is following.
         * 
         * @return the string representation of the train.
         */
        @Override
        public String toString() {
            // the system line separator
            String lineSeparator = System.getProperty("line.separator");
            // each lines of the string representation
            String line1 = "Identifier: " + identifier + lineSeparator;
            String line2 = "Start Offset: " + startOffset + lineSeparator;
            String line3 = "End Offset: " + endOffset + lineSeparator;
            String line4 = "Following Route: " + lineSeparator + route;
            // the result to be returned
            String result = line1 + line2 + line3 + line4;
            return result;
        }
    }
}
