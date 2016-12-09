package railway.test;

import railway.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Section} implementation class.
 * 
 * Write your own junit4 tests for the class here.
 */
public class SectionTest {

    /** Typical test the Section class with valid parameters */
    @Test
    public void testNormalSection() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);

        // check getLength method
        Assert.assertEquals(length, section.getLength());
        // check getEndPoints method
        Set<JunctionBranch> endPoints = new HashSet<JunctionBranch>();
        endPoints.add(endPoint1);
        endPoints.add(endPoint2);
        Assert.assertEquals(endPoints, section.getEndPoints());
        Assert.assertTrue(section.getEndPoints().contains(endPoint1));
        Assert.assertTrue(section.getEndPoints().contains(endPoint2));

        // check otherEndPoint method
        Assert.assertEquals(endPoint1, section.otherEndPoint(endPoint2));
        Assert.assertEquals(endPoint2, section.otherEndPoint(endPoint1));
        // check toString representation
        Assert.assertTrue(
                section.toString().equals("9 (j1, FACING) (j2, NORMAL)") ||
                        section.toString()
                                .equals("9 (j2, NORMAL) (j1, FACING)"));
        // check invariants
        Assert.assertTrue(section.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with a negative length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        int length = -9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with zero length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroLength() {
        int length = 0;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with null endPoint1
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPoint1() {
        int length = 9;
        JunctionBranch endPoint1 = null;
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with null endPoint2
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPoint2() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = null;
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with 2 null end-points
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPoints() {
        int length = 9;
        JunctionBranch endPoint1 = null;
        JunctionBranch endPoint2 = null;
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with 2 equivalent end-points
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEquivalentEndPoints() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);
    }

    /**
     * Check that the appropriate exception is thrown if a section is created
     * with 2 same end-points
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSameEndPoints() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint1);
    }

    /** Test otherEndPoint method with null parameter */
    @Test(expected = IllegalArgumentException.class)
    public void testOtherEndPointNull() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        JunctionBranch endPoint3 = null;
        Section section = new Section(length, endPoint1, endPoint2);
        section.otherEndPoint(endPoint3);
    }

    /**
     * Test otherEndPoint method if the given end-point is not an equivalent
     * to an end-point of the given section
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOtherEndPointNoneEquivalent() {
        int length = 9;
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        JunctionBranch endPoint3 = new JunctionBranch(new Junction("j3"),
                Branch.NORMAL);
        Section section = new Section(length, endPoint1, endPoint2);
        section.otherEndPoint(endPoint3);
    }

    /** Typical check of the equals and hashCode method */
    @Test
    public void testEquals() {
        JunctionBranch endPoint1 = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2 = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        JunctionBranch endPoint1b = new JunctionBranch(new Junction("j1"),
                Branch.FACING);
        JunctionBranch endPoint2b = new JunctionBranch(new Junction("j2"),
                Branch.NORMAL);
        JunctionBranch endPoint3 = new JunctionBranch(new Junction("j1"),
                Branch.REVERSE);
        JunctionBranch endPoint4 = new JunctionBranch(new Junction("j3"),
                Branch.FACING);

        // equal case: both sections have the same end-points
        // and in the same order
        Section section1 = new Section(9, endPoint1, endPoint2);
        Section section2 = new Section(9, endPoint1b, endPoint2b);
        Assert.assertEquals(section1, section2);
        Assert.assertEquals(section1.hashCode(), section2.hashCode());

        // equal case: sections have the same end-points
        // but in different order
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(9, endPoint2b, endPoint1b);
        Assert.assertEquals(section1.hashCode(), section2.hashCode());

        // unequal case: one of the section is null
        Assert.assertNotEquals(null, section1);
        Assert.assertNotEquals(section1, null);

        // unequal case: base case
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(19, endPoint3, endPoint4);
        Assert.assertNotEquals(section1, section2);

        // unequal case: sections with equivalent end-points
        // but different lengths
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(8, endPoint1b, endPoint2b);
        Assert.assertNotEquals(section1, section2);

        // unequal case: sections with same lengths but different end-points
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(9, endPoint3, endPoint4);
        Assert.assertNotEquals(section1, section2);

        // unequal case: sections with same lengths and endPoint1
        // but different endPoint2
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(9, endPoint1, endPoint4);
        Assert.assertNotEquals(section1, section2);

        // unequal case: sections with same lengths and endPoint2
        // but different endPoint1
        section1 = new Section(9, endPoint1, endPoint2);
        section2 = new Section(9, endPoint4, endPoint2);
        Assert.assertNotEquals(section1, section2);
    }
}
