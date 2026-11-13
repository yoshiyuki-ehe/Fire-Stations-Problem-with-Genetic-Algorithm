import java.util.Random;

public enum FireStationsSingleton
{
    // singleton dengan enum
    // diakses dengan FireStationsSingleton.INSTANCE
    INSTANCE;

    private Environment environment = null;
    private Population population = null;
    private IndividualGenerator generator = null;

    // ParentSelection, Crossover, Mutation, Termination dibuat dengan abstract class agar tidak hardcoded
    private ParentSelection parentSelection = null;
    private Crossover crossover = null;
    private Mutation mutation = null;
    private Termination termination = null;

    private Random rand = null;
    private double elitismRate = 0;
    private double crossoverRate = 0;
    private double mutationRate = 0;
    private int populationSize = 0;

    // getter dan setter
    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }
    
    public IndividualGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(IndividualGenerator generator) {
        this.generator = generator;
    }

    public ParentSelection getParentSelection() {
        return parentSelection;
    }

    public void setParentSelection(ParentSelection parentSelection) {
        this.parentSelection = parentSelection;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public void setCrossover(Crossover crossover) {
        this.crossover = crossover;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public Random getRandom() {
        return this.rand;
    }

    public void setRandom(Random rand) {
        this.rand = rand;
    }

    public double getElitismRate() {
        return elitismRate;
    }

    // elitismRate, crossoverRate, dan mutationRate harus bernilai [0, 1]
    public void setElitismRate(double elitismRate) {
        this.elitismRate = Math.max(0, Math.min(elitismRate, 1));
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = Math.max(0, Math.min(crossoverRate, 1));
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = Math.max(0, Math.min(mutationRate, 1));
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void run()
    {
        if (environment != null && population != null && generator != null
            && parentSelection != null && crossover != null && mutation != null && termination != null
            && rand != null)
        {
            while (!termination.shouldTerminate(population))
                population.populateWithNextGeneration();

            // setelah selesai run (Terminate Condition terpenuhi) Individual terbaik dapat diakses dari Population
        }
    }
}
