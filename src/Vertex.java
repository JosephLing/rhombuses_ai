import java.math.*;
import java.util.Arrays;

/**
 * user: jl653
 * name: Joseph Ling
 */

public class Vertex implements Comparable<Vertex> {
    private int x;
    private int y;

    Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public int compareTo(Vertex v) {
        if (v.get_x() == get_x()) {
            return v.get_y() - get_y();
        } else {
            return v.get_x() - get_x();
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            Vertex v = (Vertex) o;
            return compareTo(v) == 0;
        } else return false;
    }

    public static double distance(Vertex a, Vertex b){
        return Math.sqrt(Math.pow((a.x - b.x),2) + Math.pow((a.y - b.y),2));
    }

    public static double area(Vertex a, Vertex b,Vertex c,Vertex d){
        return (distance(a, c) * distance(b, d));
    }

    public static int area(Vertex a, Vertex b, Vertex c){
        // uses the shoe lace formula
        return  (((a.get_x() - c.get_x()) * (b.get_y() - a.get_y())) - ((c.get_y() - a.get_y()) * (a.get_x() - b.get_x())));
    }

    public static void main(String[] args){
        System.out.println(area(new Vertex(-3,4), new Vertex(1,-2), new Vertex(5, 5)));;
        System.out.println(area(new Vertex(30,4), new Vertex(1,-2), new Vertex(5, 5)));;

        System.out.println(area(new Vertex(-3,1), new Vertex(1,5), new Vertex(-2, 8)));;
        System.out.println(area(new Vertex(-5,7), new Vertex(-4,-5), new Vertex(4, 5)));;
    }


    /**
     * The key aspect is that a,b,c,d
     *    b ---------- c
     *   /            /
     *  /           /
     * a --------- d
     *
     * format
     * with p being the point that you are checking for
     */
    public static boolean vertexInsideRhombus(Vertex p, Vertex a, Vertex b, Vertex c, Vertex d){
        // this shouldn't work but it does.... if shoelace formula is less than 0 then false
        int[] areas = new int[] {area(p,a,b),area(p,b,c),area(p,c,d),area(p,d,a)};
        for (int area : areas) {
            if (area < 0) {
                return false;
            }
        }
        return  true;
    }

    public static boolean vertexIntersect(Vertex u, Vertex v1, Vertex v2) {
	/* check whether the vertex u falls within a given line segment where
           the line segment connects vertices v1 and v2 (but does not include them) */

        int a = v1.get_x() - v2.get_x();
        int b = u.get_x() - v2.get_x();

        int c = v1.get_y() - v2.get_y();
        int d = u.get_y() - v2.get_y();

	/* check whether the simultaneous equations a mu = b and c mu = d
           has a solution 0 < mu < 1
           where a and c are coefficients of the variable mu
           where b and d are constants */

        if (a < 0) {
            a = -a;
            b = -b;
        }

        if (c < 0) {
            c = -c;
            d = -d;
        }

        if (a == 0 && b == 0) {
            return 0 < d && d < c;
        } else if (c == 0 && d == 0) {
            return 0 < b && b < a;
        } else {
            return 0 < b && b < a && 0 < d && d < c && a * d == b * c;
        }
    }

    public static boolean linesIntersect(Vertex u1, Vertex u2, Vertex v1, Vertex v2) {
	/* check whether line segment 1 intersects line segment 2 where
           line segment 1 connects vertices u1 and u2 (but does not include them) and
           line segment 2 connects vertices v1 and v2 (but does not include them) */

        int a = u1.get_x() - u2.get_x();
        int b = v2.get_x() - v1.get_x();
        int c = v2.get_x() - u2.get_x();

        int d = u1.get_y() - u2.get_y();
        int e = v2.get_y() - v1.get_y();
        int f = v2.get_y() - u2.get_y();

	/* check whether the simultaneous equations a mu + b lambda = c and d mu + e lambda = f
           has a solution 0 < mu < 1 and 0 < lambda < 1
           where a and d are coefficients of the variable mu
           where b and e are coefficients of the variable lambda
           where c and f are constants */

        Predicate intersectPredicate = (int denominator, int lambda_numerator, int mu_numerator) ->
                0 < lambda_numerator && lambda_numerator < denominator &&
                        0 < mu_numerator && mu_numerator < denominator;

        return solve(a, b, c, d, e, f, intersectPredicate);
    }

    private static boolean solve(int a, int b, int c, int d, int e, int f, Predicate p) {
        int denominator = d * b - a * e;

        if (denominator == 0) {
            return false;
        } else {
            int lambda_numerator = d * c - a * f;
            int mu_numerator = b * f - e * c;

            if (denominator < 0) {
                lambda_numerator = -lambda_numerator;
                mu_numerator = -mu_numerator;
                denominator = -denominator;
            }

            return p.predicate(denominator, lambda_numerator, mu_numerator);
        }
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    interface Predicate {
        boolean predicate(int denominator, int lambda_numerator, int mu_numerator);
    }

}
