public class FullParameterTermination implements Termination{
    private int maxStagnantGenerations;
    private int evaluationBudget;
    private double minDelta;
    private double bestFitness;
    private int stagnantCount;

    public FullParameterTermination(double minDelta, int maxStagnantGenerations, int evaluationBudget) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        this.evaluationBudget = evaluationBudget;
        this.minDelta = minDelta;
        this.bestFitness = 0;
        this.stagnantCount = 0;
    }

    @Override
    public boolean shouldTerminate() {
        Population population = FireStationsSingleton.INSTANCE.getPopulation();

        //population pasti dalam keadaan sorted saat method ini dipanggil
        //sehingga fitness tertinggi pasti ada pada individual index 0
        double currentBestFitness = population.getIndividual(0).getFitness();

        //mencatat banyaknya stagnasi berturut-turut
        if (currentBestFitness - this.bestFitness > minDelta) {
            this.stagnantCount = 0;
        } 
        else {
            this.stagnantCount++;
        }

        bestFitness = Math.max(bestFitness, currentBestFitness);

        return this.stagnantCount >= this.maxStagnantGenerations || evaluationBudget <= 0;
    }
}
