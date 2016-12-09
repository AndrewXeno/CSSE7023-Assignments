package railway;

import java.util.*;

public class Allocator {

    /**
     * This method takes as input a list of the routes that are currently
     * occupied by trains on the track, and a list of the routes requested by
     * each of those trains and returns an allocation of routes to trains based
     * on those inputs.
     * 
     * Such a method may be used by a train controller to manage the movement of
     * trains on the track so that they do not collide. (I.e. if a train has
     * been allocated to a route, it has permission to travel on that route.)
     * 
     * @require occupied != null && requested != null
     * 
     *          && !occupied.contains(null)
     * 
     *          && !requested.contains(null)
     * 
     *          && occupied.size() == requested.size()
     * 
     *          && none of the occupied routes intersect
     * 
     *          && the routes in the occupied list are non-empty, valid routes
     * 
     *          && the routes in the requested list are non-empty, valid routes
     * 
     *          && all the routes in occupied and requested are part of the same
     *          track.
     * 
     * @ensure Let N be the number of elements in the occupied list. This method
     *         returns a list of N routes, where, for each index i satisfying 0
     *         <= i < N, \result.get(i) is the route allocated to the ith train:
     *         the train currently occupying route occupied.get(i).
     * 
     *         The route allocated to the ith train is the longest prefix of
     *         requested.get(i) that does not intersect with any of the routes
     *         currently occupied by any other train, or any of the routes
     *         \result.get(j) for indices j satisfying 0 <= j < i. (I.e. trains
     *         with lower indices have higher priority.)
     * 
     *         Neither of the two input parameters, the occupied list and the
     *         requested list, are modified in any way by this method.
     *
     * @param occupied
     *            there are occupied.size() trains on the track, and parameter
     *            occupied is a list of the routes currently occupied by each of
     *            those trains. A precondition of this method is that none of
     *            the occupied routes are null or empty, they are valid routes,
     *            and that they do not intersect (i.e. no two trains can occupy
     *            the same location on the track at the same time).
     * @param requested
     *            a list of the routes requested by each of the occupied.size()
     *            trains. A precondition of the method is that occupied.size()
     *            == requested.size(), and that none of the requested routes are
     *            null or empty, and that they are valid routes. For index i
     *            satisfying 0 <= i < requested.size(), requested.get(i) is the
     *            route requested by the train currently occupying the route
     *            occupied.get(i).
     * @return the list of allocated routes.
     */
    public static List<List<Segment>> allocate(List<List<Segment>> occupied,
            List<List<Segment>> requested) {
        // the list of allocated routes for the trains to be returned
        List<List<Segment>> allocated = new ArrayList<List<Segment>>();
        // the total number of trains
        int trainCount = occupied.size();
        for (int i = 0; i < trainCount; i++) {
            // the allocated route for the current train
            List<Segment> allocatedRoute = allocateTrain(i, occupied,
                    requested, allocated);
            allocated.add(allocatedRoute);
        }
        return allocated;
    }

    /**
     * allocate the route for a train with the given index.
     * 
     * @require occupied != null && requested != null && allocated != null
     * 
     *          && !occupied.contains(null)
     * 
     *          && !requested.contains(null)
     * 
     *          && !allocated.contains(null)
     * 
     *          && occupied.size() == requested.size()
     * 
     *          && index is an integer
     * 
     *          && 0 <= index && index < occupied.size()
     * 
     *          && none of the occupied routes intersect
     * 
     *          && the routes in the occupied list are non-empty, valid routes
     * 
     *          && the routes in the requested list are non-empty, valid routes
     * 
     *          && the routes in the allocated list are non-empty, valid routes
     * 
     *          && all the routes in occupied, requested and allocated are
     *          part of the same track.
     * 
     * 
     * @ensure The route allocated to the train with the given index is the
     *         longest prefix of requested.get(index) that does not intersect
     *         with any of the routes currently occupied by any other train,
     *         or any of the routes that has previously been allocated to
     *         other trains.
     * 
     *         The occupied, requested and allocated lists are modified
     *         in any way by this method.
     * 
     * @param index
     *            the index of the train to be allocated.
     * @param occupied
     *            a list of the routes currently occupied by each of the trains.
     * @param requested
     *            a list of the routes requested by each of the trains.
     * @param allocated
     *            a list of the routes that has been allocated to other trains.
     * @return
     *         the allocated route for the train with the given index.
     */
    private static List<Segment> allocateTrain(int index,
            List<List<Segment>> occupied, List<List<Segment>> requested,
            List<List<Segment>> allocated) {
        // the requested route for the current train
        // (current train means train with the given index)
        List<Segment> requestedRoute = requested.get(index);
        // the all invalid locations for the current train
        ArrayList<Location> invalidLocations = getInvalidLocations(index,
                occupied, allocated);
        // the allocated route for the current train to be returned
        List<Segment> result = new ArrayList<Segment>();
        for (int i = 0; i < requestedRoute.size(); i++) {
            // the current segment that is currently being processed
            Segment segment = requestedRoute.get(i);
            if (!isIntersected(segment, invalidLocations)) {
                result.add(segment);
            } else {
                // the longest valid prefix of the current segment
                Segment longestValidPrefix = getLongestValidPrefix(
                        segment, invalidLocations);
                // if there is no valid prefix (i.e. null is returned),
                // add nothing
                if (longestValidPrefix != null) {
                    result.add(longestValidPrefix);
                }
                break;
            }
        }
        return result;
    }

    /**
     * generate the longest valid prefix of the given segment.
     * 
     * @require segment != null && invalidLocations != null
     * 
     *          && !invalidLocations.contains(null)
     * 
     * @ensure the longest valid prefix of the original segment S, is a
     *         sub-segment of S which starts from the start location of S and
     *         end with the location prior to the first location that is
     *         contained in the invalid location list.
     * 
     *         This method is intended to be used when the segment intersects
     *         with the invalid location list. However, for robustness reason,
     *         this method can handle the situation that the segment does not
     *         intersects with the invalid location list. It will return an
     *         equivalent segment of the original one.
     * 
     *         If the segment does not have a valid prefix,
     *         it will return null.
     * 
     * @param segment
     *            the original segment.
     * @param invalidLocations
     *            a list of locations that are invalid for the given segment.
     * @return
     *         a segment which is the longest valid prefix of the given segment.
     *         Return null if there is no valid prefix for the segment.
     */
    private static Segment getLongestValidPrefix(Segment segment,
            ArrayList<Location> invalidLocations) {
        // the section of the given segment
        Section section = segment.getSection();
        // the departing end-point of the given segment
        JunctionBranch departingEndPoint = segment.getDepartingEndPoint();
        int startOffset = segment.getStartOffset();
        // loop variable, need to be accessed after the loop
        int i;
        // the list of all locations within the given segment
        ArrayList<Location> locations = getLocations(segment);
        for (i = 0; i < locations.size(); i++) {
            if (invalidLocations.contains(locations.get(i))) {
                break;
            }
        }
        // the end offset of the new prefix segment. Should be the offset of
        // the prior location of the location that the loop ends with. Thus -1
        int newEndOffset = startOffset + i - 1;
        if (startOffset < newEndOffset) {
            // the returned segment prefix
            Segment result = new Segment(section, departingEndPoint,
                    startOffset, newEndOffset);
            return result;
        } else {
            return null;
        }
    }

    /**
     * check if the given segment is intersect with any location in the given
     * location list.
     * 
     * @require segment != null && locations != null
     * 
     *          && !locations.contains(null)
     * 
     * @ensure Only return true if the segment intersects with the location
     *         list. i.e. return true if and only if there is at least one
     *         location within the segment that is contained by the location
     *         list.
     * 
     * @param segment
     *            the segment to be checked with.
     * @param invalidLocations
     *            a list of locations to be checked with.
     * @return return true if the segment is intersects with any location in
     *         the list. Otherwise return false.
     */
    private static boolean isIntersected(Segment segment,
            ArrayList<Location> locations) {
        // the list of all locations within the given segment
        ArrayList<Location> segmentLocations = getLocations(segment);
        for (Location l : segmentLocations) {
            if (locations.contains(l)) {
                return true;
            }
        }
        return false;
    }

    /**
     * for a train with the given index, generate a list of locations that
     * the train cannot be allocated in.
     * 
     * @require occupied != null && allocated != null
     * 
     *          && !occupied.contains(null)
     * 
     *          && !allocated.contains(null)
     * 
     *          && index is an integer
     * 
     *          && 0 <= index && index < occupied.size()
     * 
     *          && none of the occupied routes intersect
     * 
     *          && the routes in the occupied list are non-empty, valid routes
     * 
     *          && the routes in the allocated list are non-empty, valid routes
     * 
     *          && all the routes in occupied and allocated are part of the
     *          same track.
     * 
     * @ensure the returned list contains locations that are occupied by other
     *         trains and the locations that has been allocated to previous
     *         trains. No other locations are in the returned list. The
     *         locations that is only occupied by the given train will not be
     *         in the returned list. There is no duplicates in the result and
     *         there is no null value in the result.
     * 
     *         The occupied and allocated lists are modified in any way by
     *         this method.
     * 
     * @param index
     *            the index of the train(as defined in occupied list).
     * @param occupied
     *            a list of the routes currently occupied by each of the trains.
     * @param allocated
     *            a list of the routes that has been allocated to other trains.
     * @return a list of locations that the current train cannot
     *         be allocated in.
     */
    private static ArrayList<Location> getInvalidLocations(int index,
            List<List<Segment>> occupied, List<List<Segment>> allocated) {
        // the list of invalid locations to be returned
        ArrayList<Location> result = new ArrayList<Location>();
        for (int i = 0; i < occupied.size(); i++) {
            if (i != index) {
                result.addAll(getLocations(occupied.get(i)));
            }
        }
        for (int i = 0; i < allocated.size(); i++) {
            result.addAll(getLocations(allocated.get(i)));
        }
        return result;

    }

    /**
     * takes in a segment and return a list of locations that contains
     * all locations within the segment.
     * 
     * @require segment != null
     * 
     * @ensure Each location in the given segment is in the result
     *         list, and there is no location that is not in the
     *         segment. There is no duplicates in the result and there is no
     *         null value in the result.
     * 
     * @param segment
     *            the segment that to be converted into location list.
     * @return the list of locations that contains all locations within
     *         the segment.
     */
    private static ArrayList<Location> getLocations(Segment segment) {
        // the returned list of all locations within the given segment
        ArrayList<Location> result = new ArrayList<Location>();
        // the section of the given segment
        Section section = segment.getSection();
        // the departing end-point of the given segment
        JunctionBranch departingEndPoint = segment.getDepartingEndPoint();
        // the start offset of the given segment
        int startOffset = segment.getStartOffset();
        // the end offset of the given segment
        int endOffset = segment.getEndOffset();
        for (int i = startOffset; i < endOffset; i++) {
            // the location constructed with the current offset i
            Location location = new Location(section, departingEndPoint, i);
            result.add(location);
        }
        // note: when i == endOffset && endOffset == segment.length
        // the location constructor will throw an exception
        // so directly use the segment's last location
        result.add(segment.getLastLocation());
        return result;
    }

    /**
     * takes in a list of segments and return a list of locations that contains
     * all locations within the segment list.
     * 
     * @require route != null
     * 
     *          && !route.contains(null)
     * 
     *          && the routes is non-empty, valid route
     * 
     * @ensure Each location in each segment in given route is in the result
     *         list, and there is no location that is not in the route
     *         segments. There is no duplicates in the result and there is no
     *         null value in the result. If the route contains no segment, it
     *         will return an empty list.
     * 
     * @param route
     *            the list of segments that to be converted into location list.
     * @return the list of locations that contains all locations within
     *         the segment list.
     */
    private static ArrayList<Location> getLocations(List<Segment> route) {
        // the returned list of locations of all locations within
        // the given route
        ArrayList<Location> result = new ArrayList<Location>();
        for (Segment s : route) {
            result.addAll(getLocations(s));
        }
        return result;
    }
}
