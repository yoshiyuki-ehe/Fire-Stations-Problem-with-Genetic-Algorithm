import java.util.*;
import java.io.*;

public class TestAllHyperparameters {
   
    public static void main(String [] args) throws Exception{
        int i, j, k, l, m, n;
        
        TesterTools.setOtherParameters();
        int[] seeds = TesterTools.seeds;

        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;

        BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "CombinationHyperparameters.txt"));
         
        StringTokenizer stPopulation = new StringTokenizer(br.readLine(), ",");
        int nPopulation = stPopulation.countTokens(); //cari tahu n (jumlah data)
        int[] hyperParamPopulasi = new int[nPopulation]; //pake array biasa karena n diketahui
        for (i = 0; i < nPopulation; i++) {
            hyperParamPopulasi[i] = Integer.parseInt(stPopulation.nextToken().trim());
        }
        StringTokenizer stCrossover = new StringTokenizer(br.readLine(), ",");
        int nCrossover = stCrossover.countTokens();
        double[] hyperParamCrossover = new double[nCrossover];
        for (i = 0; i < nCrossover; i++) {
            hyperParamCrossover[i] = Double.parseDouble(stCrossover.nextToken().trim());
        }
        StringTokenizer stMutation = new StringTokenizer(br.readLine(), ",");
        int nMutation = stMutation.countTokens();
        double[] hyperParamMutation = new double[nMutation];
        for (i = 0; i < nMutation; i++) {
            hyperParamMutation[i] = Double.parseDouble(stMutation.nextToken().trim());
        }
        StringTokenizer stElitism = new StringTokenizer(br.readLine(), ",");
        int nElitism = stElitism.countTokens();
        double[] hyperParamElitism = new double[nElitism];
        for (i = 0; i < nElitism; i++) {
            hyperParamElitism[i] = Double.parseDouble(stElitism.nextToken().trim());
        }
        br.close();
   
        FireStationsSingleton fireStation = FireStationsSingleton.INSTANCE;

        // menggunakan 1 jenis Model
        BufferedReader brModel = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificModel.txt"));
        StringTokenizer stModel = new StringTokenizer(brModel.readLine(), ",");
        brModel.close();

        //ambil angkanya
        int encodingCode  = Integer.parseInt(stModel.nextToken().trim());
        int selectionCode = Integer.parseInt(stModel.nextToken().trim());
        int crossoverCode = Integer.parseInt(stModel.nextToken().trim());
        int mutationCode  = Integer.parseInt(stModel.nextToken().trim());

        //set komponen pake index array, karena array mulai dari 0 di -1
        fireStation.setGenerator(TesterTools.chromosomeEncodings[encodingCode]);
        fireStation.setParentSelection(TesterTools.parentSelections[selectionCode]);
        fireStation.setCrossover(TesterTools.crossovers[crossoverCode]);
        fireStation.setMutation(TesterTools.mutations[mutationCode]);

        // model menyimpan semua kombinasi Hyperparameter
        ModelTestData[] model = new ModelTestData[hyperParamPopulasi.length * hyperParamCrossover.length *
            hyperParamMutation.length * hyperParamElitism.length];
        
        for(i = 0; i < hyperParamPopulasi.length; i++)
        {
            fireStation.setPopulationSize(hyperParamPopulasi[i]);
            for (j = 0; j < hyperParamCrossover.length; j++)
            {
                fireStation.setCrossoverRate(hyperParamCrossover[j]);
                for (k = 0; k < hyperParamMutation.length; k++)
                {
                    fireStation.setMutationRate(hyperParamMutation[k]);
                    for (l = 0; l < hyperParamElitism.length; l++)
                    {
                        double[][] res = new double[maps.length][seeds.length];

                        for(m = 0; m < mapNames.length; m++){
                            fireStation.setEnvironment(maps[m]);
                            fireStation.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[m]));

                            for(n = 0; n < seeds.length; n++){
                                fireStation.setRandom(new Random(seeds[n]));
                                Population candidate = new Population(fireStation.getGenerator(), fireStation.getPopulationSize(), fireStation.getElitismRate());
                                fireStation.setPopulation(candidate);

                                fireStation.setTermination(new FullParameterTermination(candidate, 5,1, fireStation.getEvluationBudget()));
                                fireStation.run();

                                res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                            }
                        }

                        model[i * hyperParamCrossover.length * hyperParamMutation.length * hyperParamElitism.length +
                            j * hyperParamMutation.length * hyperParamElitism.length + k * hyperParamElitism.length + l] =
                            new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                        fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
                    }
                }
            }
        }
        
        FileWriter fw = new FileWriter(TesterTools.resultsPath + "/AllHyperparametersVerboseResults.csv", true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,Seed,Result");
        for (ModelTestData data : model)
            fw.append(data.outputVerbose());
        fw.close();

        fw = new FileWriter(TesterTools.resultsPath + "/AllHyperparametersResultsRanked.csv", true);
        fw.append("Chromosome Encoding,Parent Selection,Crossover,Mutation,Population Size,Crossover Rate,Mutation Rate, Elitism Rate,Map,Mean Result");
        Arrays.sort(model);
        for (ModelTestData data : model)
        {
            fw.append(data.outputBestDataOnly());
            fw.append("\nOverall Mean," + data.overallMean);
        }
        fw.close();
    }
}