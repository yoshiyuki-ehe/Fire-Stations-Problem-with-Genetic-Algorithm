import java.util.Random;

public enum FireStationsSingleton
{
    // singleton dengan enum
    // diakses dengan FireStationsSingleton.INSTANCE
    INSTANCE;

    private Environment environment = null;
    private Population population = null;

    // ParentSelection, Crossover, Mutation, Termination dibuat dengan abstract class agar tidak hardcoded
    private ParentSelection parentSelection = null;
    private Crossover crossover = null;
    private Mutation mutation = null;
    private Termination termination = null;

    private Random rand = null;
    private double elitismRate = 0;
    private double mutationRate = 0;

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

    public void setElitismRate(double elitismRate) {
        this.elitismRate = elitismRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void run()
    {
        if (environment != null && population != null
            && parentSelection != null && crossover != null && mutation != null && termination != null
            && rand != null)
        {
            // population dirun
            // setelah selesai run (Termination Condition terpenuhi) best Individual dapat diakses dari Population
            // nanti gw bikin setelah all of the above jalan
            // just be considerate about this saat kalian bikin algo kalian
        }
    }
}
