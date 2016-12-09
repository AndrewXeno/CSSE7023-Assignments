package railway.test;

import railway.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link Section} implementation class.
 */
public class CompleteSectionTest {

    /** Test construction of a typical section. */
    @Test(timeout = 5000)
    public void testTypicalSection() throws Exception {
        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        // the section under test
        Section section = new Section(length, endPoint1, endPoint2);

        // check that the length of the section is correct
        Assert.assertEquals(length, section.getLength());

        // check that the end points of the section are correct
        Set<JunctionBranch> actualEndPoints = section.getEndPoints();
        Assert.assertTrue(actualEndPoints.size() == 2
                && actualEndPoints.contains(endPoint1)
                && actualEndPoints.contains(endPoint2));

        // check that the class invariant has been established.
        Assert.assertTrue("Invariant incorrect", section.checkInvariant());
    }

    /**
     * Test construction of a section that loops from one junction back to
     * itself
     */
    @Test(timeout = 5000)
    public void testLoopingSection() throws Exception {
        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j1"), Branch.NORMAL);

        // the section under test
        Section section = new Section(length, endPoint1, endPoint2);

        // check that the length of the section is correct
        Assert.assertEquals(length, section.getLength());

        // check that the end points of the section are correct
        Set<JunctionBranch> actualEndPoints = section.getEndPoints();
        Assert.assertTrue(actualEndPoints.size() == 2
                && actualEndPoints.contains(endPoint1)
                && actualEndPoints.contains(endPoint2));

        // check that the class invariant has been established.
        Assert.assertTrue("Invariant incorrect", section.checkInvariant());
    }

    /**
     * Test otherEndPoint method works correctly
     */
    @Test(timeout = 5000)
    public void testOtherEndPoint() throws Exception {
        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.NORMAL);
        Section section; // the section under test

        // check the otherEndPoint method for a non-looping section
        section = new Section(length, endPoint1, endPoint2);
        Assert.assertEquals(endPoint2, section.otherEndPoint(endPoint1));
        Assert.assertEquals(endPoint1, section.otherEndPoint(endPoint2));

        // check the otherEndPoint method for a looping section
        section = new Section(length, endPoint1, endPoint3);
        Assert.assertEquals(endPoint3, section.otherEndPoint(endPoint1));
        Assert.assertEquals(endPoint1, section.otherEndPoint(endPoint3));
    }

    /**
     * Test otherEndPoint method throws appropriate exceptions
     */
    @Test(timeout = 5000)
    public void testOtherEndPointExceptions() throws Exception {
        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint1Copy =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.NORMAL);
        Section section; // the section under test

        // check if end-point is not equivalent to one of section's end-points
        section = new Section(length, endPoint1, endPoint2);
        try {
            section.otherEndPoint(endPoint3);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }

        // check if end-point is equivalent to one of section's end-points
        try {
            Assert.assertEquals(endPoint2, section.otherEndPoint(endPoint1Copy));
        } catch (Exception e) {
            Assert.fail("No exception should be thrown.");
        }
    }

    /**
     * Test the toString method works correctly
     */
    @Test(timeout = 5000)
    public void testToString() throws Exception {
        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.NORMAL);
        Section section; // the section under test

        // check the string representation for a non-looping section
        section = new Section(length, endPoint1, endPoint2);
        String actualString = section.toString();
        String expectedString1 = "9 (j1, FACING) (j2, NORMAL)";
        String expectedString2 = "9 (j2, NORMAL) (j1, FACING)";
        Assert.assertTrue(expectedString1.equals(actualString)
                || expectedString2.equals(actualString));

        // check the string representation for a looping section
        section = new Section(length, endPoint1, endPoint3);
        actualString = section.toString();
        expectedString1 = "9 (j1, FACING) (j1, NORMAL)";
        expectedString2 = "9 (j1, NORMAL) (j1, FACING)";
        Assert.assertTrue(expectedString1.equals(actualString)
                || expectedString2.equals(actualString));

    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with a null end-point.
     */
    @Test(timeout = 5000)
    public void testNullEndPointException() throws Exception {
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        // check that first end-point parameter cannot be null
        try {
            new Section(length, null, endPoint2);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }

        // check that second end-point parameter cannot be null
        try {
            new Section(length, endPoint1, null);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with a length that is less than or equal to zero.
     */
    @Test(timeout = 5000)
    public void testNonPositiveLengthException() throws Exception {
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        // check that the section length cannot be zero
        try {
            new Section(0, endPoint1, endPoint2);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }

        // check that the section length cannot be negative
        try {
            new Section(-10, endPoint1, endPoint2);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with two equivalent end-points.
     */
    @Test(expected = IllegalArgumentException.class, timeout = 5000)
    public void testEqualEndpointsException() throws Exception {
        int length = 11;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);

        new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the method getEndPoints does not expose internal state of the
     * class.
     */
    @Test(timeout = 5000)
    public void testInvariantProtected() throws Exception {

        // input parameters to the section under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        // the section under test
        Section section = new Section(length, endPoint1, endPoint2);

        // get the end points of the section and modify the returned set to
        // remove all of its elements
        Set<JunctionBranch> actualEndPoints1 = section.getEndPoints();
        try {
            actualEndPoints1.clear();
        } catch (UnsupportedOperationException e) {
            // OK if the returned set is immutable
        }

        // check that the end points of the section are still correct
        Set<JunctionBranch> actualEndPoints2 = section.getEndPoints();
        Assert.assertTrue(actualEndPoints2.size() == 2
                && actualEndPoints2.contains(endPoint1)
                && actualEndPoints2.contains(endPoint2));

    }

    /** Check that the equals method is correct. */
    @Test(timeout = 5000)
    public void testEquals() throws Exception {

        // sections to test for equality
        Section s1 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Section s2 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Object s3 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Section s4 =
                new Section(5, new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j1"),
                        Branch.FACING));
        Section s5 =
                new Section(11, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Section s6 =
                new Section(5, new JunctionBranch(new Junction("j4"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Section s7 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.REVERSE), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));

        // test equals cases
        Assert.assertTrue("Equals should return true", s1.equals(s2));
        Assert.assertTrue("Equals should return true", s1.equals(s3));
        Assert.assertTrue("Equals should return true", s1.equals(s4));

        // test unequal cases
        Assert.assertFalse("Equals should return false", s1.equals(null));
        Assert.assertFalse("Equals should return false", s1.equals("Delilah"));
        Assert.assertFalse("Equals should return false", s1.equals(s5));
        Assert.assertFalse("Equals should return false", s1.equals(s6));
        Assert.assertFalse("Equals should return false", s1.equals(s7));
    }

    /** Check that the hashCode method is correct. */
    @Test(timeout = 5000)
    public void testHashCode() throws Exception {

        // sections to test for equality
        Section s1 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));
        Section s2 =
                new Section(5, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL));

        Section s4 =
                new Section(5, new JunctionBranch(new Junction("j2"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j1"),
                        Branch.FACING));

        // test that hash codes of equal objects are equal

        Assert.assertEquals("Hash codes should be same for equal objects", s1
                .hashCode(), s2.hashCode());

        Assert.assertEquals("Hash codes should be same for equal objects", s1
                .hashCode(), s4.hashCode());

    }

}
