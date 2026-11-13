public class FullParameterTermination implements Termination{
    private int maxStagnantGenerations;
    private double minDelta;
    private double bestFitness;
    private int stagnantCount;

    public FullParameterTermination(int maxStagnantGenerations, double minDelta) {
        this.maxStagnantGenerations = maxStagnantGenerations;
        this.minDelta = minDelta;
        this.bestFitness = 0;
        this.stagnantCount = 0;
    }

    @Override
    public boolean shouldTerminate(Population population) {
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

        return this.stagnantCount >= this.maxStagnantGenerations;
    }
}
