import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Exception;
import java.util.Random;
import java.util.StringTokenizer;

public class TesterTools
{
    public static final String inputPath = System.getProperty("user.dir");
    public static final String projectRoot = inputFolder + "/..";
    public static final String resultsPath = projectRoot + "/Results";

    public static String[] mapNames;
    public static Environment[] maps;

    public static int[] seeds;
    public static Random[] rnds;

    public static void initializeSeeds(Random rand, int n)
    {
        seeds = new int[n];
        rnds = new Random[n];

        for (int i = 0; i < n; i++)
        {
            seeds[i] = rand.nextInt();
            rnds[i] = new Random(seeds[i]);
        }
    }

    public static void readMaps() throws Exception
    {
        File folder = new File(inputFolder + "/maps");
        File[] list = folder.listFiles();
        mapNames = new String[list.length];
        maps = new Environment[list.length];

        int n, m, p, i, f;
        for (f = 0; f < list.length; f++)
        {
            mapNames[f] = list[f].getName();
            BufferedReader br = new BufferedReader(new FileReader(list[f]));
            StringTokenizer st = new StringTokenizer(br.readLine());

            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            p = Integer.parseInt(st.nextToken());

            char[][] map = new char[n][];
            for (i = 0; i < n; i++) map[i] = br.readLine().toCharArray();
            br.close();

            maps[f] = new Environment(map, p);
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