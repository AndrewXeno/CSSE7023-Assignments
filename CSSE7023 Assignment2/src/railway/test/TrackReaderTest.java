package railway.test;

import railway.*;

import java.io.IOException;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link TrackReader} implementation class.
 * 
 * CSSE7023: Write your own tests for the class here: I have only added a few
 * basic ones to get you started. You don't have to use these tests in your own
 * test suite.
 * 
 * A more extensive test suite will be performed for assessment of your code,
 * but this should get you started writing your own unit tests.
 */
public class TrackReaderTest {

    /**
     * Test 01
     * Check reading a correctly formatted file with many sections.
     */
    @Test
    public void testCorrectlyFormattedManySections() throws Exception {
        Section[] sections = {
                new Section(9, new JunctionBranch(new Junction("j0"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),

                new Section(777, new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),

                new Section(10, new JunctionBranch(new Junction("j5"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)) };

        Track actualTrack = TrackReader.read("test_01.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Test 02
     * Check reading an incorrectly formatted file: a section is missing
     * information on a line
     */
    @Test
    public void testIncorrectlyFormattedMissingInformation() throws Exception {
        // Error on line 3: the line is blank. Each line should denote a
        // section and all section information is missing from that line.
        // should result in wrong length format or
        // wrong item number error massage
        try {
            TrackReader.read("test_02.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage());// OK
        }
    }

    /**
     * Test 03
     * Check reading an incorrectly formatted file: the length is a negative
     * integer on a line
     */
    @Test
    public void testIncorrectlyFormattedNegativeLength() throws Exception {
        // Error on line 2: the length is negative. Each line should have a
        // positive integer length.
        // should result in wrong length format error message
        try {
            TrackReader.read("test_03.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage());// OK
        }
    }

    /**
     * Test 04
     * Check reading an incorrectly formatted file: the length is not a
     * integer on a line
     */
    @Test
    public void testIncorrectlyFormattedNonintegralLength() throws Exception {
        // Error on line 1: the length is not a integer. Each line should have
        // a positive integer length.
        // should result in wrong length format error message
        try {
            TrackReader.read("test_04.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage());// OK
        }
    }

    /**
     * Test 05
     * Check reading an incorrectly formatted file: the length is not a valid
     * integer on a line
     */
    @Test
    public void testIncorrectlyFormattedWrongLength() throws Exception {
        // Error on line 3: the length is not a integer(a combination of integer
        // and character). Each line should have a positive integer length.
        // should result in wrong length format error message
        try {
            TrackReader.read("test_05.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage());// OK
        }
    }

    /**
     * Test 06
     * Check reading an incorrectly formatted file: the length is missing
     */
    @Test
    public void testIncorrectlyFormattedMissingLength() throws Exception {
        // Error on line 2: the length is missing.
        // should result in wrong length format or
        // invalid item number error message
        try {
            TrackReader.read("test_06.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage());// OK
        }
    }

    /**
     * Test 07
     * Check reading an incorrectly formatted file: one junction is missing
     */
    @Test
    public void testIncorrectlyFormattedMissingJunction() throws Exception {
        // Error on line 2: one of the junction is missing.
        // should result in wrong item number error message
        try {
            TrackReader.read("test_07.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 08
     * Check reading an incorrectly formatted file: one branch is missing
     */
    @Test
    public void testIncorrectlyFormattedMissingBranch() throws Exception {
        // Error on line 2: one of the branch is missing.
        // should result in wrong branch format or
        // invalid item number error message
        try {
            TrackReader.read("test_08.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 09
     * Check reading a correctly formatted file with zero sections.
     */
    @Test
    public void testCorrectlyFormattedEmptyFile() throws Exception {
        // The content in the file is empty,
        // this should return a empty track
        Track actualTrack = TrackReader.read("test_09.txt");
        checkTrackSections(actualTrack, new HashSet<Section>());
    }

    /**
     * Test 10
     * Check reading a correctly formatted file with extra spaces.
     */
    @Test
    public void testCorrectlyFormattedExtraSpaces() throws Exception {
        Section[] sections = {
                new Section(9, new JunctionBranch(new Junction("j0"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),

                new Section(777, new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),

                new Section(10, new JunctionBranch(new Junction("j5"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)) };

        Track actualTrack = TrackReader.read("test_10.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Test 11
     * Check reading an incorrectly formatted file: branch format is wrong
     */
    @Test
    public void testIncorrectlyFormattedWrongBranch() throws Exception {
        // Error on line 2: one of the branch is not the correct form.
        // (i.e not one of "FACING", "NORMAL" or "REVERSE")
        // should result in wrong branch format error message
        try {
            TrackReader.read("test_11.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 12
     * Check reading an incorrectly formatted file: there are more than 5 items
     * in a line
     */
    @Test
    public void testIncorrectlyFormattedExtraItems() throws Exception {
        // Error on line 2: there are 7 items.
        // should result in wrong item number error message
        try {
            TrackReader.read("test_12.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 13
     * Check reading an incorrectly formatted file: two lines denote equivalent
     * sections (two lines are identical)
     */
    @Test
    public void testIncorrectlyFormattedEquivalentSections1() throws Exception {
        // Error on line 2: it is identical to line 1
        // should result in duplicate sections error message
        try {
            TrackReader.read("test_13.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 14
     * Check reading an incorrectly formatted file: two lines denote equivalent
     * sections (only swap 2 end-points)
     */
    @Test
    public void testIncorrectlyFormattedEquivalentSections2() throws Exception {
        // Error on line 3: denotes the equivalent section as line 2
        // should result in duplicate sections error message
        try {
            TrackReader.read("test_14.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 15
     * Check reading an incorrectly formatted file: two sections have
     * a common end-point
     */
    @Test
    public void testIncorrectlyFormattedCommonEndpoints1() throws Exception {
        // Error on line 3: has a common end-point as the section in line 1
        // should result in end-point already connected error message
        try {
            TrackReader.read("test_15.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 16
     * Check reading an incorrectly formatted file: one section has
     * two equivalent end-points
     */
    @Test
    public void testIncorrectlyFormattedCommonEndpoints2() throws Exception {
        // Error on line 3: has two equivalent end-points
        // should result in end-point not distinct error message
        try {
            TrackReader.read("test_16.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 17
     * Check reading a correctly formatted file with one section.
     * (boundary case)
     */
    @Test
    public void testCorrectlyFormattedOneSection() throws Exception {
        Section[] sections = {
                new Section(9, new JunctionBranch(new Junction("j0"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)) };

        Track actualTrack = TrackReader.read("test_17.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Test 18
     * Check reading an incorrectly formatted file:
     * a file with only an empty line
     */
    @Test
    public void testIncorrectlyFormattedOnlyOneEmptyLine() throws Exception {
        // Error on line 1: an empty line
        // should result in wrong item number error message
        try {
            TrackReader.read("test_18.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 19
     * Check reading an incorrectly formatted file:
     * a line with only spaces
     */
    @Test
    public void testIncorrectlyFormattedLineWithOnlySpaces() throws Exception {
        // Error on line 1: a line with only spaces
        // should result in wrong item number
        // or wrong length format error message
        try {
            TrackReader.read("test_19.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 20
     * Check reading a correctly formatted file with a lot of sections.
     */
    @Test
    public void testCorrectlyFormattedALotOFSections() throws Exception {
        Section[] sections = {
                new Section(9, new JunctionBranch(new Junction("j0"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),

                new Section(777, new JunctionBranch(new Junction("j1"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j2"), Branch.FACING)),

                new Section(10, new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j3"), Branch.REVERSE)),

                new Section(92, new JunctionBranch(new Junction("j3"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),

                new Section(77, new JunctionBranch(new Junction("j4"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j5"), Branch.FACING)),

                new Section(210, new JunctionBranch(new Junction("j5"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)),

                new Section(22, new JunctionBranch(new Junction("j5"),
                        Branch.REVERSE), new JunctionBranch(
                                new Junction("j7"), Branch.FACING)),

                new Section(63, new JunctionBranch(new Junction("j7"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j8"), Branch.FACING)),

                new Section(999, new JunctionBranch(new Junction("j6"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j9"), Branch.REVERSE)),

                new Section(1112, new JunctionBranch(new Junction("j10"),
                        Branch.NORMAL), new JunctionBranch(
                                new Junction("j11"), Branch.REVERSE)),

                new Section(42, new JunctionBranch(new Junction("j13"),
                        Branch.FACING), new JunctionBranch(
                                new Junction("j15"), Branch.REVERSE)),

                new Section(65535, new JunctionBranch(
                        new Junction("SomeRandomJunctionName"),
                        Branch.REVERSE), new JunctionBranch(
                                new Junction("AnotherRandomJunctionName"),
                                Branch.FACING)),

                new Section(65535, new JunctionBranch(
                        new Junction("AnotherRandomJunctionName"),
                        Branch.REVERSE), new JunctionBranch(
                                new Junction("SomeRandomJunctionName"),
                                Branch.FACING)) };

        Track actualTrack = TrackReader.read("test_20.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Test 21
     * Check reading an file that is not existed
     */
    @Test
    public void testIOException1() throws Exception {
        try {
            TrackReader.read("test_17_there_is_no_such_file.txt");
            Assert.fail("FormatException not thrown");
        } catch (IOException e) {
            System.err.println(e.getMessage()); // OK
        }
    }

    /**
     * Test 22
     * Check if the argument is an empty string
     */
    @Test
    public void testIOException2() throws Exception {
        try {
            TrackReader.read("");
            Assert.fail("FormatException not thrown");
        } catch (IOException e) {
            System.err.println(e.getMessage()); // OK
        }
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
    private void checkTrackSections(Track track,
            Set<Section> expectedSections) {
        Set<Section> actualSections = new HashSet<>();
        for (Section section : track) {
            actualSections.add(section);
        }
        Assert.assertEquals(expectedSections, actualSections);
    }

}
