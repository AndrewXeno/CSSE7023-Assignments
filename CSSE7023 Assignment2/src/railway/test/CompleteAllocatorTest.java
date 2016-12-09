package railway.test;

import railway.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link Allocator} implementation class.
 */
public class CompleteAllocatorTest {

    /*
     * The end-points and sections of the track to be used in the tests in this
     * suite. (This isn't by any means the only track layout possible, but we
     * will use the one track for these particular tests for simplicity. Your
     * code should work for any track).
     */
    private JunctionBranch[] endPoints = {
            new JunctionBranch(new Junction("j0"), Branch.NORMAL),
            new JunctionBranch(new Junction("j1"), Branch.FACING),
            new JunctionBranch(new Junction("j1"), Branch.REVERSE),
            new JunctionBranch(new Junction("j2"), Branch.FACING),
            new JunctionBranch(new Junction("j1"), Branch.NORMAL),
            new JunctionBranch(new Junction("j3"), Branch.FACING),
            new JunctionBranch(new Junction("j2"), Branch.NORMAL),
            new JunctionBranch(new Junction("j3"), Branch.NORMAL) };

    private Section[] sections = { new Section(10, endPoints[0], endPoints[1]),
            new Section(12, endPoints[2], endPoints[3]),
            new Section(7, endPoints[4], endPoints[5]),
            new Section(10, endPoints[6], endPoints[7]) };

    /*
     * Another track for testing
     */
    private JunctionBranch[] endPoints2 = {
            new JunctionBranch(new Junction("j0"), Branch.FACING),
            new JunctionBranch(new Junction("j1"), Branch.NORMAL),

            new JunctionBranch(new Junction("j1"), Branch.FACING),
            new JunctionBranch(new Junction("j2"), Branch.NORMAL),

            new JunctionBranch(new Junction("j3"), Branch.NORMAL),
            new JunctionBranch(new Junction("j4"), Branch.FACING),

            new JunctionBranch(new Junction("j4"), Branch.NORMAL),
            new JunctionBranch(new Junction("j5"), Branch.FACING),

            new JunctionBranch(new Junction("j3"), Branch.FACING),
            new JunctionBranch(new Junction("j0"), Branch.NORMAL),

            new JunctionBranch(new Junction("j4"), Branch.REVERSE),
            new JunctionBranch(new Junction("j1"), Branch.REVERSE),

            new JunctionBranch(new Junction("j5"), Branch.REVERSE),
            new JunctionBranch(new Junction("j2"), Branch.FACING) };

    private Section[] sections2 = {
            new Section(15, endPoints2[0], endPoints2[1]),
            new Section(10, endPoints2[2], endPoints2[3]),
            new Section(20, endPoints2[4], endPoints2[5]),
            new Section(8, endPoints2[6], endPoints2[7]),
            new Section(11, endPoints2[8], endPoints2[9]),
            new Section(12, endPoints2[10], endPoints2[11]),
            new Section(13, endPoints2[12], endPoints2[13]) };

    /**
     * Basic test: two trains: routes with only one segment each: routes on one
     * section only: requested allocations are possible.
     */
    @Test(timeout = 5000)
    public void basicTest01() throws Exception {
        basicTest01a();
        basicTest01b();
    }

    /**
     * Basic test: two trains: routes with only one segment each: routes on one
     * section only: routes in same direction on section: requested allocations
     * are possible.
     */
    public void basicTest01a() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[0], endPoints[0], 6, 8) } };

        Segment[][] requestedArray =
                { { new Segment(sections[0], endPoints[0], 3, 5) },
                        { new Segment(sections[0], endPoints[0], 7, 9) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections[0], endPoints[0], 3, 5) },
                        { new Segment(sections[0], endPoints[0], 7, 9) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: two trains: routes with only one segment each: routes on one
     * section only: routes in different directions on section: requested
     * allocations are possible.
     */
    public void basicTest01b() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections[0], endPoints[0], 1, 3) },
                        { new Segment(sections[0], endPoints[1], 0, 3) } };

        Segment[][] requestedArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[0], endPoints[1], 1, 4) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[0], endPoints[1], 1, 4) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: two trains: routes with only one segment each: routes on one
     * section only: routes in same direction on section: only part of the
     * requested allocations are possible.
     */
    @Test(timeout = 5000)
    public void basicTest02() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections[0], endPoints[0], 0, 3) },
                        { new Segment(sections[0], endPoints[0], 6, 9) } };

        Segment[][] requestedArray =
                { { new Segment(sections[0], endPoints[0], 2, 7) },
                        { new Segment(sections[0], endPoints[0], 8, 10) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections[0], endPoints[0], 2, 5) },
                        { new Segment(sections[0], endPoints[0], 8, 10) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: two trains: routes with only one segment each: routes on one
     * section only: routes in different directions on section: only part of the
     * requested allocations are possible.
     */
    @Test(timeout = 5000)
    public void basicTest04() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections[0], endPoints[0], 1, 3) },
                        { new Segment(sections[0], endPoints[1], 0, 3) } };

        Segment[][] requestedArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[0], endPoints[1], 1, 7) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[0], endPoints[1], 1, 5) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: many trains: routes with only one segment each: routes on
     * many different sections: only part of the requested allocations are
     * possible.
     */
    @Test(timeout = 5000)
    public void basicTest05() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections[0], endPoints[0], 2, 4) },
                        { new Segment(sections[1], endPoints[3], 8, 10) },
                        { new Segment(sections[2], endPoints[5], 3, 5) },
                        { new Segment(sections[2], endPoints[5], 0, 2) } };

        Segment[][] requestedArray =
                { { new Segment(sections[0], endPoints[0], 3, 6) },
                        { new Segment(sections[1], endPoints[3], 9, 12) },
                        { new Segment(sections[2], endPoints[5], 3, 7) },
                        { new Segment(sections[2], endPoints[5], 0, 4) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections[0], endPoints[0], 3, 6) },
                        { new Segment(sections[1], endPoints[3], 9, 12) },
                        { new Segment(sections[2], endPoints[5], 3, 6) },
                        { new Segment(sections[2], endPoints[5], 0, 2) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: two trains: routes might contain more than one segment each
     */
    @Test(timeout = 5000)
    public void basicTest06() throws Exception {
        basicTest06a();
        basicTest06b();
    }

    /**
     * Basic test: two trains: routes might contain more than one segment each:
     * requested allocations are possible.
     */
    public void basicTest06a() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                {
                        { new Segment(sections[0], endPoints[0], 4, 7) },
                        { new Segment(sections[1], endPoints[2], 10, 12),
                                new Segment(sections[3], endPoints[6], 0, 4) } };

        Segment[][] requestedArray =
                {
                        { new Segment(sections[0], endPoints[0], 6, 10),
                                new Segment(sections[1], endPoints[2], 0, 3) },
                        { new Segment(sections[3], endPoints[6], 2, 7) } };

        Segment[][] expectedAllocationArray =
                {
                        { new Segment(sections[0], endPoints[0], 6, 10),
                                new Segment(sections[1], endPoints[2], 0, 3) },
                        { new Segment(sections[3], endPoints[6], 2, 7) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * Basic test: two trains: routes might contain more than one segment each:
     * requested allocations are partially possible.
     */
    public void basicTest06b() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                {
                        { new Segment(sections[0], endPoints[0], 6, 10),
                                new Segment(sections[1], endPoints[2], 0, 3) },
                        { new Segment(sections[2], endPoints[4], 5, 7),
                                new Segment(sections[3], endPoints[7], 0, 1) } };

        Segment[][] requestedArray =
                {
                        { new Segment(sections[1], endPoints[2], 2, 12),
                                new Segment(sections[3], endPoints[6], 0, 5) },
                        { new Segment(sections[3], endPoints[7], 0, 10),
                                new Segment(sections[1], endPoints[3], 0, 5) } };

        Segment[][] expectedAllocationArray =
                {
                        { new Segment(sections[1], endPoints[2], 2, 12),
                                new Segment(sections[3], endPoints[6], 0, 5) },
                        { new Segment(sections[3], endPoints[7], 0, 4) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: test with zero trains, test with one train.
     */
    @Test(timeout = 5000)
    public void test07() throws Exception {
        test07a();
        test07b();
    }

    /**
     * test with alternative track: zero trains.
     */
    public void test07a() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray = {};

        Segment[][] requestedArray = {};

        Segment[][] expectedAllocationArray = {};

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: one train: routes might contain more than
     * one segment each: there can be no conflicts and so the allocation is
     * possible.
     */
    public void test07b() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections2[6], endPoints2[12], 10, 13),
                        new Segment(sections2[1], endPoints2[3], 0, 10),
                        new Segment(sections2[5], endPoints2[11], 0, 11) } };

        Segment[][] requestedArray =
                { { new Segment(sections2[5], endPoints2[11], 11, 12),
                        new Segment(sections2[2], endPoints2[5], 0, 5) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections2[5], endPoints2[11], 11, 12),
                        new Segment(sections2[2], endPoints2[5], 0, 5) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: many trains: routes contain one segment
     * each: allocations partially possible.
     */
    @Test(timeout = 5000)
    public void test08() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                { { new Segment(sections2[2], endPoints2[4], 0, 4) },
                        { new Segment(sections2[5], endPoints2[11], 5, 11) },
                        { new Segment(sections2[2], endPoints2[4], 6, 15) },
                        { new Segment(sections2[5], endPoints2[11], 0, 3) } };

        Segment[][] requestedArray =
                { { new Segment(sections2[2], endPoints2[4], 2, 20), },
                        { new Segment(sections2[5], endPoints2[11], 10, 12), },
                        { new Segment(sections2[2], endPoints2[4], 10, 18) },
                        { new Segment(sections2[5], endPoints2[11], 2, 10) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections2[2], endPoints2[4], 2, 5) },
                        { new Segment(sections2[5], endPoints2[11], 10, 12) },
                        { new Segment(sections2[2], endPoints2[4], 10, 18) },
                        { new Segment(sections2[5], endPoints2[11], 2, 4) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: many trains: routes might contain more than
     * one segment each: allocations partially possible.
     */
    @Test(timeout = 5000)
    public void test09() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                {
                        { new Segment(sections2[1], endPoints2[2], 8, 10),
                                new Segment(sections2[6], endPoints2[13], 0, 2) },
                        { new Segment(sections2[3], endPoints2[6], 2, 8),
                                new Segment(sections2[6], endPoints2[12], 0, 3) } };

        Segment[][] requestedArray =
                {
                        { new Segment(sections2[6], endPoints2[13], 1, 12) },
                        { new Segment(sections2[6], endPoints2[12], 2, 13),
                                new Segment(sections2[1], endPoints2[3], 0, 5) } };

        Segment[][] expectedAllocationArray =
                { { new Segment(sections2[6], endPoints2[13], 1, 9) },
                        { new Segment(sections2[6], endPoints2[12], 2, 3) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: many trains: routes might contain more than
     * one segment each: allocations partially possible.
     */
    @Test(timeout = 5000)
    public void test10() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                {
                        { new Segment(sections2[4], endPoints2[9], 8, 11),
                                new Segment(sections2[2], endPoints2[4], 0, 4) },
                        {
                                new Segment(sections2[6], endPoints2[12], 10,
                                        13),
                                new Segment(sections2[1], endPoints2[3], 0, 10),
                                new Segment(sections2[5], endPoints2[11], 0, 11) },
                        { new Segment(sections2[2], endPoints2[4], 6, 15) } };

        Segment[][] requestedArray =
                {
                        { new Segment(sections2[2], endPoints2[4], 2, 20),
                                new Segment(sections2[3], endPoints2[6], 0, 3) },
                        { new Segment(sections2[5], endPoints2[11], 10, 12),
                                new Segment(sections2[2], endPoints2[5], 0, 6) },
                        { new Segment(sections2[2], endPoints2[4], 10, 18) } };

        Segment[][] expectedAllocationArray =
                {
                        { new Segment(sections2[2], endPoints2[4], 2, 5) },
                        { new Segment(sections2[5], endPoints2[11], 10, 12),
                                new Segment(sections2[2], endPoints2[5], 0, 4) },
                        { new Segment(sections2[2], endPoints2[4], 10, 15) } };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    /**
     * test with alternative track: two trains: routes might contain more than
     * one segment each: one requested allocation possible, the other empty.
     */
    @Test(timeout = 5000)
    public void test11() throws Exception {
        // arrays describing the occupied, requested and expected routes
        Segment[][] occupiedArray =
                {
                        { new Segment(sections2[4], endPoints2[9], 8, 11),
                                new Segment(sections2[2], endPoints2[4], 0, 4) },
                        {
                                new Segment(sections2[6], endPoints2[12], 10,
                                        13),
                                new Segment(sections2[1], endPoints2[3], 0, 10),
                                new Segment(sections2[5], endPoints2[11], 0, 11) } };

        Segment[][] requestedArray =
                {
                        { new Segment(sections2[2], endPoints2[4], 2, 20),
                                new Segment(sections2[3], endPoints2[6], 0, 3) },
                        { new Segment(sections2[5], endPoints2[11], 11, 12),
                                new Segment(sections2[2], endPoints2[5], 0, 5) } };

        Segment[][] expectedAllocationArray =
                {
                        { new Segment(sections2[2], endPoints2[4], 2, 20),
                                new Segment(sections2[3], endPoints2[6], 0, 3) },
                        {} };

        // input parameters to method and expected outputs
        List<List<Segment>> occupied = asList(occupiedArray);
        List<List<Segment>> requested = asList(requestedArray);
        List<List<Segment>> expectedAllocation =
                asList(expectedAllocationArray);

        // the actual result of the allocation
        List<List<Segment>> actualAllocation =
                Allocator.allocate(occupied, requested);
        Assert.assertEquals(expectedAllocation, actualAllocation);
    }

    // -----Helper Methods-------------------------------

    /**
     * Returns a list of lists representation of the array of arrays of
     * segments.
     * 
     * @param array
     *            the array to convert
     * @return the array converted to a list of lists
     */
    private List<List<Segment>> asList(Segment[][] array) {
        List<List<Segment>> list = new ArrayList<>();
        for (Segment[] innerArray : array) {
            List<Segment> innerList = new ArrayList<>();
            for (Segment segment : innerArray) {
                innerList.add(segment);
            }
            list.add(innerList);
        }
        return list;
    }

}
