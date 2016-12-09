package railway;

import java.util.*;

/**
 * <p>
 * An immutable class corresponding to a section of a railway track.
 * </p>
 * 
 * <p>
 * A section of track has a positive length (in meters), and lies between two
 * junctions. These junctions may not be distinct, since a section of track may
 * form a loop from one junction back to the same junction.
 * </p>
 * 
 * <p>
 * In the context of a particular railway track, each junction has between one
 * and three branches (of type Branch) that connect it to sections of the track.
 * It can have at most one branch of each type. (I.e. a junction may not have
 * two branches of type Branch.FACING.)
 * </p>
 * 
 * <p>
 * If a section forms a loop from one junction back to itself, then the junction
 * must be connected to the section on two different branches.
 * </p>
 * 
 * <p>
 * A section is uniquely identified by its length and two distinct end-points,
 * where an end-point is a junction and the branch that connects it to the
 * section.
 * </p>
 */
public class Section {

    // the length and the two end-points of the section
    private int length;
    private JunctionBranch endPoint1;
    private JunctionBranch endPoint2;

    /*
     * invariant:
     *
     * length > 0
     *
     * && endPoint1 != null
     *
     * && endPoint2 != null
     *
     * && endPoint1 != endPoint2
     */

    /**
     * Creates a new section with the given length (in meters) and end-points.
     * 
     * @param length
     *            a positive integer representing the length of the section in
     *            meters
     * @param endPoint1
     *            one end-point of the section
     * @param endPoint2
     *            the other end-point of the section
     * @throws NullPointerException
     *             if either end-point is null
     * @throws IllegalArgumentException
     *             if either the length is less than or equal to zero, or the
     *             end-points are equivalent (two end-points are equivalent if
     *             they are equal according to the equals method of the
     *             JunctionBranch class).
     */
    public Section(int length, JunctionBranch endPoint1,
            JunctionBranch endPoint2) throws NullPointerException,
                    IllegalArgumentException {
        if (endPoint1 == null || endPoint2 == null)
            throw new NullPointerException("end-points cannot be null");
        if (length <= 0)
            throw new IllegalArgumentException("length cannot be non-positive");
        if (endPoint1.equals(endPoint2))
            throw new IllegalArgumentException("end-points cannot be "
                    + "equivalent");
        this.length = length;
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
    }

    /**
     * Returns the length of the section (in meters).
     * 
     * @return the length of the section
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Returns the end-points of the section.
     * 
     * @return a set of the end-points of the section.
     */
    public Set<JunctionBranch> getEndPoints() {
        // the HashSet that will be returned as the result
        Set<JunctionBranch> endPoints = new HashSet<JunctionBranch>();
        endPoints.add(endPoint1);
        endPoints.add(endPoint2);
        return endPoints;
    }

    /**
     * If the given end-point is equivalent to an end-point of the section, then
     * it returns the end-point at the opposite end of the section. Otherwise
     * this method throws an IllegalArgumentException.
     * 
     * @param endPoint
     *            the given end-point of this section
     * @throws IllegalArgumentException
     *             if the given end-point is not an equivalent to an end-point
     *             of the given section.
     * @return the end-point at the opposite end of the section to endPoint
     */
    public JunctionBranch otherEndPoint(JunctionBranch endPoint) {
        if (this.endPoint1.equals(endPoint)) {
            return this.endPoint2;
        } else if (this.endPoint2.equals(endPoint)) {
            return this.endPoint1;
        } else {
            throw new IllegalArgumentException(
                    "end-point is not an equivalent to an end-point of "
                            + "the section");
        }
    }

    /**
     * <p>
     * Returns the string representation of the section. The string
     * representation consists of the length, followed by the single space
     * character ' ', followed by the toString() representation of one of the
     * end-points, followed by the single space character ' ', followed by the
     * toString() representation of the other end-point.
     * </p>
     * 
     * <p>
     * The end-points can occur in any order, so that either the string
     * "9 (j1, FACING) (j2, NORMAL)" or the string
     * "9 (j2, NORMAL) (j1, FACING)", would be valid string representations of a
     * section of length 9, with end-points "(j1, FACING)" and "(j2, NORMAL)".
     * </p>
     */
    @Override
    public String toString() {
        // the String to be returned
        String s = new String();
        s = length + " " + endPoint1.toString() + " "
                + endPoint2.toString();
        return s;
    }

    /**
     * <p>
     * Returns true if and only if the given object is an instance of the class
     * Section with the same length as this one, and equivalent end-points.
     * </p>
     * 
     * <p>
     * The end-points of Section a and Section b are equivalent if and only if,
     * for each end-point of a, there is an equivalent end-point of b. (Two
     * end-points are equivalent if their junctions and branches are equivalent
     * as per the equals method in the JunctionBranch class).
     * </p>
     */
    @Override
    public boolean equals(Object object) {
        // if object is not an instance of Section, return false
        if (!(object instanceof Section)) {
            return false;
        }
        // cast the given object into Section,
        // representing the section to be compared with
        Section other = (Section) object;
        if (this.length == other.length) {
            if (other.endPoint1.equals(this.endPoint1)
                    && other.endPoint2.equals(this.endPoint2)) {
                return true;
            } else if (other.endPoint1.equals(this.endPoint2)
                    && other.endPoint2.equals(this.endPoint1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // p1 p2 are two randomly chosen odd prime numbers
        final int p1 = 19;
        final int p2 = 65537;
        // the result to be returned. will be modified during the calculation
        int result = p1;
        result = p2 * result + length;
        // always calculate the smaller hash code first to make sure the result
        // is not affected by the end-points' order
        result = p2 * result + Math.min(endPoint1.hashCode(),
                endPoint2.hashCode());
        result = p2 * result + Math.max(endPoint1.hashCode(),
                endPoint2.hashCode());
        return result;
    }

    /**
     * Determines whether this class is internally consistent (i.e. it satisfies
     * its class invariant).
     * 
     * This method is only intended for testing purposes.
     * 
     * @return true if this class is internally consistent, and false otherwise.
     */
    public boolean checkInvariant() {
        // check if length is less or equal to 0
        if (length <= 0)
            return false;
        // check if there are null end-points
        if (endPoint1 == null || endPoint2 == null)
            return false;
        // check if two end-points are equivalent
        if (endPoint1.equals(endPoint2))
            return false;
        return true;
    }
}
