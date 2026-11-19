import java.util.Arrays;
import java.util.Random;

public class Population {
    
    private Individual[] populationArray;
    private IndividualGenerator generator;

    //culledGenSize merupakan banyaknya individu non-elit (diganti setiap pergantian generasi)
    private int culledGenSize;
    private int elitismCount;
    private int generation;
    private double[] fitnessPrefixSum;

    public Population(IndividualGenerator generator, int populationSize, double elitismRate){
        this.generator = generator;
        generator.updateContext();
        populationArray = new Individual[populationSize];
        for (int i = 0; i < populationSize; i++) populationArray[i] = generator.generateRandomIndividual();
        generation = 0;
        fitnessPrefixSum = new double[populationSize];
        refreshGeneration();

        // elitismCount dibulatkan ke bawah agar jika ukuran Population 0 tetap dapat menerima Individual baru
        elitismCount = (int)(elitismRate * this.getPopulationSize());
        culledGenSize = populationSize - elitismCount;
    }
    
    public Individual getIndividual(int index){
        return populationArray[index];
    }
    
    public int getPopulationSize(){
        return populationArray.length;
    }

    private void refreshGeneration(){
        Arrays.sort(populationArray);
        computeFitnessPrefixSum();
        generation++;
    }

    public double getFitnessPrefixSum(int index){
        return fitnessPrefixSum[index];
    }

    public int getCurrentGeneration(){
        return generation;
    }

    // fitnessPrefixSum dibuat agar ParentSelectionByFitness dapat dilakukan dengan kompleksitas O(logn)
    private void computeFitnessPrefixSum(){
        fitnessPrefixSum[0] = populationArray[0].fitnessValue;
        for (int i = 1; i < populationArray.length; i++)
            fitnessPrefixSum[i] = fitnessPrefixSum[i - 1] + populationArray[i].fitnessValue;
    }
    
    public void populateWithNextGeneration(){
        ParentSelection parentSelection = FireStationsSingleton.INSTANCE.getParentSelection();
        Random random = FireStationsSingleton.INSTANCE.getRandom();
        Crossover crossover = FireStationsSingleton.INSTANCE.getCrossover();
        Mutation mutation = FireStationsSingleton.INSTANCE.getMutation();
        double mutationRate = FireStationsSingleton.INSTANCE.getMutationRate();
        double crossoverRate = FireStationsSingleton.INSTANCE.getCrossoverRate();

        // [BUG FIX] Ukuran nextGen harus sebesar populationArray.length (bukan culledGenSize)
        Individual[] nextGen = new Individual[populationArray.length];

        //populate process
        int i = 0, j;
        while (i < culledGenSize) {
            Individual[] selectedParents = parentSelection.selectParents(this, 2);
            if (random.nextDouble(0, 1) < crossoverRate)
            {
                Chromosome[] childrensChromosomes = crossover.crossover(selectedParents[0].chromosome, selectedParents[1].chromosome);

                // [BUG FIX] Loop mulai dari j = 0 (bukan 1) dan akses childrensChromosomes[j] (bukan [i])
                for(j = 0; j < childrensChromosomes.length; j++){
                    if(random.nextDouble(0,1) < mutationRate){
                        mutation.mutate(childrensChromosomes[j]);
                    }
                }
                
                for(j = 0; j < childrensChromosomes.length && i + j < culledGenSize; j++){
                    nextGen[i+j] = generator.generateSpecificIndividual(childrensChromosomes[j]);
                }
                i += childrensChromosomes.length;
            }
        }   

        // salin individu elit ke nextGen dan perbarui populationArray
        for (i = 0; i < elitismCount; i++) nextGen[culledGenSize + i] = populationArray[i];
        populationArray = nextGen; // [BUG FIX] Update populationArray dengan generasi baru
        refreshGeneration();
    }
}