import java.lang.Math;

public class Polynomial {
    public double[] coeffs;

    public Polynomial() {
        this.coeffs = new double[1];
        // Technically, it is 0 already, but I will be explicit
        this.coeffs[0] = 0;
    }

    public Polynomial(double[] coeffs) {
        // checking for len = 0 is probably too pedantic since we have
        // a no-arg constructor anyways
        this.coeffs = new double[coeffs.length];
        for (int i = 0; i < coeffs.length; i++) {
            this.coeffs[i] = coeffs[i];
        }
    }

    public Polynomial add(Polynomial other) {
        // Which one is longest
        int longestLen = Math.max(this.coeffs.length, other.coeffs.length);
        double[] coeffs = new double[longestLen];
        for (int i = 0; i < this.coeffs.length; i++) {
            coeffs[i] += this.coeffs[i];
        }
        for (int i = 0; i < other.coeffs.length; i++) {
            coeffs[i] += other.coeffs[i];
        }
        return new Polynomial(coeffs);
    }

    public double evaluate(double x) {
        double res = 0;
        for (int i = 0; i < this.coeffs.length; i++) {
            res += this.coeffs[i] * Math.pow(x, i);
        }
        return res;
    }

    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }
}