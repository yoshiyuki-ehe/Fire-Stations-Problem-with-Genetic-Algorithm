import java.util.*;
import java.io.*;

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

    public static double minRelativeChange = 0;

    public static void setOtherParameters() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(inputPath + "/OtherParameters.txt"));
        globalRand = new Random(Integer.parseInt(br.readLine().trim()));
        
        initializeSeeds(globalRand, Integer.parseInt(br.readLine().trim()));

        minRelativeChange = Double.parseDouble(br.readLine().trim());

        br.close();
    }

    public static void initializeSeeds(Random rand, int n)
    {
        seeds = new int[n];
        for (int i = 0; i < n; i++) seeds[i] = rand.nextInt();
    }

    public static void readMaps() throws Exception
    {
        File folder = new File(inputPath + "/Maps");
        File[] list = folder.listFiles();
        mapNames = new String[list.length];
        maps = new Environment[list.length];

        int n, p, i, f;
        for (f = 0; f < list.length; f++)
        {
            BufferedReader br = new BufferedReader(new FileReader(list[f]));
            StringTokenizer st = new StringTokenizer(br.readLine());

            n = Integer.parseInt(st.nextToken().trim());
            // m tidak benar benar dibutuhkan karena banyak kolom langsung dibaca dari jumlah karakter di baris
            st.nextToken();
            p = Integer.parseInt(st.nextToken().trim());

            char[][] map = new char[n][];
            for (i = 0; i < n; i++) map[i] = br.readLine().trim().toCharArray();
            br.close();

            maps[f] = new Environment(map, p);
            // membaca nama file
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

    public static int determineEvaluationBudget(Environment map)
    {
        // referensi: Tabel1 pada pdf Tugas buatan Pak Lionov
        // ralat: gw chat Pak Lionov katanya di pdf harusnya tulisnya small, normal, large 

        if (map.unreachablePenalty < 1600) return 10000; // tiny = ukuran lebih kecil 40 x 40 (1-1599)
        if (map.unreachablePenalty < 3600) return 50000; // small = ukuran lebih kecil 60 x 60 (1600-3599)
        if (map.unreachablePenalty < 6400) return 200000; // normal = ukuran lebih kecil 80 x 80 (3600-6399)
        return 500000;// large = ukuran mulai dari 80 x 80
    }

    public static int determineMaxStagnantGenerations(int populationSize)
    {
        // referensi: Chat sama Gemini

        if (populationSize <= 50) return 10;
        if (populationSize <= 100) return 25;
        return 50;
    }

    public static void writeVerboseToFile(String fileName, ModelTestData[] data) throws IOException
    {
        FileWriter fw = new FileWriter(resultsPath + "/" + fileName, true);
        // Baris Header file CSV
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,Seed,Result");

        for (ModelTestData model : data) fw.append(model.outputVerbose());

        fw.close();
    }

    public static void writeBestToFile(String fileName, ModelTestData[] data) throws IOException
    {
        FileWriter fw = new FileWriter(resultsPath + "/" + fileName, true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,BestResult");

        for (ModelTestData model : data) fw.append(model.outputBest());

        fw.close();
    }

    public static void writeOverallMeanToFile(String fileName, ModelTestData[] data) throws IOException
    {
        FileWriter fw = new FileWriter(resultsPath + "/" + fileName, true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,OverallMean");

        for (ModelTestData model : data) fw.append(model.csvTemplate + "," + model.overallMean);

        fw.close();
    }
}
