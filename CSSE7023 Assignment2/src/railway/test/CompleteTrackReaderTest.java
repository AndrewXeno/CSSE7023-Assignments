package railway.test;

import railway.*;

import java.io.IOException;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link TrackReader} implementation class.
 */
public class CompleteTrackReaderTest {

    /**
     * Check reading a correctly formatted file with zero sections.
     */
    @Test(timeout = 5000)
    public void testCorrectlyFormattedNoSections() throws Exception {
        Section[] expectedSections = {};

        Track actualTrack = TrackReader.read("read_01_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(expectedSections)));
    }

    /**
     * Check reading a correctly formatted file with one section.
     */
    @Test(timeout = 5000)
    public void testCorrectlyFormattedOneSection() throws Exception {
        Section[] sections =
                { new Section(9, new JunctionBranch(new Junction("j0"),
                        Branch.FACING), new JunctionBranch(new Junction("j1"),
                        Branch.FACING)) };

        Track actualTrack = TrackReader.read("read_02_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Check reading a correctly formatted file with many sections.
     */
    @Test(timeout = 5000)
    public void testCorrectlyFormattedManySections() throws Exception {
        Section[] sections =
                {
                        new Section(9, new JunctionBranch(new Junction("j0"),
                                Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),
                        new Section(20, new JunctionBranch(new Junction("j1"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j2"), Branch.FACING)),
                        new Section(777, new JunctionBranch(new Junction("j2"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),
                        new Section(6, new JunctionBranch(new Junction("j1"),
                                Branch.REVERSE), new JunctionBranch(
                                new Junction("j3"), Branch.NORMAL)),
                        new Section(80, new JunctionBranch(new Junction("j3"),
                                Branch.FACING), new JunctionBranch(
                                new Junction("j4"), Branch.NORMAL)),
                        new Section(10, new JunctionBranch(new Junction("j5"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)) };

        Track actualTrack = TrackReader.read("read_03_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Check reading a correctly formatted file with many sections.
     */
    @Test(timeout = 5000)
    public void testCorrectlyFormattedManySectionsB() throws Exception {
        Section[] sections =
                {
                        new Section(9, new JunctionBranch(new Junction("j0"),
                                Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),

                        new Section(777, new JunctionBranch(new Junction("j2"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),

                        new Section(10, new JunctionBranch(new Junction("j5"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)) };

        Track actualTrack = TrackReader.read("read_03b_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Check reading a correctly formatted file with many sections: extra
     * leading and trailing spaces on lines and between items in a section.
     */
    @Test(timeout = 5000)
    public void testCorrectlyFormattedManySectionsExtraSpaces()
            throws Exception {
        Section[] sections =
                {
                        new Section(9, new JunctionBranch(new Junction("j0"),
                                Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),
                        new Section(20, new JunctionBranch(new Junction("j1"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j2"), Branch.FACING)), };

        Track actualTrack = TrackReader.read("read_04_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Check reading an incorrectly formatted file: duplicate exception, or two
     * sections share a common end-point.
     */
    @Test(timeout = 5000)
    public void testIncorrectlyFormattedDuplicateSectionOrCommonEndPoint()
            throws Exception {
        testIncorrectlyFormattedDuplicateSection();
        testIncorrectlyFormattedCommonEndPoint();
    }

    /**
     * Check reading an incorrectly formatted file: duplicate exception.
     */
    public void testIncorrectlyFormattedDuplicateSection() throws Exception {
        try {
            // Error on line 5: duplicate section: 20 (j2, FACING) (j1, NORMAL)
            TrackReader.read("read_05_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Check reading an incorrectly formatted file: two sections share a common
     * end-point.
     */
    public void testIncorrectlyFormattedCommonEndPoint() throws Exception {
        try {
            // Error on line 4: cannot add section 6 (j2, FACING) (j1, REVERSE)
            // to the track: The junction j2 is already connected to a section
            // along branch FACING
            TrackReader.read("read_06_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Check reading an incorrectly formatted file: a branch given does not
     * correspond to a branch type.
     */
    @Test(timeout = 5000)
    public void testIncorrectlyFormattedInvalidBranch() throws Exception {
        try {
            // Error on line 3: invalid branch: NORMALLL
            TrackReader.read("read_07_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Check reading an incorrectly formatted file: the length of a section is
     * not a positive integer
     */
    @Test(timeout = 5000)
    public void testIncorrectlyFormattedInvalidLength() throws Exception {
        // Error on line 2: length is negative
        try {
            TrackReader.read("read_08_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
        // Error on line 3: length is zero
        try {
            TrackReader.read("read_09_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
        // Error on line 2: length is not an integer
        try {
            TrackReader.read("read_10_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Check reading an incorrectly formatted file: a section is missing
     * information on a line, and a section has additional information on line.
     */
    @Test(timeout = 5000)
    public void testIncorrectlyFormattedAddedOrMissingInformation()
            throws Exception {
        testIncorrectlyFormattedMissingInformation();
        testIncorrectlyFormattedExtraInformation();
    }

    /**
     * Check reading an incorrectly formatted file: a section is missing
     * information on a line
     */
    public void testIncorrectlyFormattedMissingInformation() throws Exception {
        // Error on line 3: all information missing from a line
        try {
            TrackReader.read("read_11_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
        // Error on line 3: some information missing from a line
        try {
            TrackReader.read("read_12_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Check reading an incorrectly formatted file: a section has additional
     * information on a line
     */
    public void testIncorrectlyFormattedExtraInformation() throws Exception {
        // Error on line 1: additional information at end of line
        try {
            TrackReader.read("read_13_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
            // System.out.println(e.getMessage());
        }
    }

    /**
     * Test reading from a file that does not exist.
     */
    @Test(timeout = 5000, expected = IOException.class)
    public void testReadIOError() throws Exception {
        TrackReader.read("doesNotExist.txt");
    }

    // -----Helper Methods-------------------------------

    /**
     * Checks that the given track has all, and only the expected sections.
     * 
     * @param track
     *            The track whose sections will be checked.
     * @param expectedSections
     *            The expected sections that the track should have
     */
    private void checkTrackSections(Track track, Set<Section> expectedSections) {
        Set<Section> actualSections = new HashSet<>();
        for (Section section : track) {
            actualSections.add(section);
        }
        Assert.assertEquals(expectedSections, actualSections);
    }

}
