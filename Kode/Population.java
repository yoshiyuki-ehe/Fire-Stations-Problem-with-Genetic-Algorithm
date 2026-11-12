import java.util.Arrays;
import java.util.Random;

public class Population {
    
    private Individual[] populationArray;
    private double totalFitness;
    //culledGenSize menyimpan ukuran population setelah initial population
    private int culledGenSize;

    //TODO: refactor constructor untuk menerima parameter IndividualGenerator
    public Population(Individual[] populationArray, double elitsmRate){
        this.populate(populationArray);
        this.precomputeTotalFitness();
        int elitismCount = (int)(elitsmRate * this.getPopulationSize());
        this.culledGenSize = this.getPopulationSize() - elitismCount;
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
        this.precomputeTotalFitness();
    }

    public double getTotalFitness(){
        return this.totalFitness;
    }

    private void precomputeTotalFitness(){
        double total = 0;
        for (Individual individual: this.populationArray) {
            total += individual.getFitness();
        }
        this.totalFitness = total;
    }
    
    //mungkin biar clean code, harusnya beberapa parameternya diganti FireStationsSingleton saja.
    public Individual[] populateWithNextGeneration(ParentSelection parentSelection, Random random, Crossover crossover, Mutation mutation, double mutationRate){
        
        Individual[] children = new Individual[this.getPopulationSize()];

        //populate process
        for (int i = 0; i < this.getPopulationSize(); i += 2) {
            Individual[] selectedParents = parentSelection.selectParents(this, random, 2);
            
            Chromosome[] childrensChromosomes = crossover.crossover(selectedParents[0].chromosome, selectedParents[1].chromosome);//mana getter chromosome? :sad:
            
            
            if(random.nextDouble(0,1) < mutationRate){//TODO: masalahnya 1 exclusive, mungkin technically bug, mungkin mau cari solusi lebih baik
                mutation.mutate(childrensChromosomes[0]);
            }

            if(random.nextDouble(0,1) < mutationRate){//TODO: masalahnya 1 exclusive, mungkin technically bug, mungkin mau cari solusi lebih baik
                mutation.mutate(childrensChromosomes[1]);
            }
            
            //mengambil asumsi constructor langsung menghitung fitnessValue dan objectiveValue
            Individual childA = new Individual(childrensChromosomes[0]);
            Individual childB = new Individual(childrensChromosomes[1]);

            children[i] = childA;
            children[i+1] = childB;

        }   
        Arrays.sort(children);
        //sebelum nextGen menggantikan population yang sekarang, cull children untuk membuat nextGen.
        Individual[] nextGen = Arrays.copyOfRange(children, 0, culledGenSize-1);
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
