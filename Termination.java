
public class Termination implements TerminateCondition{

    private int maxStagnantGenerations;
    private double minDelta;
    private double bestFitness;
    private int stagnantCount;

    public Termination(int maxStagnantGenerations, double minDelta) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        this.minDelta = minDelta;
        this.bestFitness = 0;
        this.stagnantCount = 0;
    }


    @Override
    public boolean shouldTerminate(Population population, int currentGeneration) {
         Individual individuals[] = population.getArray();
        
        if (individuals == null || individuals.length == 0) {
            return true;
        }

        double currentBestFitness = individuals[0].getFitness();

        if (currentBestFitness - this.bestFitness > minDelta) {
            this.stagnantCount = 0;
        } 
        else {
            this.stagnantCount++;
        }

        if (currentBestFitness > this.bestFitness) {
            this.bestFitness = currentBestFitness;
        }

        return this.stagnantCount >= this.maxStagnantGenerations;
    }

}
