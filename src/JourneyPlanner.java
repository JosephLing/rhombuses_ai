/**
 * user: jl653
 * name: Joseph Ling
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JourneyPlanner {
    public static void main(String args[]) {
        String start = "rye";        /* default start state */
        String finish = "tent";        /* default finish state */

        if (args.length >=  1) {
            start = args[0];
        }
        if (args.length >= 2) {
            finish = args[1];
        }

        JourneyPlanner planner = new JourneyPlanner();
//        LinkedList<Vertex> route = planner.iterativeDeepening(start, finish);
//        System.out.println("route = " + route);
    }

    /**
     * This should be side effect free
     *
     * no more than enurmate all the vertices which are connected with a single
     * straight line segment to the given state `state`.
     *
     * These adjacent tates are those vertices which can be reached from state
     * in a single straight line without passing through the interior of any rhombus.
     *
     * A vertex of one rhombus can arise on the edge of another rhumbus. So this
     * method will have to perform case analysis (which is typical of this kind of
     * problem).
     *
     * Check the code carefully before plugging it into the algorithm.
     *
     * MAKE SURE:
     * - this method only find's adjacent states
     * - implement a simple one first before making it complicated
     * - spend time considering how a journey can navigate around the objstacles
     * otherwise nextCOnfig will be buggy and incomplete.
     *
     * do not implement:
     * - a search algorithm with poor space behaviour such as Dijkstra's, BFS
     * - or implement A* without carefully choosing the hueristics AND DOCUMENTING THEM!!!
     *
     *
     * @param state
     * @return
     */
    public List<Vertex> nextConfigs(Vertex state) {

        return null;
    }

    public LinkedList<Vertex> iterativeDeepening(Vertex first, Vertex last) {
        for (int depth = 1; true; depth++) {
            LinkedList<Vertex> route = depthFirst(first, last, depth);
            if (route != null) return route;
        }
    }

    private LinkedList<Vertex> depthFirst(Vertex first, Vertex last, int depth) {
        if (depth == 0) {
            return null;
        } else if (first.equals(last)) {
            LinkedList<Vertex> route = new LinkedList<Vertex>();
            route.add(first);
            return route;
        } else {
            List<Vertex> nexts = nextConfigs(first);
            for (Vertex next : nexts) {
                LinkedList<Vertex> route = depthFirst(next, last, depth - 1);
                if (route != null) {
                    route.addFirst(first);
                    return route;
                }
            }
            return null;
        }
    }
}
