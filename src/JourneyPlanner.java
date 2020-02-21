/**
 * user: jl653
 * name: Joseph Ling
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JourneyPlanner {

    /**
     * Reads in all the rhombuses until it reaches an empty line then reads in puzzles
     *
     * @param pathToCsv string
     * @return rhombuses
     * @throws IOException
     */
    private static ArrayList<Vertex[]> loadConfig(String pathToCsv) throws IOException {
        ArrayList<Vertex[]> rhombus = new ArrayList<>();
        puzzles = new ArrayList<>();
        String row;
        boolean problems = false;
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if (problems && data.length == 4) {
                puzzles.add(new Vertex[]{
                        new Vertex(Integer.parseInt(data[0]), Integer.parseInt(data[1])),
                        new Vertex(Integer.parseInt(data[2]), Integer.parseInt(data[3]))
                });

            } else if (data.length == 8) {
                rhombus.add(new Vertex[]{
                        new Vertex(Integer.parseInt(data[0]), Integer.parseInt(data[1])),
                        new Vertex(Integer.parseInt(data[2]), Integer.parseInt(data[3])),
                        new Vertex(Integer.parseInt(data[4]), Integer.parseInt(data[5])),
                        new Vertex(Integer.parseInt(data[6]), Integer.parseInt(data[7])),
                });

            } else {
                problems = true;
            }

        }
        csvReader.close();
        return rhombus;
    }

    private static ArrayList<Vertex[]> puzzles;
    private static HashMap<Vertex, ArrayList<Vertex>> cache;
    public static HashSet<Vertex> invalid;
    private static final boolean testing = true;


    public static void main(String[] args) throws IOException {
        // if it can't find the file use me:
        // System.out.println(System.getProperty("user.dir"));;
        cache = new HashMap<>();
        invalid = new HashSet<>();

        String file_name = "jl653.csv";
        if (args.length == 1) {
            file_name = args[0];
        }

        System.out.println("loading in rhombuses and problems from " + file_name);

        ArrayList<Vertex[]> rhombus = loadConfig(file_name);
        String output_path;
        for (int i = 0; i < puzzles.size(); i++) {
            output_path = String.format("./%s.txt", i + 1);
            Files.write(
                    Paths.get(output_path),
                    new JourneyPlanner(rhombus).iterativeDeepening(puzzles.get(i)[0], puzzles.get(i)[1]).stream().map(Object::toString)
                            .collect(Collectors.joining(" ")).getBytes()
            );
            FormatChecker.check(output_path);
            if (testing) {
                break;
            }
        }
    }


    private ArrayList<Vertex[]> rhombuses;


    public JourneyPlanner(ArrayList<Vertex[]> rhombus) {
        this.rhombuses = rhombus;
    }

    /**
     * This should be side effect free
     * <p>
     * no more than enurmate all the vertices which are connected with a single
     * straight line segment to the given state `state`.
     * <p>
     * These adjacent states are those vertices which can be reached from state
     * in a single straight line without passing through the interior of any rhombus.
     * <p>
     * A vertex of one rhombus can arise on the edge of another rhumbus. So this
     * method will have to perform case analysis (which is typical of this kind of
     * problem).
     * <p>
     * Check the code carefully before plugging it into the algorithm.
     * <p>
     * MAKE SURE:
     * - this method only find's adjacent states
     * - implement a simple one first before making it complicated
     * - spend time considering how a journey can navigate around the objstacles
     * otherwise nextCOnfig will be buggy and incomplete.
     * <p>
     * do not implement:
     * - a search algorithm with poor space behaviour such as Dijkstra's, BFS
     * - or implement A* without carefully choosing the hueristics AND DOCUMENTING THEM!!!
     *
     * @param state
     * @return
     */
    public List<Vertex> nextConfigs(Vertex state) {
        // so what vertex are in a straight line from this current state
        // could go through all the vertexs or a pre computed list that has got rid of the intersections

        if (cache.get(state) == null) {
            HashSet<Vertex> set = new HashSet<>();
            for (Vertex[] rhombus : rhombuses) {
                for (Vertex vertex : rhombus) {
                    if (!invalid.contains(vertex) && !set.contains(vertex) && isReachable(state, vertex)) {
                        set.add(vertex);
                    }
                }
            }
            ArrayList<Vertex> results = new ArrayList<>(set);
            cache.put(state, results);
            return results;

        } else {
            return cache.get(state);
        }

    }

    private boolean isReachable(Vertex start, Vertex end) {
        boolean check = true;
        Vertex[] rhombus;
        for (int i = 0; i < rhombuses.size(); i++) {
            rhombus = rhombuses.get(i);

            // if anything doesn't intersect continue
            check = !(
                    Vertex.linesIntersect(start, end, rhombus[0], rhombus[1])
                            || Vertex.linesIntersect(start, end, rhombus[0], rhombus[2])
                            || Vertex.linesIntersect(start, end, rhombus[1], rhombus[3])
                            || Vertex.linesIntersect(start, end, rhombus[2], rhombus[3])
            );

            if (!check) {
                return false;
            }

            // if it's reachable then is it inside a rhombus
            if (
                    Vertex.vertexInsideRhombus(end, rhombus[0], rhombus[1], rhombus[2], rhombus[3]) &&
                    !(end.equals(rhombus[0]) || end.equals(rhombus[1]) || end.equals(rhombus[2]) || end.equals(rhombus[3]))
            ) {
                invalid.add(end);
                return false;
            }

        }
        return check;
    }


    public LinkedList<Vertex> iterativeDeepening(Vertex first, Vertex last) {
        if (testing) {
            System.out.println("should be true");
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(15, 2)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(15, 5)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(18, 4)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(15, 6)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(14, 1)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(17, 4)));
            System.out.println("--------------------");
            System.out.println("should be false");
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(17, 1)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(17, 9)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(19, 6)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(3, 7)));
            System.out.println(isReachable(new Vertex(16, 4), new Vertex(18, 13)));

            // this one should be picked up
            System.out.println(isReachable(new Vertex(14, 21), new Vertex(15, 9)));

            // going diagoonally thruogh
            System.out.println(isReachable(new Vertex(14, 1), new Vertex(15, 2)));

            System.out.println(isReachable(new Vertex(2, 15), new Vertex(4, 16)));

            System.out.println("----------------------");
            System.out.println(Vertex.vertexInsideRhombus(new Vertex(4, 16), rhombuses.get(13)[0], rhombuses.get(13)[1], rhombuses.get(13)[2], rhombuses.get(13)[3]));
            System.out.println(Vertex.vertexInsideRhombus(new Vertex(4, 19), rhombuses.get(13)[0], rhombuses.get(13)[1], rhombuses.get(13)[2], rhombuses.get(13)[3]));
            System.out.println("SHOULD BE FALSE");
            System.out.println(Vertex.vertexInsideRhombus(new Vertex(2, 18), rhombuses.get(13)[0], rhombuses.get(13)[1], rhombuses.get(13)[2], rhombuses.get(13)[3]));
            System.out.println(Vertex.vertexInsideRhombus(new Vertex(20, 0), rhombuses.get(13)[0], rhombuses.get(13)[1], rhombuses.get(13)[2], rhombuses.get(13)[3]));

            return new LinkedList<>();
        } else {
            for (int depth = 1; true; depth++) {
                LinkedList<Vertex> route = depthFirst(first, last, depth);
                if (route != null) return route;
            }
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
