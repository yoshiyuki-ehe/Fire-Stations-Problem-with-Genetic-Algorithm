import java.util.*;
import java.io.*;

public class TestHyperparameterTuning {
   
    public static void main(String [] args) throws Exception{
        int i, j, k;
        
        TesterTools.setOtherParameters();
        int[] seeds = TesterTools.seeds;

        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;

        // KODE LAMA:
        // BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "CombinationHyperparameters.txt"));

        // [PERBAIKAN PATH] Membaca CombinationHyperparameters.txt dari folder Input/Hyperparameters/
        BufferedReader br = new BufferedReader(new FileReader(TesterTools.hyperparametersPath + "/CombinationHyperparameters.txt"));
         
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
        // KODE LAMA:
        // BufferedReader brModel = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificModel.txt"));

        // [PERBAIKAN PATH] Membaca SpecificModel.txt dari folder Input/Hyperparameters/
        BufferedReader brModel = new BufferedReader(new FileReader(TesterTools.hyperparametersPath + "/SpecificModel.txt"));
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

        FileWriter rankings = new FileWriter(TesterTools.resultsPath + "/BestParameterPerRun.txt", true);

        fireStation.setCrossoverRate(hyperParamCrossover[0]);
        fireStation.setMutationRate(hyperParamMutation[0]);
        fireStation.setElitismRate(hyperParamElitism[0]);

        //population size tuning
        ModelTestData[] populationTest = new ModelTestData[hyperParamPopulasi.length];
        for(i = 0; i < hyperParamPopulasi.length; i++){
            fireStation.setPopulationSize(hyperParamPopulasi[i]);
            double[][] res = new double[maps.length][seeds.length];

            for(j = 0; j < mapNames.length; j++){
                fireStation.setEnvironment(maps[j]);
                fireStation.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[j]));

                for(k = 0; k < seeds.length; k++){
                    fireStation.setRandom(new Random(seeds[k]));
                    Population candidate = new Population(fireStation.getGenerator(), fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    fireStation.setTermination(new FullParameterTermination(candidate,
                        TesterTools.determineMaxStagnantGenerations(fireStation.getPopulationSize()),
                        TesterTools.minRelativeChange, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            populationTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
        }
        TesterTools.writeVerboseToFile("TestPopulationTuning.csv", populationTest);
        Arrays.sort(populationTest);
        TesterTools.writeOverallMeanToFile("/TestPopulationTuningOverallMean.csv", populationTest);
        rankings.append("Population Size:");
        for (i = 0; i < populationTest.length; i++)
            rankings.append(" " + populationTest[i].populationSize);
        rankings.append("\n");

        fireStation.setPopulationSize(populationTest[0].populationSize);
        //crossover rate tuning
        ModelTestData[] crossoverTest = new ModelTestData[hyperParamCrossover.length];
        for(i = 0; i < hyperParamCrossover.length; i++){
            fireStation.setCrossoverRate(hyperParamCrossover[i]);
            double[][] res = new double[maps.length][seeds.length];

            for(j = 0; j < mapNames.length; j++){
                fireStation.setEnvironment(maps[j]);
                fireStation.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[j]));

                for (k = 0; k < seeds.length; k++){
                    fireStation.setRandom(new Random(seeds[k]));
                    Population candidate = new Population(fireStation.getGenerator(), fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    fireStation.setTermination(new FullParameterTermination(candidate,
                        TesterTools.determineMaxStagnantGenerations(fireStation.getPopulationSize()),
                        TesterTools.minRelativeChange, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            crossoverTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
        }
        TesterTools.writeVerboseToFile("TestCrossoverTuning.csv", crossoverTest);
        Arrays.sort(crossoverTest);
        TesterTools.writeOverallMeanToFile("/TestCrossoverTuningOverallMean.csv", crossoverTest);
        rankings.append("Crossover Rate:");
        for (i = 0; i < crossoverTest.length; i++)
            rankings.append(" " + crossoverTest[i].crossoverRate);
        rankings.append("\n");
        
        
        fireStation.setCrossoverRate(crossoverTest[0].crossoverRate);
        //mutation rate tuning
        ModelTestData[] mutationTest = new ModelTestData[hyperParamMutation.length];
        for(i = 0; i < hyperParamMutation.length; i++){
            fireStation.setMutationRate(hyperParamMutation[i]);
            double[][] res = new double[maps.length][seeds.length];

            for(j = 0; j < mapNames.length; j++){
                fireStation.setEnvironment(maps[j]);
                fireStation.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[j]));

                for (k = 0; k < seeds.length; k++){
                    fireStation.setRandom(new Random(seeds[k]));
                    Population candidate = new Population(fireStation.getGenerator(), fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    fireStation.setTermination(new FullParameterTermination(candidate,
                        TesterTools.determineMaxStagnantGenerations(fireStation.getPopulationSize()),
                        TesterTools.minRelativeChange, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            mutationTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
        }
        TesterTools.writeVerboseToFile("TestMutationTuning.csv", mutationTest);
        Arrays.sort(mutationTest);
        TesterTools.writeOverallMeanToFile("/TestMutationTuningOverallMean.csv", mutationTest);
        rankings.append("Mutation Rate:");
        for (i = 0; i < mutationTest.length; i++)
            rankings.append(" " + mutationTest[i].mutationRate);
        rankings.append("\n");


        fireStation.setMutationRate(mutationTest[0].mutationRate);
        //elitism rate tuning
        ModelTestData[] elitismTest = new ModelTestData[hyperParamElitism.length];
        for(i = 0; i < hyperParamElitism.length; i++){
            fireStation.setElitismRate(hyperParamElitism[i]);
            double[][] res = new double[maps.length][seeds.length];

            for(j = 0; j < mapNames.length; j++){
                fireStation.setEnvironment(maps[j]);
                fireStation.setEvaluationBudget(TesterTools.determineEvaluationBudget(maps[j]));

                for (k = 0; k < seeds.length; k++){
                    fireStation.setRandom(new Random(seeds[k]));
                    Population candidate = new Population(fireStation.getGenerator(), fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    fireStation.setTermination(new FullParameterTermination(candidate,
                        TesterTools.determineMaxStagnantGenerations(fireStation.getPopulationSize()),
                        TesterTools.minRelativeChange, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            elitismTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
        }
        TesterTools.writeVerboseToFile("TestElitismTuning.csv", elitismTest);
        Arrays.sort(elitismTest);
        TesterTools.writeOverallMeanToFile("/TestElitismTuningOverallMean.csv", elitismTest);
        rankings.append("Elitism Rate:");
        for (i = 0; i < elitismTest.length; i++)
            rankings.append(" " + elitismTest[i].elitismRate);
        rankings.close();
    }
}