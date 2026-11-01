public abstract class Individual implements Comparable<Individual> {
    protected double fitness = -1;
    protected int totalDistance = -1;

    // method abstract wajib buat nanti di override
    protected abstract void selfCorrect();
    protected abstract void initFitness();

    public double getFitness() {
        return this.fitness;
    }

    public int getTotalDistance() {
        return this.totalDistance;
    }

    @Override
    public int compareTo(Individual other) {
        // fitness tertinggi didepan
        return Double.compare(other.fitness, this.fitness);
    }
}