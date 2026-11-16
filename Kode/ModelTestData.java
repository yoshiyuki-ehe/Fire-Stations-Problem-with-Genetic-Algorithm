public class ModelTestData implements Comparable<ModelTestData>
{
    public final String csvTemplate;

    private double[][] results;
    private double[] mapBest;
    public final double generalMean;

    public ModelTestData(int chromosomeCode, int parentSelectionCode, int crossoverCode, int mutationCode,
        int populationSize, double crossoverRate, double mutationRate, double elitismRate, double[][] results)
    {
        this.results = results;
        csvTemplate = TesterTools.chromosomeEncodings[chromosomeCode] + "," +
        TesterTools.parentSelections[parentSelectionCode] + "," +
        TesterTools.crossovers[crossoverCode] + "," +
        TesterTools.mutations[mutationCode] + "," +
        populationSize + "," + crossoverRate + "," + mutationRate + "," + elitismRate;

        int i, j;
        for (i = 0; i < results.length; i++)
        {
            mapBest[i] = Double.MAX_VALUE;
            for (j = 0; j < results[i].length; j++) mapBest[i] = Math.min(mapBest[i], results[i][j]);
        }
        
        generalMean = TesterTools.meanAndStdev(mapBest)[0];
    }

    @Override 
    public int compareTo(ModelTestData other)
    {
        return Double.compare(generalMean, other.generalMean);
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
            res.append("\n" + csvTemplate + "," + TesterTools.mapNames[i] + "," + mapBest[i]);

        return res.toString();
    }

    public String outputBestDataOnly()
    {
        StringBuilder res = new StringBuilder();
        int i;
        for (i = 0; i < results.length; i++)
            res.append("\n" + TesterTools.mapNames[i] + "," + mapBest[i]);

        return res.toString();
    }
}
