import java.util.*;
import java.io.*;

public class TestAllModels
{
    public static void main(String[] args) throws Exception
    {
        TesterTools.setOtherParameters();
        int[] seeds = TesterTools.seeds;

        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;
        
        BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificHyperparameter.txt"));

        FireStationsSingleton fss = FireStationsSingleton.INSTANCE;
        fss.setPopulationSize(Integer.parseInt(br.readLine().trim()));
        fss.setCrossoverRate(Double.parseDouble(br.readLine().trim()));
        fss.setMutationRate(Double.parseDouble(br.readLine().trim()));
        fss.setElitismRate(Double.parseDouble(br.readLine().trim()));
        br.close();
        
        ModelTestData[] model = new ModelTestData[16];
        int i, j, k, l, m, n;
        for (i = 0; i < 2; i++)
        {
            fss.setGenerator(TesterTools.chromosomeEncodings[i]);
            for (j = 0; j < 2; j++)
            {
                fss.setParentSelection(TesterTools.parentSelections[j]);
                for (k = 0; k < 2; k++)
                {
                    fss.setCrossover(TesterTools.crossovers[k]);
                    for (l = 0; l < 2; l++)
                    {
                        fss.setMutation(TesterTools.mutations[l]);

                        double[][] res = new double[mapNames.length][seeds.length];
                        for (m = 0; m < mapNames.length; m++)
                        {
                            fss.setEnvironment(maps[m]);
                            fss.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[m]));

                            for (n = 0; n < seeds.length; n++)
                            {
                                fss.setRandom(new Random(seeds[n]));
                                Population subject = new Population(
                                    TesterTools.chromosomeEncodings[i],
                                    fss.getPopulationSize(),
                                    fss.getElitismRate());
                                fss.setPopulation(subject);
                                fss.setTermination(new FullParameterTermination(
                                    subject, 5, 1, fss.getEvluationBudget()));
                                fss.run();
                            
                                res[m][n] = fss.getPopulation().getIndividual(0).objectiveValue;
                            }
                        }

                        model[i * 8 + j * 4 + k * 2 + l] = new ModelTestData(i, j, k, l,
                            fss.getPopulationSize(), fss.getCrossoverRate(), fss.getMutationRate(), fss.getElitismRate(),
                            res);
                    }
                }
            }
        }

        FileWriter fw = new FileWriter(TesterTools.resultsPath + "/AllModelsVerboseResults.csv", true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,Seed,Result");
        for (ModelTestData data : model)
            fw.append(data.outputVerbose());
        fw.close();

        fw = new FileWriter(TesterTools.resultsPath + "/Top3Results.csv", true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,Mean Result");
        Arrays.sort(model);
        for (int pos = 0; pos < 3; pos++)
        {
            fw.append(model[pos].outputBestDataOnly());
            fw.append("\nOverall Mean," + model[pos].overallMean);
        }
        fw.close();
    }
}