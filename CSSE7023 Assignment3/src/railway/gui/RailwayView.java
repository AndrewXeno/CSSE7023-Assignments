package railway.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The view for the Railway Manager.
 */
@SuppressWarnings("serial")
public class RailwayView extends JFrame {

    // the model of the Railway Manager
    private RailwayModel model;

    // UI components for Add Train section:
    // the input field for the route file
    private JTextField routeFileAdd;
    // the input field for the start offset
    private JTextField startOffsetAdd;
    // the input field for the end offset
    private JTextField endOffsetAdd;
    // the button for adding the train
    private JButton addTrain;

    // UI components for Train List section:
    // the selective list for displaying added trains
    private JList<String> trainList;
    // the data model of the train list
    private DefaultListModel<String> listModel;
    // the button for viewing a train's allocation
    private JButton viewAllocation;

    // UI components for Update Train section:
    // the input field for the train identifier
    private JTextField trainIdentifier;
    // the input field for the start offset
    private JTextField startOffsetUpdate;
    // the input field for the end offset
    private JTextField endOffsetUpdate;
    // the button for updating the train allocation
    private JButton updateTrain;

    // UI components for Output Information section:
    // the text pane for displaying styled output text
    private JTextPane outputArea;
    // the button for clear the text pane
    private JButton clearOutput;

    // predefined constants:
    // the width of the window
    private final int windowWidth = 640;
    // the height of the window
    private final int windowHeight = 480;
    // the system line separator
    private final String LineSeparator = System.getProperty("line.separator");

    /*
     * invariant:
     *
     * model != null
     *
     * && routeFileAdd != null
     * 
     * && startOffsetAdd != null
     * 
     * && endOffsetAdd != null
     * 
     * && addTrain != null
     * 
     * && trainList != null
     * 
     * && listModel != null
     * 
     * && viewAllocation != null
     * 
     * && trainIdentifier != null
     * 
     * && startOffsetUpdate != null
     * 
     * && endOffsetUpdate != null
     * 
     * && updateTrain != null
     * 
     * && outputArea != null
     * 
     * && clearOutput != null
     *
     */

    /**
     * Creates a new Railway Manager window.
     */
    public RailwayView(RailwayModel model) {
        this.model = model;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // REMOVE THIS LINE AND COMPLETE THIS METHOD
        setTitle("Train Management System");
        setSize(windowWidth, windowHeight);
        setResizable(false);
        Container c = getContentPane();
        // Move the window to the centre of the screen
        setLocationRelativeTo(c);
        c.setLayout(null);
        createAddTrain(c);
        createTrainList(c);
        createUpdateTrain(c);
        createOutputInformation(c);
    }

    /**
     * Creates UI components for Add Train section on the given container.
     * 
     * @param container
     *            the container that the UI components will be added on
     */
    private void createAddTrain(Container container) {
        // a pane to wrap all UI components in this section
        JPanel wrapper = new JPanel();
        wrapper.setBounds(10, 10, 614, 80);
        wrapper.setLayout(null);
        wrapper.setBorder(BorderFactory.createTitledBorder("Add Train"));

        // the label indicating the route file input area
        JLabel routeFileLabel = new JLabel("Route File: ");
        routeFileLabel.setBounds(10, 20, 300, 20);
        wrapper.add(routeFileLabel);

        routeFileAdd = new JTextField("");
        routeFileAdd.setBounds(10, 40, 300, 20);
        wrapper.add(routeFileAdd);

        // the label indicating the start offset input area
        JLabel startOffsetLabel = new JLabel("Start Offset: ");
        startOffsetLabel.setBounds(320, 20, 80, 20);
        wrapper.add(startOffsetLabel);

        startOffsetAdd = new JTextField("");
        startOffsetAdd.setBounds(320, 40, 80, 20);
        wrapper.add(startOffsetAdd);

        // the label indicating end offset input area
        JLabel endOffsetLabel = new JLabel("End Offset: ");
        endOffsetLabel.setBounds(410, 20, 80, 20);
        wrapper.add(endOffsetLabel);

        endOffsetAdd = new JTextField("");
        endOffsetAdd.setBounds(410, 40, 80, 20);
        wrapper.add(endOffsetAdd);

        addTrain = new JButton("Add");
        addTrain.setBounds(504, 40, 100, 20);
        wrapper.add(addTrain);

        container.add(wrapper);
    }

    /**
     * Creates UI components for Train List section on the given container.
     * 
     * @param container
     *            the container that the UI components will be added on
     */
    private void createTrainList(Container container) {
        // a pane to wrap all UI components in this section
        JPanel wrapper = new JPanel();
        wrapper.setBounds(10, 100, 150, 340);
        wrapper.setLayout(null);
        wrapper.setBorder(BorderFactory.createTitledBorder("Train List"));

        // the scroll bar of the Train List
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 131, 280);
        listModel = new DefaultListModel<String>();
        trainList = new JList<String>(listModel);
        trainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(trainList);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        viewAllocation = new JButton("View Allocation");
        viewAllocation.setBounds(10, 310, 130, 20);
        wrapper.add(viewAllocation);

        container.add(wrapper);
    }

    /**
     * Creates UI components for Update Train section on the given container.
     * 
     * @param container
     *            the container that the UI components will be added on
     */
    private void createUpdateTrain(Container container) {
        // a pane to wrap all UI components in this section
        JPanel wrapper = new JPanel();
        wrapper.setBounds(170, 100, 454, 80);
        wrapper.setLayout(null);
        wrapper.setBorder(BorderFactory.createTitledBorder("Update Train "
                + "(please specify a train using the Train List)"));

        // the label indicating the train identifier input area
        JLabel identifierLabel = new JLabel("Train Identifier: ");
        identifierLabel.setBounds(10, 20, 140, 20);
        wrapper.add(identifierLabel);

        trainIdentifier = new JTextField("");
        trainIdentifier.setBounds(10, 40, 140, 20);
        trainIdentifier.setEditable(false);
        wrapper.add(trainIdentifier);

        // the label indicating the start offset input area
        JLabel startOffsetLabel = new JLabel("Start Offset: ");
        startOffsetLabel.setBounds(160, 20, 80, 20);
        wrapper.add(startOffsetLabel);

        startOffsetUpdate = new JTextField("");
        startOffsetUpdate.setBounds(160, 40, 80, 20);
        wrapper.add(startOffsetUpdate);

        // the label indicating the end offset input area
        JLabel endOffsetLabel = new JLabel("End Offset: ");
        endOffsetLabel.setBounds(250, 20, 80, 20);
        wrapper.add(endOffsetLabel);

        endOffsetUpdate = new JTextField("");
        endOffsetUpdate.setBounds(250, 40, 80, 20);
        wrapper.add(endOffsetUpdate);

        updateTrain = new JButton("Update");
        updateTrain.setBounds(344, 40, 100, 20);
        wrapper.add(updateTrain);

        container.add(wrapper);
    }

    /**
     * Creates UI components for Output Information section on the given
     * container.
     * 
     * @param container
     *            the container that the UI components will be added on
     */
    private void createOutputInformation(Container container) {
        // a pane to wrap all UI components in this section
        JPanel wrapper = new JPanel();
        wrapper.setBounds(170, 190, 454, 250);
        wrapper.setLayout(null);
        wrapper.setBorder(
                BorderFactory.createTitledBorder("Output Information"));

        // the scroll bar of the output area
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 20, 435, 190);
        outputArea = new JTextPane();
        outputArea.setEditable(false);
        scrollPane.setViewportView(outputArea);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        clearOutput = new JButton("Clear Log");
        clearOutput.setBounds(344, 220, 100, 20);

        wrapper.add(clearOutput);
        container.add(wrapper);
    }

    /**
     * Returns the text content in the route file text area in the
     * Add Train section.
     * 
     * @return the route file name specified by the user.
     */
    public String getRouteFileAdd() {
        return routeFileAdd.getText();
    }

    /**
     * Returns the text content in the start offset text area in the
     * Add Train section.
     * 
     * @return the start offset of the train specified by the user in string.
     */
    public String getStartOffsetAdd() {
        return startOffsetAdd.getText();
    }

    /**
     * Returns the text content in the end offset text area in the
     * Add Train section.
     * 
     * @return the end offset of the train specified by the user in string.
     */
    public String getEndOffsetAdd() {
        return endOffsetAdd.getText();
    }

    /**
     * Returns the text content in the start offset text area in the
     * Update Train section.
     * 
     * @return the new start offset specified by the user in string.
     */
    public String getStartOffsetUpdate() {
        return startOffsetUpdate.getText();
    }

    /**
     * Returns the text content in the end offset text area in the
     * Update Train section.
     * 
     * @return the new end offset specified by the user in string.
     */
    public String getEndOffsetUpdate() {
        return endOffsetUpdate.getText();
    }

    /**
     * Sets the text in the train identifier text field to the given string.
     * 
     * @param string
     *            The string to be displayed in the train identifier text field.
     */
    public void setTrainIdentifier(String string) {
        trainIdentifier.setText(string);
    }

    /**
     * Sets the text in the start offset text field to the given string.
     * 
     * @param string
     *            The string to be displayed in the start offset text field.
     */
    public void setStartOffsetUpdate(String string) {
        startOffsetUpdate.setText(string);
    }

    /**
     * Sets the text in the end offset text field to the given string.
     * 
     * @param string
     *            The string to be displayed in the end offset text field.
     */
    public void setEndOffsetUpdate(String string) {
        endOffsetUpdate.setText(string);
    }

    /**
     * Adds an item into the train list model.
     * 
     * @param string
     *            The sting to be displayed in the list representing the
     *            added item.
     */
    public void addTrainListItem(String string) {
        listModel.addElement(string);
    }

    /**
     * Clears texts in the input fields of the Add Train section.
     */
    public void clearAddTrainInput() {
        startOffsetAdd.setText("");
        endOffsetAdd.setText("");
    }

    /**
     * Returns the index of the item that is currently being selected
     * in the Train List.
     * 
     * @return the index of the item that is currently being selected
     *         in the Train List.
     */
    public int getSelectedIndex() {
        return trainList.getSelectedIndex();
    }

    /**
     * Sets the item with the given index to be selected in the train list.
     * 
     * @param index
     */
    public void setSelectedIndex(int index) {
        trainList.setSelectedIndex(index);
    }

    /**
     * Returns the content in the output area.
     * 
     * @return the content in the output area.
     */
    public String getOutput() {
        return outputArea.getText();
    }

    /**
     * Sets the text of the output area to the given string.
     * 
     * @param string
     *            The string to be displayed in the output area.
     */
    public void setOutput(String string) {
        outputArea.setText(string);
    }

    // // some legacy code for TextArea implementation.
    // public void addError(String s){
    // String message = ">> [Error] " + s + LineSeparator;
    // outputArea.append(message);
    // }
    //
    // public void addMessage(String s){
    // String message = ">> "+s + LineSeparator;
    // outputArea.append(message);
    // }
    //
    // public void addDetailMessage(String s) {
    // String message = s + LineSeparator;
    // outputArea.append(message);
    // }

    /**
     * Appends an error message to the output area.
     * The error message will be shown in red.
     * 
     * @param string
     *            The raw error message to be added.
     */
    public void addError(String string) {
        // the text document of the output area
        Document document = outputArea.getStyledDocument();
        // the style configuration of the new message
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, Color.RED);
        // the processed error message to be appended
        String error = ">> [Error] " + string + LineSeparator;
        try {
            document.insertString(document.getLength(), error, style);
            outputArea.select(Integer.MAX_VALUE, 0);
        } catch (BadLocationException e) {
        }
    }

    /**
     * Appends a normal message to the output area.
     * A normal message is used to show the result of an action.
     * The message will be shown in black.
     * 
     * @param string
     *            The raw message to be added.
     */
    public void addMessage(String string) {
        // the text document of the output area
        Document document = outputArea.getStyledDocument();
        // the style configuration of the new message
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, Color.BLUE);
        // the processed message to be appended
        String message = ">> " + string + LineSeparator;
        try {
            document.insertString(document.getLength(), message, style);
            outputArea.select(Integer.MAX_VALUE, 0);
        } catch (BadLocationException e) {
        }
    }

    /**
     * Appends a detailed message to the output area.
     * A detailed message is used to show detail information of the track or
     * an train.
     * The message will be shown in black.
     * 
     * @param string
     *            The raw message to be added.
     */
    public void addDetailMessage(String string) {
        // the text document of the output area
        Document document = outputArea.getStyledDocument();
        // the style configuration of the new message
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, Color.BLACK);
        // the processed message to be appended
        String message = string + LineSeparator;
        try {
            document.insertString(document.getLength(), message, style);
            outputArea.select(Integer.MAX_VALUE, 0);
        } catch (BadLocationException e) {
        }
    }

    /**
     * Clears texts in the output area.
     */
    public void clearOutput() {
        outputArea.setText("");
    }

    /**
     * Adds the given action listener to the add train button.
     * 
     * @param pl
     *            The action listener to be added.
     */
    public void addAddTrainListener(ActionListener pl) {
        addTrain.addActionListener(pl);
    }

    /**
     * Adds the given list selection listener to the train list.
     * 
     * @param pl
     *            The list selection listener to be added.
     */
    public void addTrainListListener(ListSelectionListener pl) {
        trainList.addListSelectionListener(pl);
    }

    /**
     * Adds the given action listener to the view allocation button.
     * 
     * @param pl
     *            The action listener to be added.
     */
    public void addViewAllocationListener(ActionListener pl) {
        viewAllocation.addActionListener(pl);
    }

    /**
     * Adds the given action listener to the update train button.
     * 
     * @param pl
     *            The action listener to be added.
     */
    public void addUpdateTrainListener(ActionListener pl) {
        updateTrain.addActionListener(pl);
    }

    /**
     * Adds the given action listener to the clear output button.
     * 
     * @param pl
     *            The action listener to be added.
     */
    public void addClearOutputListener(ActionListener pl) {
        clearOutput.addActionListener(pl);
    }

}
