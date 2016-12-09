package railway.test;

import railway.*;

import java.util.*;
import java.util.regex.MatchResult;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link Track} implementation class.
 */
public class CompleteTrackTest {

    /**
     * From hand-out:
     * 
     * Test the initial state of the track
     */
    @Test(timeout = 5000)
    public void testInitialState() throws Exception {

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j0"));
        notJunctions.add(new Junction("j1"));

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(9, new JunctionBranch(notJunctions.get(0),
                Branch.FACING), new JunctionBranch(notJunctions.get(1),
                Branch.FACING)));

        Track track = new Track(); // the track under test

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>();
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Section expectedSection = null;
        Assert.assertEquals(expectedSection, track.getTrackSection(notJunctions
                .get(0), Branch.FACING));

        // check the iterator
        Assert.assertFalse(track.iterator().hasNext());

        // check the string representation
        Assert.assertEquals("", track.toString());

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * From hand-out:
     * 
     * Test adding multiple sections to the track that do not result in the
     * track becoming invalid.
     **/
    @Test(timeout = 5000)
    public void testValidAdditions() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));
        junctions.add(new Junction("j3"));
        junctions.add(new Junction("j4"));
        junctions.add(new Junction("j5"));
        junctions.add(new Junction("j6"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j7"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(80, new JunctionBranch(junctions.get(3),
                Branch.FACING), new JunctionBranch(junctions.get(4),
                Branch.NORMAL)));
        sections.add(new Section(10, new JunctionBranch(junctions.get(5),
                Branch.NORMAL), new JunctionBranch(junctions.get(6),
                Branch.REVERSE)));

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(junctions.get(0),
                Branch.NORMAL), new JunctionBranch(junctions.get(5),
                Branch.REVERSE)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>(junctions);
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null, track.getTrackSection(junctions.get(0),
                Branch.REVERSE));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());

    }

    /**
     * Test adding multiple sections to the track that do not result in the
     * track becoming invalid. The sections that are added contain duplicates.
     **/
    @Test(timeout = 5000)
    public void testValidAdditionsWithDuplicates() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));
        junctions.add(new Junction("j3"));
        junctions.add(new Junction("j4"));
        junctions.add(new Junction("j5"));
        junctions.add(new Junction("j6"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j7"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(80, new JunctionBranch(junctions.get(3),
                Branch.FACING), new JunctionBranch(junctions.get(4),
                Branch.NORMAL)));
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(10, new JunctionBranch(junctions.get(5),
                Branch.NORMAL), new JunctionBranch(junctions.get(6),
                Branch.REVERSE)));
        sections.add(new Section(10, new JunctionBranch(junctions.get(5),
                Branch.NORMAL), new JunctionBranch(junctions.get(6),
                Branch.REVERSE)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(junctions.get(0),
                Branch.NORMAL), new JunctionBranch(junctions.get(5),
                Branch.REVERSE)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>(junctions);
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null, track.getTrackSection(junctions.get(0),
                Branch.REVERSE));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * From hand-out:
     * 
     * Test adding a null section to the track
     **/
    @Test(expected = NullPointerException.class, timeout = 5000)
    public void testNullAdditionException() throws Exception {
        Track track = new Track(); // the track under test
        track.addSection(null);
    }

    /**
     * From hand-out:
     * 
     * Test adding a section to a track that would result in the track becoming
     * invalid.
     **/
    @Test(expected = InvalidTrackException.class, timeout = 5000)
    public void testInvalidAdditionExceptionBasic() throws Exception {

        // junctions, and sections to test with
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.NORMAL)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(2),
                Branch.REVERSE), new JunctionBranch(junctions.get(0),
                Branch.FACING)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }
    }

    /**
     * Test adding multiple sections to the track, some of which would result in
     * the track becoming invalid.
     **/
    @Test(timeout = 5000)
    public void testInvalidAdditionException() throws Exception {

        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));
        junctions.add(new Junction("j3"));
        junctions.add(new Junction("j4"));
        junctions.add(new Junction("j5"));
        junctions.add(new Junction("j6"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j7"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(80, new JunctionBranch(junctions.get(3),
                Branch.FACING), new JunctionBranch(junctions.get(4),
                Branch.NORMAL)));
        sections.add(new Section(10, new JunctionBranch(junctions.get(5),
                Branch.NORMAL), new JunctionBranch(junctions.get(6),
                Branch.REVERSE)));

        // conflicting section that we will attempt to add to the track
        List<Section> conflictingSections = new ArrayList<>();
        conflictingSections.add(new Section(10, new JunctionBranch(junctions
                .get(1), Branch.NORMAL), new JunctionBranch(
                notJunctions.get(0), Branch.FACING)));
        conflictingSections.add(new Section(10, new JunctionBranch(junctions
                .get(0), Branch.FACING), new JunctionBranch(junctions.get(4),
                Branch.NORMAL)));

        Track track = new Track(); // the track under test
        // add the first three sections that are not conflicting
        for (int i = 0; i <= 2; i++) {
            track.addSection(sections.get(i));
        }

        // attempt to add the conflicting sections
        for (int i = 0; i < conflictingSections.size(); i++) {
            try {
                track.addSection(conflictingSections.get(i));
                Assert.fail("InvalidTrackException not thrown");
            } catch (Exception e) {
                Assert.assertTrue("InvalidTrackException not thrown",
                        e instanceof InvalidTrackException);
            }
        }

        // add the remainder of the non-conflicting sections
        for (int i = 3; i < sections.size(); i++) {
            track.addSection(sections.get(i));
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain the conflicting sections
        for (Section conflictingSection : conflictingSections) {
            Assert.assertFalse(track.contains(conflictingSection));
        }

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>(junctions);
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null, track.getTrackSection(junctions.get(0),
                Branch.REVERSE));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * Test removing a section of track from a track with no sections.
     */
    @Test(timeout = 5000)
    public void testRemoveFromEmptySet() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j0"));
        notJunctions.add(new Junction("j1"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(notJunctions.get(0),
                Branch.NORMAL), new JunctionBranch(notJunctions.get(1),
                Branch.REVERSE)));

        // the empty track under test
        Track track = new Track();
        // remove a section that is not on the track
        track.removeSection(notSections.get(0));

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>(junctions);
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());

    }

    /**
     * Test removing a section of track from a track of size one.
     */
    @Test(timeout = 5000)
    public void testRemoveFromSizeOneSet() throws Exception {
        testRemoveExistingSectionSizeOneTrack();
        testRemoveNonExistingSectionSizeOneTrack();
    }

    /**
     * For a track with one section, test removing the only section on the
     * track.
     */
    private void testRemoveExistingSectionSizeOneTrack() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j0"));
        notJunctions.add(new Junction("j1"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(21, new JunctionBranch(notJunctions.get(0),
                Branch.FACING), new JunctionBranch(notJunctions.get(1),
                Branch.REVERSE)));

        Track track = new Track(); // the track under test
        // add one section to the track, and remove it again
        track.addSection(notSections.get(0));
        track.removeSection(notSections.get(0));

        // check that the track does not contain the removed section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        Assert.assertEquals(new HashSet<>(junctions), track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * For a track with one section, test removing a section that is not on the
     * track.
     */
    private void testRemoveNonExistingSectionSizeOneTrack() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j2"));
        notJunctions.add(new Junction("j3"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(21, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.REVERSE)));

        // some sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(notJunctions.get(1),
                Branch.REVERSE)));

        Track track = new Track(); // the track under test
        // add one section to the track
        for (Section section : sections) {
            track.addSection(section);
        }

        /*
         * Remove a section that is not on the track, and check track properties
         */
        track.removeSection(notSections.get(0));

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        Assert.assertEquals(new HashSet<>(junctions), track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * Test removals from a track with multiple sections.
     */
    @Test(timeout = 5000)
    public void testRemoveFromTypicalSet() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("junction0"));
        junctions.add(new Junction("junction1"));
        junctions.add(new Junction("junction2"));
        junctions.add(new Junction("junction3"));
        junctions.add(new Junction("junction4"));
        junctions.add(new Junction("junction5"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("junction6"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.NORMAL), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(0),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(5, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.REVERSE)));
        sections.add(new Section(80, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.NORMAL)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(4),
                Branch.REVERSE), new JunctionBranch(junctions.get(5),
                Branch.REVERSE)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(5),
                Branch.FACING), new JunctionBranch(notJunctions.get(0),
                Branch.REVERSE)));

        // sections that won't be in the track after additions and then removals
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(5),
                Branch.REVERSE)));
        for (int i = 0; i < sections.size(); i++) {
            if (i % 2 == 0) {
                notSections.add(sections.get(i));
            }
        }

        Track track = new Track(); // the track under test

        // add all the sections to the track
        for (Section section : sections) {
            track.addSection(section);
        }
        // remove some of the sections from the track
        for (Section section : notSections) {
            track.removeSection(section);
            sections.remove(section);
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain sections that have been
        // removed, or were never added to the track.
        for (Section section : notSections) {
            Assert.assertFalse(track.contains(section));
        }

        // check that the junctions are correct
        Assert.assertEquals(new HashSet<>(junctions), track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null, track.getTrackSection(junctions.get(0),
                Branch.REVERSE));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /**
     * Test combinations of additions and removals.
     */
    @Test(timeout = 5000)
    public void testAddRemove() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("junction0"));
        junctions.add(new Junction("junction1"));
        junctions.add(new Junction("junction2"));
        junctions.add(new Junction("junction3"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("junction4"));
        notJunctions.add(new Junction("junction5"));
        notJunctions.add(new Junction("junction6"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.NORMAL), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(0),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(5, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.REVERSE)));

        // sections that won't be in the track after additions and then removals
        List<Section> notSections = new ArrayList<>();
        notSections.add(sections.get(0));

        Track track = new Track(); // the track under test

        // combinations of additions and removals
        track.addSection(sections.get(0));
        track.addSection(sections.get(1));
        track.addSection(sections.get(2));
        track.removeSection(sections.get(1));
        track.removeSection(sections.get(0));
        track.addSection(sections.get(3));
        track.addSection(sections.get(1));

        // remove from sections the ones that won't be on the track
        for (Section section : notSections) {
            sections.remove(section);
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain sections that have been
        // removed, or were never added to the track.
        for (Section section : notSections) {
            Assert.assertFalse(track.contains(section));
        }

        // check that the junctions are correct
        Assert.assertEquals(new HashSet<>(junctions), track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null, track.getTrackSection(notJunctions.get(0),
                Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null, track.getTrackSection(junctions.get(0),
                Branch.NORMAL));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        checkGetTrackSection(track, sections);

        // check the iterator of the track
        checkTrackIterator(track, new HashSet<Section>(sections));

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());

    }

    /**
     * Check that the method getJunctions does not expose internal state of the
     * class.
     */
    @Test(timeout = 5000)
    public void testInvariantProtected() throws Exception {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.FACING)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }

        // get the junctions of the track, modify the returned junctions, and
        // check that getJunctions method still works as desired.
        Set<Junction> expectedJunctions = new HashSet<>(junctions);
        Set<Junction> actualJunctions1 = track.getJunctions();
        Assert.assertEquals(expectedJunctions, actualJunctions1);
        try {
            actualJunctions1.clear();
        } catch (UnsupportedOperationException e) {
            // OK if the returned set is immutable
        }
        Set<Junction> actualJunctions2 = track.getJunctions();
        Assert.assertEquals(expectedJunctions, actualJunctions2);

        // check that the invariant has not been violated
        Assert.assertTrue(track.checkInvariant());
    }

    /*----------------- Helper Methods ----------------- */

    /**
     * Checks that the the appropriate sections can be retrieved for all the
     * junctions on the track, given that expectedSections contains the sections
     * that should be on the track.
     * 
     * @param track
     *            the track whose getTrackSection method will be checked
     * @param expectedSections
     *            the sections that are expected to be on the track
     */
    private void checkGetTrackSection(Track track,
            List<Section> expectedSections) throws Exception {
        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        for (Section section : expectedSections) {
            for (JunctionBranch endPoint : section.getEndPoints()) {
                Assert.assertEquals(section, track.getTrackSection(endPoint
                        .getJunction(), endPoint.getBranch()));
            }
        }
    }

    /**
     * Checks that the iterator of the given track has all, and only the
     * expected sections.
     * 
     * @param track
     *            The track whose sections will be checked.
     * @param expectedSections
     *            The expected sections that the track should have
     */
    private void checkTrackIterator(Track track, Set<Section> expectedSections)
            throws Exception {
        // check the iterator of the track
        Set<Section> actualSections = new HashSet<>();
        for (Section section : track) {
            Assert.assertFalse("Duplicate section detected", actualSections
                    .contains(section));
            actualSections.add(section);
        }
        Assert.assertEquals(expectedSections, actualSections);
    }
}
