import java.util.*;
import java.io.*;

public class TestAllOfIt
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
        String[] encodingNames = new String[] {"SelectedPositionCombination", "FireStationPositionArray"};

        Random rand = new Random(20010911);
        int[] seeds = new int[30];
        double[] res = new double[30];

        String cmdPath = System.getProperty("user.dir");
        BufferedReader br = new BufferedReader(new FileReader(cmdPath + "/Hyperparameter.csv"));
        StringTokenizer st = new StringTokenizer(br.readLine(), ",");

        String resPath = cmdPath + "/../Results";
        FileWriter fw = new FileWriter(resPath + "/generalResults.csv", true);

        FireStationsSingleton fss = FireStationsSingleton.INSTANCE;
        fss.setPopulationSize(Integer.parseInt(st.nextToken()));
        fss.setEvaluationBudget(Integer.parseInt(st.nextToken()));
        fss.setCrossoverRate(Double.parseDouble(st.nextToken()));
        fss.setMutationRate(Double.parseDouble(st.nextToken()));
        
        int i, j, k, l, m;
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

                        // belum set Environment awokawok

                        for (m = 0; m < 30; m++)
                        {
                            seeds[m] = rand.nextInt();
                            
                            fss.setRandom(new Random(seeds[m]));
                            Population subject = new Population(chromosomeEncodings[i],
                                fss.getPopulationSize(),
                                fss.getElitismRate());
                            fss.setPopulation(subject);
                            fss.run();

                            res[m] = fss.getPopulation().getIndividual(0).objective;
                        }

                        double[] values = CustomStatistics.meanAndStdev(res);
                        fw.append("\n" + values[0] + "," + values[1]);
                    }
                }
            }
        }
    }
}