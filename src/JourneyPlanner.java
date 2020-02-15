/**
 * user: jl653
 * name: Joseph Ling
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JourneyPlanner {
    private static ArrayList<Vertex[]> puzzles;

    /**
     * Reads in all the rhombuses until it reaches an empty line then reads in puzzles
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


    public static void main(String[] args) throws IOException {
        // if it can't find the file use me:
        // System.out.println(System.getProperty("user.dir"));;

        String file_name = "jl653.csv";
        if (args.length == 1) {
            file_name = args[0];
        }

        System.out.println("loading in rhombuses and problems from " + file_name);

        ArrayList<Vertex[]> rhombus = loadConfig(file_name);
        String output_path;
        for (int i = 0; i < puzzles.size(); i++) {
            output_path = String.format("./%s.txt", i+1);
            Files.write(
                    Paths.get(output_path),
                    new JourneyPlanner(rhombus).depthFirst(puzzles.get(i)[0], puzzles.get(i)[1], 4).stream().map(Object::toString)
                            .collect(Collectors.joining(" ")).getBytes()
            );
            FormatChecker.check(output_path);
        }
    }


    private ArrayList<Vertex[]> rhombus;

    public JourneyPlanner(ArrayList<Vertex[]> rhombus) {
        this.rhombus = rhombus;
    }

    /**
     * This should be side effect free
     * <p>
     * no more than enurmate all the vertices which are connected with a single
     * straight line segment to the given state `state`.
     * <p>
     * These adjacent tates are those vertices which can be reached from state
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

        return null;
    }

    public LinkedList<Vertex> iterativeDeepening(Vertex first, Vertex last) {
        for (int depth = 1; true; depth++) {
            LinkedList<Vertex> route = depthFirst(first, last, depth);
            if (route != null) return route;
        }
    }

    private LinkedList<Vertex> depthFirst(Vertex first, Vertex last, int depth) {
//        if (depth == 0) {
//            return null;
//        } else if (first.equals(last)) {
//            LinkedList<Vertex> route = new LinkedList<Vertex>();
//            route.add(first);
//            return route;
//        } else {
//            List<Vertex> nexts = nextConfigs(first);
//            for (Vertex next : nexts) {
//                LinkedList<Vertex> route = depthFirst(next, last, depth - 1);
//                if (route != null) {
//                    route.addFirst(first);
//                    return route;
//                }
//            }
//            return null;
//        }
        LinkedList<Vertex> route = new LinkedList<Vertex>();
        route.add(first);
        route.add(last);

        return route;
    }
}
