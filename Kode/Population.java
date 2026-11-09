public class Population {
    
    private Individual[] populationArray;
    private double totalFitness;
    private int elitismCount;

    //TODO: refactor constructor untuk menerima parameter IndividualGenerator
    public Population(Individual[] populationArray, double elitsmRate){
        this.populate(populationArray);
        double total = 0;
        this.precomputeTotalFitness();
        this.elitismCount = (int)(elitsmRate * this.getPopulationSize());
    }
    
    public Individual getIndividual(int index){
        return this.populationArray[i];
    }
    
    public int getPopulationSize(){
        return this.populationArray.length;
    }

    public Population populate(Individual[] array){
        this.populationArray = array;
        Arrays.sort(this.populationArray);
        return this;
    }

    public double getTotalFitness(){
        return this.totalFitness;
    }

    private void precomputeTotalFitness(){
        for (Individual individual: this.populationArray) {
            total += individual.getFitness();
        }
        this.totalFitness = total;
    }
    //TODO: Refaktor supaya memakai method crossover yang benar dan mengimplementasi elitism
    
    public Individual[] populateWithNextGeneration(Crossover crossover){
        
        Individual[] nextGeneration = new Individual[getPopulationSize()];
        int eliteIndividualCount = this.getPopulationSize() - this.elitismCount;
        for (int i = 0; i < this.eliteIndividualCount; i++) {
            nextGeneration[i] = crossover.crossover();
        }
        this.populate(nextGeneration);
        this.precomputeTotalFitness();
        return nextGeneration;
    }

}

// method Populate harusnya void
// simpan atribut precomputed totalFitness
// hitung totalFitness secara O(n) pada inisialisasi (generasi pertama)
// ganti totalFitness saat melakukan populateWithNextGeneration()
// nextGeneration size = n * (1 - elitism)
// nextGeneration[i] = crossover.crossover() tidak akan bekerja karena method crossover dapat return array of individuals (jika multiple children)
// sort Population di akhir populateWithNextGeneration()
