import java.io.File;

public class Driver {
    
    public static Polynomial createLinear(double c) {
        // x + c
        Polynomial p = new Polynomial(new double[] {c, 1});
        if (p.hasRoot(-c))
            System.out.println("Polynomial \"x - " + c + "\" has a root of \"" + -c + "\".");
        else
            throw new Error("Cooked");
        return p;
    }

    public static void testmul(int from, int to) {
        // search from -from..0 and 1..=to
        Polynomial p = createLinear(from);
        for (int i = from + 1; i <= to; i++) {
            if (i == 0) continue;
            p = p.multiply(createLinear(i));
            for (int j = from + 1; j <= i; j++) {
                if (j == 0) continue;
                // Multiply is correct, but if the numbers get too big
                // and exceed the precision of f64's, it'll err
                if (!p.hasRoot(-j))
                    System.out.println(
                        "Aggregate at " + i + " failed evaluation for" + -j + 
                        ", evaluates to " + p.evaluate(-j)
                    );
            }
        }
        System.out.println("Test Mul Yields: " + p);
    }

    public static void testfile() {
        try {
            Polynomial p1 = new Polynomial(new File("testfile.txt"));
            System.out.println(p1);
            Polynomial p2 = new Polynomial(new File("testfile2.txt"));
            System.out.println(p2);
            Polynomial p3 = createLinear(1)
                .multiply(createLinear(2))
                .multiply(createLinear(6))
                .multiply(createLinear(7))
                .multiply(createLinear(42));
            p3.saveToFile("write.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args) {
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        double [] c1 = {6,0,0,5};
        Polynomial p1 = new Polynomial(c1);
        double [] c2 = {0,-2,0,0,-9};
        Polynomial p2 = new Polynomial(c2);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");
        testmul(-5, 6);
        testfile();
    }
}