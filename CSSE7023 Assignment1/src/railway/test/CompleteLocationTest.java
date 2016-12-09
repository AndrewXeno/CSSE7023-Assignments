package railway.test;

import railway.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link Location} implementation class.
 */
public class CompleteLocationTest {

    /**
     * From hand-out:
     * 
     * Test construction of a typical location that is not at a junction
     */
    @Test(timeout = 5000)
    public void testTypicalLocation() throws Exception {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = endPoint1;
        int offset = 3;

        // the location under test
        Location location = new Location(section, endPoint, offset);

        // check the section, end-point and offset of the location
        Assert.assertEquals(section, location.getSection());
        Assert.assertEquals(endPoint, location.getEndPoint());
        Assert.assertEquals(offset, location.getOffset());

        // check whether or not this location is at a junction
        Assert.assertFalse(location.atAJunction());

        // the location is not at a junction: test that the location is on a
        // section equivalent to location.getSection()
        Section equivalentSection = new Section(length, endPoint1, endPoint2);
        Assert.assertTrue(location.onSection(equivalentSection));

        // the location is not at a junction: check that it is not on some other
        // section
        Section anotherSection =
                new Section(10, new JunctionBranch(new Junction("j1"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j3"),
                        Branch.FACING));
        Assert.assertFalse(location.onSection(anotherSection));

        // check that the string representation is correct
        String actualString = location.toString();
        String expectedString = "Distance 3 from j1 along the FACING branch";
        Assert.assertEquals(expectedString, actualString);

        // check that the class invariant has been established.
        Assert.assertTrue("Invariant incorrect", location.checkInvariant());
    }

    /** Test construction of a location that is at a junction */
    @Test(timeout = 5000)
    public void testLocationAtJunction() throws Exception {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = endPoint1;
        int offset = 0;

        // the location under test
        Location location = new Location(section, endPoint, offset);

        // check the section, end-point and offset of the location
        Assert.assertEquals(section, location.getSection());
        Assert.assertEquals(endPoint, location.getEndPoint());
        Assert.assertEquals(offset, location.getOffset());

        // check whether or not this location is at a junction
        Assert.assertTrue(location.atAJunction());

        // the location is at a junction: check that it is on a section that
        // also has that junction at one of its end-points
        Section anotherSection =
                new Section(10, new JunctionBranch(new Junction("j1"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j3"),
                        Branch.FACING));
        Assert.assertTrue(location.onSection(anotherSection));

        // the location is at a junction: check that it is not on a section
        // that does not have that junction at one of its end-points
        anotherSection =
                new Section(10, new JunctionBranch(new Junction("j3"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j4"),
                        Branch.FACING));
        Assert.assertFalse(location.onSection(anotherSection));

        // check that the string representation is correct
        String actualString = location.toString();
        String expectedString = "j1";
        Assert.assertEquals(expectedString, actualString);

        // check that the class invariant has been established.
        Assert.assertTrue("Invariant incorrect", location.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown if a location is created
     * with a null section or end-point.
     */
    @Test(timeout = 5000)
    public void testNullEndPointException() throws Exception {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = endPoint1;
        int offset = 3;

        // check that section parameter cannot be null
        try {
            new Location(null, endPoint, offset);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }

        // check that end-point parameter cannot be null
        try {
            new Location(section, null, offset);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a location is created
     * with an offset that is out of bounds.
     */
    @Test(timeout = 5000)
    public void testOffsetOutOfBoundsException() throws Exception {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = endPoint1;

        // check that the offset cannot be equal be negative
        try {
            new Location(section, endPoint, -1);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }

        // check that the offset cannot be equal to the section length
        try {
            new Location(section, endPoint, length);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }

        // check that the offset cannot be greater than the section length
        try {
            new Location(section, endPoint, length + 4);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a location is created
     * with an end-point that is not equivalent to an end-point of the given
     * section.
     * 
     * Check that no exception is thrown if a location is created with an
     * end-point that is equivalent to an end-point of the given section.
     */
    @Test(timeout = 5000)
    public void testInvalidEndPointException() throws Exception {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint1Copy =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j3"), Branch.REVERSE);

        // check for exception if end-point is not equivalent
        try {
            Section section = new Section(length, endPoint1, endPoint2);
            JunctionBranch endPoint = endPoint3;
            int offset = 3;

            // attempt to create the location under test
            new Location(section, endPoint, offset);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }

        // make sure that no exception is thrown if the end-point is equivalent.
        try {
            Section section = new Section(length, endPoint1, endPoint2);
            JunctionBranch endPoint = endPoint1Copy;
            int offset = 3;

            // attempt to create the location under test
            new Location(section, endPoint, offset);
        } catch (Exception e) {
            Assert.fail("Exception should not have been thrown.");
        }
    }

    /**
     * From hand-out:
     * 
     * Basic check of the equals method
     */
    @Test(timeout = 5000)
    public void testBasicEquals() throws Exception {

        // parameters used to construct the locations under test
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint1Copy =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.REVERSE);
        JunctionBranch endPoint4 =
                new JunctionBranch(new Junction("j3"), Branch.FACING);

        Section section1 = new Section(9, endPoint1, endPoint2);
        Section section2 = new Section(12, endPoint3, endPoint4);

        // equal case: the locations are both at the same junction
        Location location1 = new Location(section1, endPoint1, 0);
        Location location2 = new Location(section2, endPoint3, 0);
        Assert.assertEquals(location1, location2);

        // equal case: the locations are not at a junction: but they have
        // equivalent end-points and non-zero offsets.
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint1Copy, 3);
        Assert.assertEquals(location1, location2);

        // equal case: the locations are not at a junction: they are the same
        // location described from opposite end-points of the same section.
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint2, 6);
        Assert.assertEquals(location1, location2);
        Assert.assertEquals(location1.hashCode(), location2.hashCode());

        // unequal case: basic case
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint2, 4);
        Assert.assertFalse(location1.equals(location2));
    }

    /** Check that the equals method is correct: additional tests */
    @Test(timeout = 5000)
    public void testEquals() throws Exception {
        // parameters used to construct the locations under test
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint1Copy =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint2Copy =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.REVERSE);
        JunctionBranch endPoint4 =
                new JunctionBranch(new Junction("j3"), Branch.FACING);

        Section section1 = new Section(9, endPoint1, endPoint2);
        Section section1Copy = new Section(9, endPoint1Copy, endPoint2Copy);
        Section section2 = new Section(12, endPoint4, endPoint3);

        // equal case: the locations are not at a junction: they are the same
        // location described from opposite end-points of the same section.
        Location location1 = new Location(section1, endPoint1, 3);
        Location location2 = new Location(section1Copy, endPoint2Copy, 6);
        Assert.assertEquals(location1, location2);

        // unequal case: both locations are not at a junction, equal offsets
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint2, 3);
        Assert.assertFalse(location1.equals(location2));

        // unequal case: both locations are at different junctions
        location1 = new Location(section1, endPoint1, 0);
        location2 = new Location(section2, endPoint4, 0);
        Assert.assertFalse(location1.equals(location2));

        // unequal case: one location is at a junction and the other isn't
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section2, endPoint4, 0);
        Assert.assertFalse(location1.equals(location2));

        // unequal case: null
        location1 = new Location(section1, endPoint1, 3);
        Assert.assertFalse(location1.equals(null));

        // unequal case: not a location
        location1 = new Location(section1, endPoint1, 3);
        Assert.assertFalse(location1.equals("Delilah"));
    }

    /** Check that the hashCode method is correct. */
    @Test(timeout = 5000)
    public void testHashCode() throws Exception {
        // parameters used to construct the locations under test
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint1Copy =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint2Copy =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.REVERSE);
        JunctionBranch endPoint4 =
                new JunctionBranch(new Junction("j3"), Branch.FACING);

        Section section1 = new Section(9, endPoint1, endPoint2);
        Section section1Copy = new Section(9, endPoint1Copy, endPoint2Copy);
        Section section2 = new Section(12, endPoint4, endPoint3);

        // test that hash codes of equal objects are equal

        // equal case: the locations are both at the same junction
        Location location1 = new Location(section1, endPoint1, 0);
        Location location2 = new Location(section2, endPoint3, 0);
        Assert.assertEquals(location1.hashCode(), location2.hashCode());

        // equal case: the locations are not at a junction: but they have
        // equivalent end-points and non-zero offsets.
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint1Copy, 3);
        Assert.assertEquals(location1.hashCode(), location2.hashCode());

        // equal case: the locations are not at a junction: they are the same
        // location described from opposite end-points of the same section.
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1Copy, endPoint2Copy, 6);
        Assert.assertEquals(location1.hashCode(), location2.hashCode());

    }

}
