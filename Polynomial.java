import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

public class Polynomial {
    private double[] coeffs;
    private int[] pows;

    public Polynomial() {
        // Since we want to store non-zero's this wud be empty
        this.coeffs = new double[0];
        this.pows = new int[0];
    }

    public Polynomial(double[] coeffs) {
        // checking for len = 0 is probably too pedantic since we have
        // a no-arg constructor anyways
        int actualLen = 0;
        for (int i = 0; i < coeffs.length; i++) {
            if (coeffs[i] == 0) continue;
            actualLen++;
        }
        this.coeffs = new double[actualLen];
        this.pows = new int[actualLen];
        for (int i = 0, j = 0; i < coeffs.length; i++) {
            if (coeffs[i] == 0) continue;
            this.coeffs[j] = coeffs[i];
            this.pows[j] = i;
            j++;
        }
    }

    public Polynomial(File f) throws FileNotFoundException {
        Scanner input = new Scanner(f);
        String line = input.nextLine();
        String[] chunks = line.splitWithDelimiters("\\+|-", 9999);
        double multiplier = 1;
        // Thank You AP Computer Science A+
        ArrayList<Double> ncoeffs = new ArrayList<Double>();
        ArrayList<Integer> npows = new ArrayList<Integer>();
        for (String chunk: chunks) {
            if (chunk.equals("+")) {
                multiplier = 1;
                continue;
            } else if (chunk.equals("-")) {
                multiplier = -1;
                continue;
            } else if (chunk.isBlank())
                continue;
            String[] nums = chunk.splitWithDelimiters("x", 2);
            if (nums[0].isEmpty()) // x2 => ["", "x", "2"]
                ncoeffs.add(multiplier);
            else
                ncoeffs.add(multiplier * Double.parseDouble(nums[0]));
            if (nums.length == 1) // 1
                npows.add(0);
            else if (nums[2].isEmpty()) // 2x (if len != 1, then it's 3)
                npows.add(1);
            else // 3x2
              npows.add(Integer.parseInt(nums[2]));
        }
        this.coeffs = new double[ncoeffs.size()];
        this.pows = new int[npows.size()];
        for (int i = 0; i < ncoeffs.size(); i++)
            this.coeffs[i] = ncoeffs.get(i).doubleValue();
        for (int i = 0; i < npows.size(); i++)
            this.pows[i] = npows.get(i).intValue();
        input.close();
    }

    public Polynomial add(Polynomial other) {
        // Which one is longest
        int maxthis = 0;
        int maxother = 0;
        for (int i = 0; i < this.pows.length; i++)
            if (maxthis < this.pows[i])
                maxthis = this.pows[i];
        for (int i = 0; i < other.pows.length; i++)
            if (maxother < other.pows[i])
                maxother = other.pows[i];
        int maxPoly = Math.max(maxthis, maxother);
        // if max is 9, then we need 0..=9 = length of 10
        double[] ncoeffs = new double[maxPoly + 1];
        for (int i = 0; i < this.pows.length; i++)
            ncoeffs[this.pows[i]] += this.coeffs[i];
        for (int i = 0; i < other.pows.length; i++)
            ncoeffs[other.pows[i]] += other.coeffs[i];
        return new Polynomial(ncoeffs);
    }

    public Polynomial multiply(Polynomial other) {
        int maxthis = 0;
        int maxother = 0;
        for (int i = 0; i < this.pows.length; i++)
            if (maxthis < this.pows[i])
                maxthis = this.pows[i];
        for (int i = 0; i < other.pows.length; i++)
            if (maxother < other.pows[i])
                maxother = other.pows[i];
        // deg(2) * deg(3) = deg(2 * 3) = len(2 * 3 + 1)
        int maxPoly = maxthis + maxother + 1;
        double[] ncoeffs = new double[maxPoly];
        for (int i = 0; i < this.pows.length; i++) {
            for (int j = 0; j < other.pows.length; j++) {
                int idx = this.pows[i] + other.pows[j];
                ncoeffs[idx] += this.coeffs[i] * other.coeffs[j];
            }
        }
        return new Polynomial(ncoeffs);
    }

    public double evaluate(double x) {
        double res = 0;
        for (int i = 0; i < this.coeffs.length; i++) {
            res += this.coeffs[i] * Math.pow(x, this.pows[i]);
        }
        return res;
    }

    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }

    public void saveToFile(String fileName) throws IOException {
        FileWriter writer = new FileWriter(new File(fileName));
        writer.write(this.toString());
        writer.close();
    }

    @Override
    public String toString() {
        String out = new String();
        for (int i = 0; i < this.pows.length; i++) {
            if (!out.isEmpty() && this.coeffs[i] > 0)
                out += "+";
            out += this.coeffs[i];
            if (this.pows[i] == 0) continue;
            out += "x";
            if (this.pows[i] != 1)
                out += this.pows[i];
        }
        return out;
    }
}