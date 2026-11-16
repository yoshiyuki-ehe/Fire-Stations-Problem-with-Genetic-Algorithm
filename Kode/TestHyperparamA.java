/*
    ini aku sebagian besar ngikutin bentukan TestAllModels cuman aku sesuai in buat hyperparameter aja
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class TestHyperparamA {
   
    public static void main(String [] args) throws Exception{
        // ini aku ngambil dari yang punya [Redacted] yang testAllModels
        Random rand = new Random(20010911);
        TesterTools.initializeSeeds(rand, 10);
        TesterTools.readMaps();
        String[] mapNames = TesterTools.mapNames;
        Environment[] maps = TesterTools.maps;
        double[][] res = new double[mapNames.length][rnds.length];

        BufferedReader br = new BufferedReader(new FileReader(TesterTools.inputPath + "path......csv"));
         
        StringTokenizer stPopulation = new StringTokenizer(br.readLine(), ",");
        int nPopulation = stPopulation.countTokens(); //cari tahu n (n itu jumlah data)
        int[] hyperParamPopulasi = new int[nPopulation]; //pake array biasa karena n diketahui
        for (int i = 0; i < nPopulation; i++) {
            hyperParamPopulasi[i] = Integer.parseInt(stPopulation.nextToken().trim());
        }

        StringTokenizer stCrossover = new StringTokenizer(br.readLine(), ",");
        int nCrossover = stCrossover.countTokens();
        double[] hyperParamCrossover = new double[nCrossover];
        for (int i = 0; i < nCrossover; i++) {
            hyperParamCrossover[i] = Double.parseDouble(stCrossover.nextToken().trim());
        }

        StringTokenizer stMutation = new StringTokenizer(br.readLine(), ",");
        int nMutation = stMutation.countTokens();
        double[] hyperParamMutation = new double[nMutation];
        for (int i = 0; i < nMutation; i++) {
            hyperParamMutation[i] = Double.parseDouble(stMutation.nextToken().trim());
        }

        StringTokenizer stElitism = new StringTokenizer(br.readLine(), ",");
        int nElitism = stElitism.countTokens();
        double[] hyperParamElitism = new double[nElitism];
        for (int i = 0; i < nElitism; i++) {
            hyperParamElitism[i] = Double.parseDouble(stElitism.nextToken().trim());
        }
   
        FireStationsSingleton fireStation = FireStationsSingleton.INSTANCE;
        IndividualGenerator individualGenerate = new SelectionIndividualGenerator();

        //ini yg kata [Redacted] jangan hardcode
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

        //baca lagi dari spesifik model skrng buat nentuin enaknya pake model apa
        BufferedReader brModel = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificModel.csv"));
        StringTokenizer stModel = new StringTokenizer(brModel.readLine(), ",");

        //ambil angkkanya
        int encodingCode  = Integer.parseInt(stModel.nextToken().trim());
        int selectionCode = Integer.parseInt(stModel.nextToken().trim());
        int crossoverCode = Integer.parseInt(stModel.nextToken().trim());
        int mutationCode  = Integer.parseInt(stModel.nextToken().trim());

        //set komponen pake index array, karena array mulai dari 0 di -1
        fireStation.setGenerator(chromosomeEncodings[encodingCode - 1]);
        fireStation.setParentSelection(parentSelections[selectionCode - 1]);
        fireStation.setCrossover(crossovers[crossoverCode - 1]);
        fireStation.setMutation(mutations[mutationCode - 1]);

        //disini harusnya ada file writer aku kurang ngerti cara pakenya nanti boleh tolong tambahin
        FileWriter fw = new FileWriter(TesterTools.resultsPath + "/hyperparamResults.csv", true);
        fw.append("Parameter,Value,Map,Mean,Stdev\n"); // Tulis header kolom
        
        //ini gw ambil dari csv [Redacted] nilainya yang specifichyper parameter buat acuan
        BufferedReader brDefault = new BufferedReader(new FileReader(TesterTools.inputPath + "/SpecificHyperparameter.csv"));
        StringTokenizer stDefault = new StringTokenizer(brDefault.readLine(), ",");

        int bestPopulationSize = Integer.parseInt(stDefault.nextToken().trim());
        double bestCrossoverRate = Double.parseDouble(stDefault.nextToken().trim());
        double bestMutationRate = Double.parseDouble(stDefault.nextToken().trim());
        double bestElitismRate = Double.parseDouble(stDefault.nextToken().trim());
       

        double totalScore = 0.0;
        int evaluationBudget = 0;

        //population tuning
        double bestScore = Double.MAX_VALUE;
        for(int i = 0; i < hyperParamPopulasi.length; i++){
            fireStation.setPopulationSize(hyperParamPopulasi[i]);
            fireStation.setCrossoverRate(bestCrossoverRate);
            fireStation.setMutationRate(bestMutationRate);
            fireStation.setElitismRate(bestElitismRate);

            for(int ii = 0; ii < mapNames.length; ii++){
                fireStation.setEnvironment(maps[ii]);
                //ini aku ngikutin dari [Redacted] evaluation Budgetnya if if nya juga aku ngikutin dari [Redacted] yang testAllModels
                evaluationBudget = 500000;
                if (maps[ii].unreachablePenalty < 400){
                    evaluationBudget = 10000; // ukuran lebih kecil 20 x 20 (1-399)
                } 
                else if (maps[ii].unreachablePenalty < 1600) {
                    evaluationBudget = 50000; // ukuran lebih kecil 40 x 40 (400-1599)
                }
                else if (maps[ii].unreachablePenalty < 6400){
                    evaluationBudget = 200000; // ukuran lebih kecil 80 x 80 (1600-6399)
                }
                fireStation.setEvaluationBudget(evaluationBudget);

                for(int iii = 0; iii < rnds.length; iii++){
                    fireStation.setRandom(rnds[iii]);
                    individualGenerate.updateContext();
                    Population candidate = new Population(individualGenerate, fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, evaluationBudget));
                    fireStation.run();

                    res[ii][iii] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }

                double [] values = TesterTools.meanAndStdev(res[ii]);

                    //disini kalo liat dari punya [Redacted] bakal ada append append cuman aku kurang ngerti cara pakenya
                    //update ini uda gw tambahin append cmn gw sekali lagi ga ngerti, nanti [Redacted] cek ya ini bener apa engga
                    fw.append("PopulationSize," + hyperParamPopulasi[i] + "," + mapNames[ii] + "," + values[0] + "," + values[1] + "\n");
                    fw.flush(); 

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    totalScore += values[0];
            }

            if (totalScore < bestScore){
                bestScore = totalScore;
                bestPopulationSize = hyperParamPopulasi[i];
            }
        }

        //crossover rate
        totalScore = 0.0;
        bestScore = Double.MAX_VALUE;
        for(int i = 0; i < hyperParamCrossover.length; i++){
            fireStation.setPopulationSize(bestPopulationSize);
            fireStation.setCrossoverRate(hyperParamCrossover[i]);
            fireStation.setMutationRate(bestMutationRate);
            fireStation.setElitismRate(bestElitismRate);

            for(int ii = 0; ii < mapNames.length; ii++){
                fireStation.setEnvironment(maps[ii]);
                //ini aku ngikutin dari [Redacted] evaluation Budgetnya if if nya juga aku ngikutin dari [Redacted] yang testAllModels
                evaluationBudget = 500000;
                if (maps[ii].unreachablePenalty < 400){
                    evaluationBudget = 10000; // ukuran lebih kecil 20 x 20 (1-399)
                } 
                else if (maps[ii].unreachablePenalty < 1600) {
                    evaluationBudget = 50000; // ukuran lebih kecil 40 x 40 (400-1599)
                }
                else if (maps[ii].unreachablePenalty < 6400){
                    evaluationBudget = 200000; // ukuran lebih kecil 80 x 80 (1600-6399)
                }
                fireStation.setEvaluationBudget(evaluationBudget);

                for (int iii = 0; iii < rnds.length; iii++){
                    fireStation.setRandom(rnds[iii]);
                    individualGenerate.updateContext();
                    Population candidate = new Population(individualGenerate, fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, evaluationBudget));
                    fireStation.run();

                    res[ii][iii] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }

                double [] values = TesterTools.meanAndStdev(res[ii]);
                    // disini kalo liat dari punya [Redacted] bakal ada append append cuman aku kurang ngerti cara pakenya

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    //update ini uda gw tambahin append cmn gw sekali lagi ga ngerti, nanti [Redacted] cek ya ini bener apa engga
                    fw.append("CrossoverRate," + hyperParamCrossover[i] + "," + mapNames[ii] + "," + values[0] + "," + values[1] + "\n");
                    fw.flush();

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    totalScore += values[0];
            }

            if (totalScore < bestScore){
                bestScore = totalScore;
                bestCrossoverRate = hyperParamCrossover[i];
            }
        }

        //Mutation Rate
        totalScore = 0.0;
        bestScore = Double.MAX_VALUE;
        for(int i = 0; i < hyperParamMutation.length; i++){
            fireStation.setPopulationSize(bestPopulationSize);
            fireStation.setCrossoverRate(bestCrossoverRate);
            fireStation.setMutationRate(hyperParamMutation[i]);
            fireStation.setElitismRate(bestElitismRate);

            for(int ii = 0; ii < mapNames.length; ii++){
                fireStation.setEnvironment(maps[ii]);
                //ini aku ngikutin dari [Redacted] evaluation Budgetnya if if nya juga aku ngikutin dari [Redacted] yang testAllModels
                evaluationBudget = 500000;
                if (maps[ii].unreachablePenalty < 400){
                    evaluationBudget = 10000; // ukuran lebih kecil 20 x 20 (1-399)
                } 
                else if (maps[ii].unreachablePenalty < 1600) {
                    evaluationBudget = 50000; // ukuran lebih kecil 40 x 40 (400-1599)
                }
                else if (maps[ii].unreachablePenalty < 6400){
                    evaluationBudget = 200000; // ukuran lebih kecil 80 x 80 (1600-6399)
                }
                fireStation.setEvaluationBudget(evaluationBudget);

                for(int iii = 0; iii < rnds.length; iii++){
                    fireStation.setRandom(rnds[iii]);
                    individualGenerate.updateContext();
                    Population candidate = new Population(individualGenerate, fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, evaluationBudget));
                    fireStation.run();

                    res[ii][iii] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }

                double [] values = TesterTools.meanAndStdev(res[ii]);
                    // disini kalo liat dari punya [Redacted] bakal ada append append cuman aku kurang ngerti cara pakenya

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    //update ini uda gw tambahin append cmn gw sekali lagi ga ngerti, nanti [Redacted] cek ya ini bener apa engga
                    fw.append("MutationRate," + hyperParamMutation[i] + "," + mapNames[ii] + "," + values[0] + "," + values[1] + "\n");
                    fw.flush();

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    totalScore += values[0];
            }

            if(totalScore < bestScore){
                bestScore = totalScore;
                bestMutationRate= hyperParamMutation[i];
            }
        }

        //elitism
        totalScore = 0.0;
        bestScore = Double.MAX_VALUE;
        for(int i = 0; i < hyperParamElitism.length; i++){
            fireStation.setPopulationSize(bestPopulationSize);
            fireStation.setCrossoverRate(bestCrossoverRate);
            fireStation.setMutationRate(bestMutationRate);
            fireStation.setElitismRate(hyperParamElitism[i]);

            for(int ii = 0; ii < mapNames.length; ii++){
                fireStation.setEnvironment(maps[ii]);
                //ini aku ngikutin dari [Redacted] evaluation Budgetnya if if nya juga aku ngikutin dari [Redacted] yang testAllModels
                evaluationBudget = 500000;
                if (maps[ii].unreachablePenalty < 400){
                    evaluationBudget = 10000; // ukuran lebih kecil 20 x 20 (1-399)
                } 
                else if (maps[ii].unreachablePenalty < 1600) {
                    evaluationBudget = 50000; // ukuran lebih kecil 40 x 40 (400-1599)
                }
                else if (maps[ii].unreachablePenalty < 6400){
                    evaluationBudget = 200000; // ukuran lebih kecil 80 x 80 (1600-6399)
                }
                fireStation.setEvaluationBudget(evaluationBudget); 

                for(int iii = 0; iii < rnds.length; iii++){
                    fireStation.setRandom(rnds[iii]);
                    individualGenerate.updateContext();
                    Population candidate = new Population(individualGenerate, fireStation.getPopulationSize(), fireStation.getElitismRate());
                    fireStation.setPopulation(candidate);

                    // ini angkanya aku ngikutin test model [Redacted]
                    fireStation.setTermination(new FullParameterTermination(candidate, 5,1, evaluationBudget));
                    fireStation.run();

                    res[ii][iii] = fireStation.getPopulation().getIndividual(0).objectiveValue;
                }

                double [] values = TesterTools.meanAndStdev(res[ii]);
                    // disini kalo liat dari punya [Redacted] bakal ada append append cuman aku kurang ngerti cara pakenya

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    //update ini uda gw tambahin append cmn gw sekali lagi ga ngerti, nanti [Redacted] cek ya ini bener apa engga
                    fw.append("ElitismRate," + hyperParamElitism[i] + "," + mapNames[ii] + "," + values[0] + "," + values[1] + "\n");
                    fw.flush();

                    //jujur aku kurang ngerti bagian jumlahin totalScsorenya nanti boleh tolong tambahin
                    totalScore += values[0];
            }

            if(totalScore < bestScore){
                bestScore = totalScore;
                bestElitismRate= hyperParamElitism[i];
            }
        }
    }
}
