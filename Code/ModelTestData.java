public class ModelTestData implements Comparable<ModelTestData>
{
    public final String csvTemplate;

    public final int populationSize;
    public final double crossoverRate;
    public final double mutationRate;
    public final double elitismRate;

    private double[][] results;
    private double[] mapMean;

    public final double overallMean;

    public ModelTestData(int chromosomeCode, int parentSelectionCode, int crossoverCode, int mutationCode,
        int populationSize, double crossoverRate, double mutationRate, double elitismRate, double[][] results)
    {
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismRate = elitismRate;
        
        this.results = results;
        csvTemplate = TesterTools.encodingNames[chromosomeCode] + "," +
        TesterTools.parentSelections[parentSelectionCode].getClass().getSimpleName() + "," +
        TesterTools.crossovers[crossoverCode].getClass().getSimpleName() + "," +
        TesterTools.mutations[mutationCode].getClass().getSimpleName() + "," +
        populationSize + "," + crossoverRate + "," + mutationRate + "," + elitismRate;

        mapMean = new double[results.length];

        int i;
        for (i = 0; i < results.length; i++)
        {
            mapMean[i] = TesterTools.meanAndStdev(results[i])[0];
        }
        
        overallMean = TesterTools.meanAndStdev(mapMean)[0];
    }

    @Override 
    public int compareTo(ModelTestData other)
    {
        return Double.compare(overallMean, other.overallMean);
    }

    public String outputVerbose()
    {
        StringBuilder res = new StringBuilder();
        int i, j;
        for (i = 0; i < results.length; i++)
        {
            for (j = 0; j < results[i].length; j++)
            {
                res.append("\n" + csvTemplate + "," +
                TesterTools.mapNames[i] + "," + TesterTools.seeds[j] + "," + results[i][j]);
            }
        }

        return res.toString();
    }

    public String outputVerboseDataOnly()
    {
        StringBuilder res = new StringBuilder();
        int i, j;
        for (i = 0; i < results.length; i++)
        {
            for (j = 0; j < results[i].length; j++)
            {
                res.append("\n" + TesterTools.mapNames[i] + "," + TesterTools.seeds[j] + results[i][j]);
            }
        }

        return res.toString();
    }

    public String outputBest()
    {
        StringBuilder res = new StringBuilder();
        int i;
        for (i = 0; i < results.length; i++)
            res.append("\n" + csvTemplate + "," + TesterTools.mapNames[i] + "," + mapMean[i]);

        return res.toString();
    }

    public String outputBestDataOnly()
    {
        StringBuilder res = new StringBuilder();
        int i;
        for (i = 0; i < results.length; i++)
            res.append("\n" + TesterTools.mapNames[i] + "," + mapMean[i]);

        return res.toString();
    }
}
