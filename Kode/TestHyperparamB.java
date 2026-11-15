import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class TestHyperparamB {

    public static void main(String[] args) throws Exception {
        Random rand = new Random(20010911);
        TesterTools.initializeSeeds(rand, 10);
        Random[] rnds = TesterTools.rnds;
        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;
        double[][] res = new double[mapNames.length][rnds.length];

        // semua method yang mau di test
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

        FireStationsSingleton fireStation = FireStationsSingleton.INSTANCE;

        // Ambil hyperparameter terbaik dari CSV
        BufferedReader brBest = new BufferedReader(new FileReader(TesterTools.inputPath + ".csv"));
        StringTokenizer stBest = new StringTokenizer(brBest.readLine(), ",");

        fireStation.setPopulationSize(Integer.parseInt(stBest.nextToken().trim()));
        fireStation.setCrossoverRate(Double.parseDouble(stBest.nextToken().trim()));
        fireStation.setMutationRate(Double.parseDouble(stBest.nextToken().trim()));
        fireStation.setElitismRate(Double.parseDouble(stBest.nextToken().trim()));
        brBest.close();

        FileWriter fw = new FileWriter(TesterTools.resultsPath + "/generalResults.csv");

        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Map,Mean,Stdev\n");

        int evaluationBudget = 0;

        // 
        for (int i = 0; i < chromosomeEncodings.length; i++) {
            for (int ii = 0; ii < parentSelections.length; ii++) {
                for (int iii = 0; iii < crossovers.length; iii++) {
                    for (int iv = 0; iv < mutations.length; iv++) {
                        fireStation.setGenerator(chromosomeEncodings[i]);
                        fireStation.setParentSelection(parentSelections[ii]);
                        fireStation.setCrossover(crossovers[iii]);
                        fireStation.setMutation(mutations[iv]);

                        for (int v = 0; v < mapNames.length; v++) {
                            fireStation.setEnvironment(maps[v]);

                            evaluationBudget = 500000;
                            if (maps[v].unreachablePenalty < 400) {
                                evaluationBudget = 10000;
                            } 
                            else if (maps[v].unreachablePenalty < 1600) {
                                evaluationBudget = 50000;
                            } 
                            else if (maps[v].unreachablePenalty < 6400) {
                                evaluationBudget = 200000;
                            }

                            fireStation.setEvaluationBudget(evaluationBudget);

                            for (int vi = 0; vi < rnds.length; vi++) {
                                fireStation.setRandom(rnds[vi]);
                                chromosomeEncodings[i].updateContext();

                                Population candidate = new Population(chromosomeEncodings[i], fireStation.getPopulationSize(),fireStation.getElitismRate());
                                fireStation.setPopulation(candidate);
                                fireStation.setTermination(new FullParameterTermination(candidate, 5, 1, evaluationBudget));
                                fireStation.run();

                                res[v][vi] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                            }

                            double[] values = TesterTools.meanAndStdev(res[v]);
                            fw.append(
                                encodingNames[i] + "," +
                                parentSelections[ii].getClass().getSimpleName() + "," +
                                crossovers[iii].getClass().getSimpleName() + "," +
                                mutations[iv].getClass().getSimpleName() + "," +
                                mapNames[v] + "," +
                                values[0] + "," + values[1] + "\n"
                            );
                            fw.flush();
                        }
                    }
                }
            }
        }

        fw.close();
    }
}