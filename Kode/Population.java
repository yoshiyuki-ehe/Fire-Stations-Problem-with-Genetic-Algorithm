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
        return this;
    }

    public double getTotalFitness(){
        double total = 0;
        for (Individual individual: this.getArray()) {
            total += individual.getFitness();
        }
        return total;
    }
    //TODO: populateWithNextGeneration memang belum beres
    // public Population populateWithNextGeneration(){
    //     return this;
    // }

}
