public class FullParameterTermination implements Termination{
    private Population population;
    private int maxStagnantGenerations;
    private int remainingGenerations;
    private double minRelativeChange;
    private double bestFitness;
    private int stagnantCount;

    public FullParameterTermination(Population population, int maxStagnantGenerations, double minRelativeChange, int evaluationBudget) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        remainingGenerations = (evaluationBudget + population.getPopulationSize() - 1) / population.getPopulationSize() - 1;
        this.minRelativeChange = minRelativeChange;
        bestFitness = 0.000001;
        stagnantCount = 0;
    }

    @Override
    public boolean shouldTerminate() {
        //population pasti dalam keadaan sorted saat method ini dipanggil
        //sehingga fitness tertinggi pasti ada pada individual index 0
        double currentBestFitness = population.getIndividual(0).fitnessValue;

        //mencatat banyaknya stagnasi berturut-turut berdasarkan perubahan relatif
        if ((currentBestFitness - bestFitness) / bestFitness > minRelativeChange) stagnantCount = 0;
        else stagnantCount++;

        bestFitness = Math.max(bestFitness, currentBestFitness);

        return remainingGenerations-- <= 0 || stagnantCount >= maxStagnantGenerations;
    }
}