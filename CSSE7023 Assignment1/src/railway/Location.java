package railway;

/**
 * <p>
 * An immutable class representing a location on a railway track.
 * </p>
 * 
 * <p>
 * A location on a railway track may be at either at a junction in the track, or
 * on a section of the track between the two end-points of the section.
 * </p>
 * 
 * <p>
 * Note: A location at a junction lies on all of the sections of track that are
 * connected to the junction; a location that is not at a junction, lies on only
 * one section of track.
 * </p>
 * 
 * <p>
 * A location can be identified by its offset with respect to an end-point of a
 * section of the track. However, the same location can be identified by
 * different descriptions. For example, given a section s of length 10 and with
 * end-points (j1, FACING) and (j2, REVERSE), the same location may equivalently
 * be described as being either 3 meters from junction j1 along its FACING
 * branch, or 7 meters from junction j2 along its REVERSE branch. A location
 * which is to be found at a junction, is represented as having a 0 offset from
 * the junction along any branch of the junction. For this reason, the
 * equivalence method of this class is more complex than usual.
 * </p>
 */
public class Location {

    // the section the location lies on
    private Section section;
    // the end-point of the section
    private JunctionBranch endPoint;
    // the distance of the location from the end-point of the section
    private int offset;

    /*
     * invariant:
     *
     * section != null
     *
     * && endPoint != null
     *
     * && offset >= 0 && offset < section.length
     *
     * && section.getEndPoints().contains(endPoint)
     */
    // INVARIANT HERE

    /**
     * Creates a new location that lies on the given section at a distance of
     * offset meters from endPoint.getJunction() along endPoint.getBranch().
     * 
     * @param section
     *            a section that the location lies on
     * @param endPoint
     *            an end-point of the given section
     * @param offset
     *            the distance of the location from the given end-point of the
     *            section that it lies on. The distance must be greater than or
     *            equal to zero, and strictly less than the length of the
     *            section.
     * @throws NullPointerException
     *             if either parameter section or endPoint is null.
     * @throws IllegalArgumentException
     *             if (i) offset is either a negative value or if it is greater
     *             than or equal to the length of the given section, or (ii)
     *             section and endPoint are not null, but parameter endPoint is
     *             not equivalent to an end-point of the given section.
     */
    public Location(Section section, JunctionBranch endPoint, int offset) {
        if (section == null)
            throw new NullPointerException("section cannot be null");
        if (endPoint == null)
            throw new NullPointerException("endPoint cannot be null");
        if (offset < 0)
            throw new IllegalArgumentException("offset cannot be negative");
        if (offset >= section.getLength())
            throw new IllegalArgumentException(
                    "offset cannot be >= the length of the section");
        if (!section.getEndPoints().contains(endPoint)) {
            throw new IllegalArgumentException(
                    "endPoint is not equivalent to an end-point of the given "
                            + "section");
        }
        this.section = section;
        this.endPoint = endPoint;
        this.offset = offset;
    }

    /**
     * <p>
     * Returns a section of the track that this location lies on. Note that a
     * location at a junction may lie on multiple sections, and this method only
     * returns one of them: the section that this method was constructed with.
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of
     * this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return a section that this location lies on
     */
    public Section getSection() {
        return this.section;
    }

    /**
     * <p>
     * This method returns an end-point of the section this.getSection(), that
     * this location lies on. The end-point that is returned is the one that
     * this class was constructed with.
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of
     * this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return an end-point of this.getSection()
     */
    public JunctionBranch getEndPoint() {
        return this.endPoint;
    }

    /**
     * <p>
     * This method returns the offset of this location with respect to the
     * end-point this.getEndPoint() that lies on the track section
     * this.getSection().
     * </p>
     * 
     * <p>
     * Equivalent locations can be equivalently described by different offsets
     * and end-points. Methods getOffset() and getEndPoint() return the offset
     * and end-point that this location was constructed with.
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of
     * this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return the offset of this location with respect to this.getEndPoint()
     * 
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * Returns true if this location is at a junction (i.e. it has an offset of
     * zero with respect to the end-point of a section).
     * 
     * @return whether or not this location is at a junction
     */
    public boolean atAJunction() {
        if (this.offset == 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Returns true if this location lies on the given section.
     * </p>
     * 
     * <p>
     * This location lies on a section if and only if either (i) it is at a
     * junction on an end-point of the given section or (ii) it is not at a
     * junction, and the given section is equivalent to this.getSection().
     * </p>
     * 
     * @param section
     *            the section to check
     * @return true iff this location lies on the given section
     */
    public boolean onSection(Section section) {
        if (this.atAJunction()) {
            // the junction that the location is on
            Junction junction = this.getEndPoint().getJunction();
            // check if the junction is equal to any junction of end-points
            // of the given section
            for (JunctionBranch jb : section.getEndPoints()) {
                if (jb.getJunction().equals(junction)) {
                    return true;
                }
            }
            return false;
        } else {
            return section.equals(this.getSection());
        }
    }

    /**
     * <p>
     * If this location is at a junction (i.e. it has an offset of zero from the
     * end-point of a section), then this method returns the toString()
     * representation of the junction where this location lies.
     * 
     * Otherwise, if the location is not at a junction, this method returns a
     * string of the form: <br>
     * <br>
     * 
     * "Distance OFFSET from JUNCTION along the BRANCH branch" <br>
     * <br>
     * 
     * where OFFSET is this.getOffset(), JUNCTION is the toString()
     * representation of the junction of this.getEndPoint(), and BRANCH is the
     * toString() representation of the branch of this.getEndPoint().
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of
     * this
     * class may have different return-values of this method.
     * </p>
     */
    @Override
    public String toString() {
        if (this.atAJunction()) {
            // the String to be returned
            String s = this.getEndPoint().getJunction().toString();
            return s;
        } else {
            // the offset of the location
            int offset = this.getOffset();
            // the Junction of the location
            String junction = this.getEndPoint().getJunction().toString();
            // the Branch of the location
            String branch = this.getEndPoint().getBranch().toString();
            return String.format("Distance %d from %s along the %s branch",
                    offset, junction, branch);
        }
    }

    /**
     * <p>
     * Two locations are equivalent if either: <br>
     * <br>
     * 
     * (i) their offsets are both zero and their end-points are at the same
     * junction (two junctions are considered to be the same if they are
     * equivalent according to the equals method of the Junction class) or <br>
     * <br>
     * 
     * (ii) if their end-points are equivalent and their offsets are equal, or
     * <br>
     * <br>
     * 
     * (iii) if their end-points are not equivalent, but they lie on the same
     * section, and the sum of the length of their offsets equals the length of
     * the section that they lie on. (Two sections are considered to be the same
     * if they are equal according to the equals method of the Section class.)
     * <br>
     * <br>
     * 
     * and they are not equivalent otherwise.
     * </p>
     * 
     * <p>
     * This method returns true if and only if the given object is an instance
     * of the class Location, and the locations are equivalent according to the
     * above definition.
     * </p>
     */
    @Override
    public boolean equals(Object object) {
        // if object is not an instance of Location, return false
        if (!(object instanceof Location)) {
            return false;
        }
        // cast the given object into Location,
        // representing the Location to be compared with
        Location other = (Location) object;
        // case 1: both location have zero offset
        if (this.offset == 0 && other.offset == 0) {
            // the junction that the location lies on
            Junction thisJunction = this.getEndPoint().getJunction();
            // the junction that the given location lies on
            Junction otherJunction = other.getEndPoint().getJunction();
            if (thisJunction.equals(otherJunction)) {
                return true;
            }
            // case 2: end-points are equivalent and offsets are equal
        } else if (this.getEndPoint().equals(other.getEndPoint())) {
            if (this.getOffset() == other.getOffset()) {
                return true;
            }
            // case 3: different end-points but on the same section,
            // check if the sum of offsets equals the length of the section
        } else if (this.getSection().equals(other.getSection())) {
            // the length of the section that they are on
            int len = this.getSection().getLength();
            if (this.getOffset() + other.getOffset() == len) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // p1 p2 are two randomly chosen odd prime numbers
        int p1 = 19;
        int p2 = 65537;
        // the result to be returned. will be modified during the calculation
        int result = p1;
        // the location's offset from the other end-point
        int otherOffset = section.getLength() - offset;
        // the end-point that the location is constructed with
        JunctionBranch thisEndpoint = this.getEndPoint();
        // the other end-point in the section
        JunctionBranch otherEndpoint = section.otherEndPoint(thisEndpoint);
        // the junction of the end-point that the location is constructed with
        Junction thisJunction = thisEndpoint.getJunction();
        // the junction of the other end-point in the section
        Junction otherJunction = otherEndpoint.getJunction();
        // case 1: if the location is at the middle point of the section
        // use the junction whose hashCode is smaller to calculate hashCode
        // (offset is equal so that either one can be used in the calculation)
        if (offset == otherOffset) {
            result = p2 * result + Math.min(thisJunction.hashCode(),
                    otherJunction.hashCode());
            result = p2 * result + offset;
            return result;
            // case 2: the location is close to the end-point that it is
            // constructed with.
            // use the junction of the original end-point to calculate hashCode
            // (corresponding offset is used in the calculation)
        } else if (offset < otherOffset) {
            result = p2 * result + thisJunction.hashCode();
            result = p2 * result + offset;
            return result;
            // case 3: the location is close to the other end-point
            // use the junction of the other end-point to calculate hashCode
            // (corresponding offset (i.e. otherOffset) is used in the
            // calculation)
        } else {
            result = p2 * result + otherJunction.hashCode();
            result = p2 * result + otherOffset;
            return result;
        }
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
        // check if the section is null
        if (section == null)
            return false;
        // check if the end-point is null
        if (endPoint == null)
            return false;
        // check if the offset is negative
        if (offset < 0)
            return false;
        // check if the offset is greater or equal to the section length
        if (offset >= section.getLength())
            return false;
        // check if the section contains the end-point
        if (!section.getEndPoints().contains(endPoint))
            return false;
        return true;
    }

}
