import java.util.*;
import java.io.*;
import java.lang.*;

public class TesterTools
{
    public static Random globalRand;

    public static final String inputPath = System.getProperty("user.dir");
    public static final String projectRoot = inputPath + "/..";
    public static final String resultsPath = projectRoot + "/Results";

    public static final IndividualGenerator[] chromosomeEncodings = new IndividualGenerator[] {
        new SelectionIndividualGenerator(),
        new CoordinateIndividualGenerator()
    };
    public static final ParentSelection[] parentSelections = new ParentSelection[] {
        new ParentSelectionByFitness(),
        new ParentSelectionByRank()
    };
    public static final Crossover[] crossovers = new Crossover[] {
        new SinglePointCrossover(),
        new ArithmeticAverageCrossover()
    };
    public static final Mutation[] mutations = new Mutation[] {
        new SwapMutation(),
        new IncrementMutation()
    };
    public static final String[] encodingNames = new String[] {"Combination", "BoundedCoordinate"};

    public static String[] mapNames;
    public static Environment[] maps;

    public static int[] seeds;

    public static void setOtherParameters() throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(inputPath + "/OtherParameters.txt"));
        globalRand = new Random(Integer.parseInt(br.readLine().trim()));
        br.close();
    }

    public static void initializeSeeds(int randSeed, int n)
    {
        Random rand = new Random(randSeed);
        seeds = new int[n];

        for (int i = 0; i < n; i++) seeds[i] = rand.nextInt();
    }

    public static void readMaps() throws Exception
    {
        File folder = new File(inputPath + "/Maps");
        File[] list = folder.listFiles();
        mapNames = new String[list.length];
        maps = new Environment[list.length];

        int n, m, p, i, f;
        for (f = 0; f < list.length; f++)
        {
            BufferedReader br = new BufferedReader(new FileReader(list[f]));
            StringTokenizer st = new StringTokenizer(br.readLine());

            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            p = Integer.parseInt(st.nextToken());

            char[][] map = new char[n][];
            for (i = 0; i < n; i++) map[i] = br.readLine().toCharArray();
            br.close();

            maps[f] = new Environment(map, p);
            mapNames[f] = list[f].getName();
        }
    }

    public static double[] meanAndStdev(double[] arr)
    {
        int i;
        double mean = 0.0;
        double stdev = 0.0;

        // variabel mean menyimpan sum sementara
        for (i = 0; i < arr.length; i++) mean += arr[i];
        // mean = sum / n
        mean /= arr.length;

        // stdev = sqrt(sigma{(xi - mean)^2} / (n - 1))
        for (i = 0; i < arr.length; i++) stdev += Math.pow((arr[i] - mean), 2);

        return new double[] {mean, stdev};
    }
}
