package railway.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import railway.*;
import railway.gui.RailwayModel.Train;

/**
 * The controller for the Railway Manager.
 */
public class RailwayController {

    // the model that is being controlled
    private RailwayModel model;
    // the view that is being controlled
    private RailwayView view;

    /*
     * invariant:
     *
     * model != null
     *
     * && view != null
     *
     */

    /**
     * Initialises the Controller for the Railway Manager.
     */
    public RailwayController(RailwayModel model, RailwayView view) {
        this.model = model;
        this.view = view;

        this.model.setTrack(loadTrack());
        this.view.addMessage("Successfully loaded track:");
        view.addDetailMessage(this.model.getTrack().toString());
        
        view.addAddTrainListener(new AddTrainActionListener());
        view.addTrainListListener(new TrainListListener());
        view.addViewAllocationListener(new ViewAllocationActionListener());
        view.addUpdateTrainListener(new UpdateTrainActionListener());
        view.addClearOutputListener(new ClearOutputnActionListener());
    }

    /**
     * Loads the track form the track.txt file.
     * If encounters IOException or FormatException, the message will be
     * displayed using JOptionPane, and the program will exit with status 0.
     * @return the track read form the file.
     */
    private Track loadTrack() {
        // the track to be returned
        Track track = null;
        try {
            track = TrackReader.read("track.txt");
        } catch (IOException e) {
            // the error message to be displayed for IOException
            String message = "Cannot read file: " + e.getMessage();
            JOptionPane.showMessageDialog(null, message, "Error Reading File",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (FormatException e) {
            // the error message to be displayed for FormatException
            String message = "Format error: " + e.getMessage();
            JOptionPane.showMessageDialog(null, message, "Format Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return track;
    }

    /**
     * The action listener class for the add train button.
     */
    private class AddTrainActionListener implements ActionListener {
        /**
         * When the add train button is clicked, constructs and adds a new 
         * train to the model, using the route file and start & end offsets
         * specified in the input fields. 
         * The added train details will be displayed.
         * If the inputs are invalid, the error message will be displayed.
         * @param e
         *            The event to trigger the action.
         */
        public void actionPerformed(ActionEvent e) {
            // the route file name specified in the route file input field
            String routeFile = view.getRouteFileAdd();
            if (routeFile.equals("")) {
                view.addError("Please specify a route file name.");
                return;
            }
            // the start offset specified in the start offset input field
            int startOffset;
            // the end offset specified in the end offset input field
            int endOffset;
            try {
                startOffset = Integer.parseInt(view.getStartOffsetAdd());
                endOffset = Integer.parseInt(view.getEndOffsetAdd());
            } catch (NumberFormatException ex) {
                view.addError("Wrong format of Start Offset or End Offset.");
                return;
            }
            // the route object created from the given route file
            Route route = createRoute(routeFile);
            if (route == null) {
                return;
            }
            // the unique identifier assigned to the new train
            int identifier = model.getTrains().size();
            if (!checkRoute(route, startOffset, endOffset, identifier)) {
                return;
            }
            model.addTrain(identifier, route, startOffset, endOffset);
            view.addTrainListItem("Train " + Integer.toString(identifier));
            view.clearAddTrainInput();
            view.setSelectedIndex(model.getTrains().size() - 1);
            view.addMessage("The following train is added successfully: ");
            view.addDetailMessage(model.getTheLastTrain().toString());
        }

        /**
         * Creates a route object according to the given route file. 
         * If encountered IOException or FormatException, error messages will
         * be displayed in the output area, and null is returned.
         * @param routeFile
         *            The route file name.
         * @return the route created from the given file.
         */
        private Route createRoute(String routeFile) {
            // the route to be returned
            Route route = null;
            try {
                route = RouteReader.read(routeFile);
            } catch (IOException ex) {
                view.addError("Error reading file: " + ex.getMessage());
                return null;
            } catch (FormatException ex) {
                view.addError("Wrong format: " + ex.getMessage());
                return null;
            }
            return route;
        }
    }

    /**
     * The list selection listener class for the train list.
     */
    private class TrainListListener implements ListSelectionListener {
        /**
         * When the selected value of the list is changed, change the texts in
         * the update train sections to the data of the currently 
         * selected train.
         * @param e
         *            The event to trigger the action.
         */
        public void valueChanged(ListSelectionEvent e) {
            // Selecting action actually changes the value twice (one item is 
            // unselected and a new item is selected). The condition clause
            // ensures the inner process is only executed once.
            if (!e.getValueIsAdjusting()) {
                // the index of the current selected item in the list
                int index = view.getSelectedIndex();
                // the currently selected train
                Train train = model.getTrain(index);
                view.setTrainIdentifier(
                        Integer.toString(train.getIdentifier()));
                view.setStartOffsetUpdate(
                        Integer.toString(train.getStartOffset()));
                view.setEndOffsetUpdate(Integer.toString(train.getEndOffset()));
            }
        }
    }

    /**
     * The action listener class for the view allocation button.
     */
    private class ViewAllocationActionListener implements ActionListener {
        /**
         * When the view allocation button is clicked, displays the allocation
         * details of the currently selected train. 
         * If no train is selected, displays an error message.
         * @param e
         *            The event to trigger the action.
         */
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedIndex() < 0) {
                view.addError("No train is specified for viewing allocation. "
                        + "Please select a train in the train list.");
                return;
            }
            // the index of the currently selected item in the list
            int index = view.getSelectedIndex();
            // the currently selected train
            Train train = model.getTrain(index);
            view.addMessage("The allocation of Train " + Integer.toString(index)
                    + " is: ");
            view.addDetailMessage(train.toString());
        }
    }

    /**
     * The action listener class for the update train button.
     */
    private class UpdateTrainActionListener implements ActionListener {
        /**
         * When the update train button is clicked, update the allocation of 
         * the currently selected train and displays the new allocation.
         * If no train is selected, displays an error message.
         * If the inputs are invalid, the error message will be displayed.
         * @param e
         *            The event to trigger the action.
         */
        public void actionPerformed(ActionEvent e) {
            if (view.getSelectedIndex() < 0) {
                view.addError("No train is specified for updating allocation. "
                        + "Please select a train in the train list.");
                return;
            }
            // the start offset specified in the start offset input field
            int startOffset;
            // the end offset specified in the end offset input field
            int endOffset;
            try {
                startOffset = Integer.parseInt(view.getStartOffsetUpdate());
                endOffset = Integer.parseInt(view.getEndOffsetUpdate());
            } catch (NumberFormatException ex) {
                view.addError("Wrong format of Start Offset or End Offset.");
                return;
            }
            // the index of the currently selected item in the list
            int index = view.getSelectedIndex();
            // the currently selected train
            Train train = model.getTrain(index);
            // the whole route of the currently selected train
            Route route = train.getRoute();
            // the unique identifier of the currently selected train
            int identifier = train.getIdentifier();

            if (startOffset == train.getStartOffset()
                    && endOffset == train.getEndOffset()) {
                view.addMessage("The given start and end offsets are the same "
                        + "as the original. The train remains unchanged.");
                return;
            }
            if (!checkRoute(route, startOffset, endOffset, identifier)) {
                return;
            }
            train.setStartOffset(startOffset);
            train.setEndOffset(endOffset);
            view.addMessage("The allocation of Train "
                    + Integer.toString(identifier) + " is updated to: ");
            view.addDetailMessage(train.toString());
        }
    }

    /**
     * The action listener class for the clear output button.
     */
    private class ClearOutputnActionListener implements ActionListener {
        /**
         * When the clear output button is clicked, clears all texts in the 
         * output area.
         * @param e
         *            The event to trigger the action.
         */
        public void actionPerformed(ActionEvent e) {
            view.clearOutput();
        }
    }

    /**
     * Checks if the allocation of the given parameters is currently valid.
     * Following conditions are checked:
     * - The given route is on the track of the system model.
     * - The start and end offset satisfies:
     *      0 <= startOffset < endOffset <= route.getLength().
     * - The sub-route created from the route and offsets does not intersect
     * with any of the sub-routes currently allocated to other trains.
     * 
     * @param route
     *            The route that the train is following.
     * @param startOffset
     *            The start offset of the train's allocation.
     * @param endOffset
     *            The end offset of the train's allocation.
     * @param identifier
     *            The unique identifier of the train.
     * @return Returns true if the allocation is valid, otherwise returns false.
     */
    private boolean checkRoute(Route route, int startOffset, int endOffset,
            int identifier) {
        if (!route.onTrack(model.getTrack())) {
            view.addError("The route in file is not on the system's track.");
            return false;
        }
        if (!((0 <= startOffset) && (startOffset < endOffset)
                && (endOffset <= route.getLength()))) {
            view.addError(
                    "Invalid Start Offset and/or End Offset for the route.");
            return false;
        }
        // The sub-route created from the given route and offsets
        Route subRoute = route.getSubroute(startOffset, endOffset);
        for (int i = 0; i < model.getTrains().size(); i++) {
            // the train at index i
            Train train = model.getTrain(i);
            if (train.getIdentifier() != identifier
                    && subRoute.intersects(train.getAllocation())) {
                view.addError("The sub-route intersects with some sub-routes "
                        + "currently allocated to other trains.");
                return false;
            }
        }
        return true;
    }
}
