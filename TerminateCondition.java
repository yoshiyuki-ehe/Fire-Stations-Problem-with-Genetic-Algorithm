public class TerminateCondition{

    private int maxStagnantGenerations;
    private double minDelta;
    private double bestFitness;
    private int stagnantCount;

    public TerminateCondition(int maxStagnantGenerations, double minDelta) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        this.minDelta = minDelta;
        this.bestFitness = Double.MIN_VALUE;
        this.stagnantCount = 0;
    }

    public boolean shouldTerminate(Population population, int currentGeneration) {
        Individua individuals[] = population.getArray();
        
        if (individuals == null || individuals.length == 0) {
            return false;
        }

        double currentBestFitness = individuals[0].getFitness();
        for (int i = 1; i < individuals.length; i++) {
            if (individuals[i].getFitness() > currentBestFitness) {
                currentBestFitness = individuals[i].getFitness();
            }
        }

        if (currentBestFitness - this.bestFitness > minDelta) {
            this.bestFitness = currentBestFitness;
            this.stagnantCount = 0;
        } 
        else {
            this.stagnantCount++;
        }

        return this.stagnantCount >= this.maxStagnantGenerations;
    }

    public int getStagnantCount() {
        return this.stagnantCount;
    }

    public double getBestFitness() {
        return this.bestFitness;
    }
}
