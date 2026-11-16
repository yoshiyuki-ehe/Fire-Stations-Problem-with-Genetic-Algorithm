import java.util.*;
import java.io.*;

public class TestHyperparamA {
   
    public static void main(String [] args) throws Exception{
        int i, j, k;
        
        TesterTools.setOtherParameters();
        int[] seeds = TesterTools.seeds;

        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;

        BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "CombinationHyperparameters.txt"));
         
        StringTokenizer stPopulation = new StringTokenizer(br.readLine(), ",");
        int nPopulation = stPopulation.countTokens(); //cari tahu n (n itu jumlah data)
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

        //baca lagi dari spesifik model skrng buat nentuin enaknya pake model apa
        BufferedReader brModel = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificModel.txt"));
        StringTokenizer stModel = new StringTokenizer(brModel.readLine(), ",");
        brModel.close();

        //ambil angkkanya
        int encodingCode  = Integer.parseInt(stModel.nextToken().trim());
        int selectionCode = Integer.parseInt(stModel.nextToken().trim());
        int crossoverCode = Integer.parseInt(stModel.nextToken().trim());
        int mutationCode  = Integer.parseInt(stModel.nextToken().trim());

        //set komponen pake index array, karena array mulai dari 0 di -1
        fireStation.setGenerator(TesterTools.chromosomeEncodings[encodingCode]);
        fireStation.setParentSelection(TesterTools.parentSelections[selectionCode]);
        fireStation.setCrossover(TesterTools.crossovers[crossoverCode]);
        fireStation.setMutation(TesterTools.mutations[mutationCode]);

        //disini harusnya ada file writer aku kurang ngerti cara pakenya nanti boleh tolong tambahin
        FileWriter fw = new FileWriter(TesterTools.resultsPath + "/TestAPopulationRun.csv", true);
        fw.append("Parameter,Value,Map,Mean\n"); // Tulis header kolom

        FileWriter rankings = new FileWriter(TesterTools.resultsPath + "/BestParameterPerRun.txt", true);

        fireStation.setCrossoverRate(hyperParamCrossover[0]);
        fireStation.setMutationRate(hyperParamMutation[0]);
        fireStation.setElitismRate(hyperParamElitism[0]);

        //population tuning
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

                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            populationTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
            fw.append(populationTest[i].outputVerbose());
        }
        Arrays.sort(populationTest);
        rankings.append("Population Size:");
        for (i = 0; i < populationTest.length; i++)
            rankings.append(" " + populationTest[i].populationSize);
        rankings.append("\n");
        
        
        
        fireStation.setPopulationSize(populationTest[0].populationSize);
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

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            crossoverTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
            fw.append(crossoverTest[i].outputVerbose());
        }
        Arrays.sort(crossoverTest);
        rankings.append("Crossover Rate:");
        for (i = 0; i < crossoverTest.length; i++)
            rankings.append(" " + crossoverTest[i].crossoverRate);
        rankings.append("\n");
        
        
        
        fireStation.setCrossoverRate(crossoverTest[0].crossoverRate);
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

                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            mutationTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
            fw.append(mutationTest[i].outputVerbose());
        }
        Arrays.sort(mutationTest);
        rankings.append("Mutation Rate:");
        for (i = 0; i < mutationTest.length; i++)
            rankings.append(" " + mutationTest[i].mutationRate);
        rankings.append("\n");



        fireStation.setMutationRate(mutationTest[0].mutationRate);
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

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, fireStation.getEvluationBudget()));
                    fireStation.run();

                    res[j][k] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }
            }

            elitismTest[i] = new ModelTestData(encodingCode, selectionCode, crossoverCode, mutationCode,
                fireStation.getPopulationSize(), fireStation.getCrossoverRate(), fireStation.getMutationRate(), fireStation.getElitismRate(), res);
            fw.append(elitismTest[i].outputVerbose());
        }
        Arrays.sort(elitismTest);
        rankings.append("Elitism Rate:");
        for (i = 0; i < elitismTest.length; i++)
            rankings.append(" " + elitismTest[i].elitismRate);
        
        rankings.close();
        fw.close();
    }
}
