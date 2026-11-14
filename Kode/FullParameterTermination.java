public class FullParameterTermination implements Termination{
    private Population population;
    private int maxStagnantGenerations;
    private int remainingGenerations;
    private double minDelta;
    private double bestFitness;
    private int stagnantCount;

    public FullParameterTermination(Population population, int maxStagnantGenerations, double minDelta, int evaluationBudget) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        remainingGenerations = (evaluationBudget + population.getPopulationSize() - 1) / population.getPopulationSize();
        this.minDelta = minDelta;
        bestFitness = 0;
        stagnantCount = 0;
    }

    @Override
    public boolean shouldTerminate() {
        //population pasti dalam keadaan sorted saat method ini dipanggil
        //sehingga fitness tertinggi pasti ada pada individual index 0
        double currentBestFitness = population.getIndividual(0).getFitness();

        //mencatat banyaknya stagnasi berturut-turut
        if (currentBestFitness - bestFitness > minDelta) stagnantCount = 0;
        else stagnantCount++;

        bestFitness = Math.max(bestFitness, currentBestFitness);

        return remainingGenerations-- <= 0 || stagnantCount >= maxStagnantGenerations;
    }
}