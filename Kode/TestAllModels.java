import java.util.*;
import java.io.*;

public class TestAllModels
{
    public static void main(String[] args) throws Exception
    {
        IndividualGenerator[] chromosomeEncodings = new IndividualGenerator[] {
            new SelectionIndividualGenerator(),
            new CoordinateIndividualGenerator()
        };
        ParentSelection[] parentSelections = new ParentSelection[] {
            new ParentSelectionByFitness(),
            new ParentSelectionByRank()
        };
        Crossover[] crossovers = new Crossover[] {
            new SinglePointCrossover(),
            new ArithmeticAverageCrossover()
        };
        Mutation[] mutations = new Mutation[] {
            new SwapMutation(),
            new IncrementMutation()
        };
        String[] encodingNames = new String[] {"Combination", "BoundedCoordinate"};

        Random rand = new Random(20010911);
        TesterTools.initializeSeeds(rand);
        Random[] rnds = TesterTools.rnds;
        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;
        double[][] res = new double[mapNames.length][rnds.length];

        BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificHyperparameter.csv"));
        StringTokenizer st = new StringTokenizer(br.readLine(), ",");
        FileWriter fw = new FileWriter(Tester.resultsPath + "/generalResults.csv", true);

        FireStationsSingleton fss = FireStationsSingleton.INSTANCE;
        fss.setPopulationSize(Integer.parseInt(st.nextToken()));
        fss.setCrossoverRate(Double.parseDouble(st.nextToken()));
        fss.setMutationRate(Double.parseDouble(st.nextToken()));
        
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Map,Mean,Standard Deviation");
        int i, j, k, l, m, n, evaluationBudget;
        for (i = 0; i < 2; i++)
        {
            for (j = 0; j < 2; j++)
            {
                for (k = 0; k < 2; k++)
                {
                    for (l = 0; l < 2; l++)
                    {
                        fss.setGenerator(chromosomeEncodings[i]);
                        fss.setParentSelection(parentSelections[j]);
                        fss.setCrossover(crossovers[k]);
                        fss.setMutation(mutations[l]);

                        for (m = 0; m < mapNames.length; m++)
                        {
                            fss.setEnvironment(maps[m]);
                            // referensi: Tabel1 pada pdf Tugas buatan Pak Lionov
                            evaluationBudget = 500000; // ukuran mulai dari 80 x 80
                            if (maps[m].unreachablePenalty < 400) evaluationBudget = 10000; // ukuran lebih kecil 20 x 20 (1-399)
                            else if (maps[m].unreachablePenalty < 1600) evaluationBudget = 50000; // ukuran lebih kecil 40 x 40 (400-1599)
                            else if (maps[m].unreachablePenalty < 6400) evaluationBudget = 200000; // ukuran lebih kecil 80 x 80 (1600-6399)
                            fss.setEvaluationBudget(evaluationBudget);

                            for (n = 0; n < rnds.length; n++)
                            {
                                fss.setRandom(rnds[n]);
                                Population subject = new Population(
                                    chromosomeEncodings[i],
                                    fss.getPopulationSize(),
                                    fss.getElitismRate());
                                fss.setPopulation(subject);
                                fss.setTermination(new FullParameterTermination(
                                    subject, 5, 1, evaluationBudget));
                                fss.run();
                            
                                res[m][n] = fss.getPopulation().getIndividual(0).objective;
                            }

                            double[] values = TesterTools.meanAndStdev(res[m]);
                            fw.append("\n" + encodingNames[i] + "," + 
                            parentSelections[j].getClass().getSimpleName() + "," +
                            crossovers[k].getClass().getSimpleName() + "," +
                            mutations[l].getClass().getSimpleName() + "," +
                            values[0] + "," + values[1]);
                        }
                    }
                }
            }
        }

        br.close();
    }
}
