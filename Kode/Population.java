import java.util.Arrays;
import java.util.Random;

public class Population {
    
    private Individual[] populationArray;
    private double totalFitness;
    //culledGenSize menyimpan ukuran population setelah initial population
    private int culledGenSize;
    private double[] prefixSumFitness;

    //TODO: refactor constructor untuk menerima parameter IndividualGenerator
    public Population(Individual[] populationArray, double elitsmRate){
        this.populate(populationArray);
        int elitismCount = (int)(elitsmRate * this.getPopulationSize());
        this.culledGenSize = this.getPopulationSize() - elitismCount;
        
    }
    
    public void preComputePrefixSumFitness(){
        double curSum = 0;
        for (int i = 0; i < this.getPopulationSize(); i++) {
            curSum += getIndividual(i).getFitness();
            this.prefixSumFitness[i] = curSum;
        }
    }
    
    public Individual getIndividual(int index){
        return this.populationArray[index];
    }
    
    public int getPopulationSize(){
        return this.populationArray.length;
    }

    public void populate(Individual[] array){
        this.populationArray = array;
        Arrays.sort(this.populationArray);
        this.preComputePrefixSumFitness();
    }

    public double getTotalFitness(){
        return this.prefixSumFitness[prefixSumFitness.length-1];
    }
    
    private int getCulledGenSize(){
        return this.culledGenSize;
    }
    //mungkin biar clean code, harusnya beberapa parameternya diganti FireStationsSingleton saja.
    public Individual[] populateWithNextGeneration(ParentSelection parentSelection, Random random, Crossover crossover, Mutation mutation, double mutationRate){
        
        Individual[] children = new Individual[this.getPopulationSize()];

        //populate process
        for (int i = 0; i < this.getPopulationSize(); i += 2) {
            Individual[] selectedParents = parentSelection.selectParents(this, random, 2);
            
            Chromosome[] childrensChromosomes = crossover.crossover(selectedParents[0].chromosome, selectedParents[1].chromosome);//mana getter chromosome? :sad:

            for(int j = 1; j < childrensChromosomes.length; j++){
                if(random.nextDouble(0,1) < mutationRate){//TODO: masalahnya 1 exclusive, mungkin technically bug, mungkin mau cari solusi lebih baik
                    mutation.mutate(childrensChromosomes[i]);
                }
            }
            
            //mengambil asumsi constructor langsung menghitung fitnessValue dan objectiveValue,
            for(int j = 0; j < childrensChromosomes.length; j++){
                children[i+j] = new Individual(childrensChromosomes[j]);
            }
            
        }   
        Arrays.sort(children);
        //sebelum nextGen menggantikan population yang sekarang, cull children untuk membuat nextGen.
        Individual[] nextGen = Arrays.copyOfRange(children, this.getPopulationSize()-this.getCulledGenSize(), this.getPopulationSize()-1);
        populate(nextGen);
    }

}

// method Populate harusnya void
// simpan atribut precomputed totalFitness
// hitung totalFitness secara O(n) pada inisialisasi (generasi pertama)
// ganti totalFitness saat melakukan populateWithNextGeneration()
// nextGeneration size = n * (1 - elitism)
// nextGeneration[i] = crossover.crossover() tidak akan bekerja karena method crossover dapat return array of individuals (jika multiple children)
// sort Population di akhir populateWithNextGeneration()
