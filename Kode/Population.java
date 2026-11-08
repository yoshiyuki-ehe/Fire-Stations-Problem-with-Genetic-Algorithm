public class Population {
    
    private Individual[] populationArray;

    public Individual[] getArray(){
        return this.populationArray;
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
        double total = 0;
        for (Individual individual: this.getArray()) {
            total += individual.getFitness();
        }
        return total;
    }

    //TODO: Refaktor supaya memakai method crossover yang benar.
    public Individual[] populateWithNextGeneration(Crossover crossover){
        Individual[] nextGeneration = new Individual[getPopulationSize()];
        for (int i = 0; i < nextGeneration.length; i++) {
            nextGeneration[i] = crossover.crossover();
        }
        populate(nextGeneration);
        return nextGeneration;
    }

}
