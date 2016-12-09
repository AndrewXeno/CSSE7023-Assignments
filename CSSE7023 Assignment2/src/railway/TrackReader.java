package railway;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides a method to read a track from a text file.
 */
public class TrackReader {

    /**
     * <p>
     * Reads a text file named fileName that describes the sections on a track,
     * and returns a track containing each of the sections in the file.
     * </p>
     * 
     * <p>
     * The file contains zero or more lines, each of which corresponds to a
     * section on the track.
     * 
     * Each line should contain five items separated by one or more whitespace
     * characters: a positive integer representing the length of the section,
     * followed by the name of a first junction, then the type of a first
     * branch, followed by the name of a second junction, and then the type of a
     * second branch. The section denoted by the line has the given length, and
     * two end-points: one constructed from the first junction and first branch
     * on the line, and the other constructed from the second junction and
     * section branch.
     * 
     * A junction name is simply an unformatted non-empty string that doesn't
     * contain any whitespace characters. The type of a branch is one of the
     * three strings "FACING", "NORMAL" or "REVERSE", which correspond to the
     * branches Branch.FACING, Branch.NORMAL, and Branch.REVERSE, respectively.
     * 
     * There may be leading or trailing whitespace on each line of the file.
     * (Refer to the Character.isWhitespace() method for the definition of a
     * white space in java.)
     * 
     * For example, the line <br>
     * <br>
     * 
     * 10 j1 FACING j2 NORMAL
     * 
     * <br>
     * <br>
     * denotes a section with length 10 and end-points (j1, FACING) and (j2,
     * NORMAL).
     * </p>
     * 
     * <p>
     * No two lines of the file should denote equivalent sections (as defined by
     * the equals method of the Section class), and no two sections described by
     * the input file should have a common end-point (since each junction can
     * only be connected to at most one section on each branch on a valid
     * track).
     * </p>
     * 
     * <p>
     * The method throws an IOException if there is an input error with the
     * input file (e.g. the file with name given by input parameter fileName
     * does not exist); otherwise it throws a FormatException if there is an
     * error with the input format (this includes the case where there is a
     * duplicate section, and the case where two or more sections have a common
     * end-point), otherwise it returns a track that contains each of the
     * sections described in the file (and no others).
     * 
     * If a FormatException is thrown, it will have a meaningful message that
     * accurately describes the problem with the input file format, including
     * the line of the file where the problem was detected.
     * </p>
     * 
     * @param fileName
     *            the file to read from
     * @return a track containing the sections from the file
     * @throws IOException
     *             if there is an error reading from the input file
     * @throws FormatException
     *             if there is an error with the input format. The
     *             FormatExceptions thrown should have a meaningful message that
     *             accurately describes the problem with the input file format,
     *             including the line of the file where the problem was
     *             detected.
     */
    public static Track read(String fileName) throws IOException,
            FormatException {
        // the track constructed from the file
        Track track = new Track();
        // the file scanner of the opened file
        Scanner fs;
        try {
            fs = new Scanner(new File(fileName));
        } catch (IOException e) {
            throw new IOException("Cannot read file: " + fileName);
        }
        // keep track of the current line number to provide information for
        // throwing exceptions
        int lineCount = 1;
        while (fs.hasNextLine()) {
            // the line that is currently being processed
            String currentLine = fs.nextLine();
            // the line scanner for the current line
            Scanner ls = new Scanner(currentLine);
            // keep the length that read from the current line
            int length = 0;
            // the list that keeps following items in the current line
            // (the length is not contained here)
            ArrayList<String> lineItems = new ArrayList<String>();
            // the section that will be constructed from the current line
            Section section;
            // read the first item as an integer
            if (ls.hasNextInt()) {
                length = ls.nextInt();
            } else {
                ls.close();
                throw new FormatException("Format error in line " + lineCount
                        + ": " + "Wrong length format.");
            }
            // read the rest items as strings and add them into an array list
            while (ls.hasNext()) {
                lineItems.add(ls.next());
            }
            ls.close();
            if (lineItems.size() != 4) {
                throw new FormatException("Format error in line " + lineCount
                        + ": " + "Wrong number of items.");
            }
            try {
                section = constructSection(lineCount, length, lineItems);
                // check for duplicated sections
                if (track.contains(section)) {
                    throw new FormatException("Format error in line "
                            + lineCount + ": Duplicate sections detected.");
                }
                track.addSection(section);

                // catch any known possible exceptions
            } catch (IllegalArgumentException e) {
                throw new FormatException("Format error in line " + lineCount
                        + ": " + e.getMessage());
            } catch (NullPointerException e) {
                throw new FormatException("Format error in line " + lineCount
                        + ": " + e.getMessage());
            } catch (InvalidTrackException e) {
                throw new FormatException("Format error in line " + lineCount
                        + ": " + e.getMessage());
            } catch (FormatException e) {
                throw new FormatException(e.getMessage());
            }
            ls.close();
            lineCount++;
        }
        fs.close();
        return track;
    }

    /**
     * construct and return a Section according to the given parameters.
     * 
     * @require lineCount >= 1 &&
     * 
     *          length is an integer && length > 0
     * 
     *          lineItems != null &&
     * 
     *          lineItems.size() == 4 &&
     * 
     *          lineItems.get(0) denotes the name of a first junction &&
     *          lineItems.get(1) denotes the type of a first branch &&
     *          lineItems.get(2) denotes the name of a second junction &&
     *          lineItems.get(3) denotes the type of a second branch &&
     * 
     *          the strings of elements in the lineItems are in correct format
     * 
     * @ensure the returned Section is constructed with the given parameters,
     *         and it is not null.
     * 
     * @param lineCount
     *            the line number of the current line in the opened file.
     * @param length
     *            the length of the section to be constructed.
     * @param lineItems
     *            a string list of the other four items in the current line.
     * @return
     *         the constructed Section.
     * @throws FormatException
     *             if the format of lineItems.get(1) or lineItems.get(3) is
     *             incorrect.
     *             (i.e. not one of "FACING", "NORMAL" or "REVERSE")
     */
    private static Section constructSection(int lineCount, int length,
            ArrayList<String> lineItems) throws FormatException {
        // the branch of the first end-point
        Branch branch1 = constructBranch(lineItems.get(1));
        // the branch of the second end-point
        Branch branch2 = constructBranch(lineItems.get(3));
        if (branch1 == null) {
            throw new FormatException("Format error in line " + lineCount +
                    ": " + "Wrong format for the first branch type.");
        }
        if (branch2 == null) {
            throw new FormatException("Format error in line " + lineCount +
                    ": " + "Wrong format for the second branch type.");
        }
        // the junction of the first end-point
        Junction junction1 = new Junction(lineItems.get(0));
        // the junction of the second end-point
        Junction junction2 = new Junction(lineItems.get(2));
        // the first end-point of the section
        JunctionBranch endPoint1 = new JunctionBranch(junction1, branch1);
        // the second end-point of the section
        JunctionBranch endPoint2 = new JunctionBranch(junction2, branch2);
        // the returned section
        Section section = new Section(length, endPoint1, endPoint2);
        return section;
    }

    /**
     * construct and return a Branch according to the string s.
     * 
     * @require s != null
     * 
     * @ensure if the string is one of "FACING", "NORMAL" or "REVERSE", it will
     *         return a branch of Branch.FACING, Branch.NORMAL, and
     *         Branch.REVERSE, respectively.
     * 
     *         Otherwise, it will return null.
     * 
     * @param s
     *            the string of the branch type
     * @return
     *         The constructed Branch. Return null if the given string has a
     *         wrong
     *         format.
     */
    private static Branch constructBranch(String s) {
        if (s.equals("FACING")) {
            return Branch.FACING;
        } else if (s.equals("NORMAL")) {
            return Branch.NORMAL;
        } else if (s.equals("REVERSE")) {
            return Branch.REVERSE;
        } else {
            return null;
        }
    }
}
