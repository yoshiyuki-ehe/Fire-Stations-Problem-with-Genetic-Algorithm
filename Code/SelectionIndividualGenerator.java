import java.util.Random;

public class SelectionIndividualGenerator extends IndividualGenerator
{
    private Environment env;
    private Random rand;

    @Override
    public void updateContext()
    {
        env = FireStationsSingleton.INSTANCE.getEnvironment();
        rand = FireStationsSingleton.INSTANCE.getRandom();
    }

    @Override
    public Individual generateRandomIndividual()
    {
        int[] arr = new int[env.freePositionCount];
        for (int i = env.fireStationCount - 1; i >= 0; i--)
            arr[rand.nextInt(arr.length)] = 1;

        // pemilihan random index sebanyak r kali dapat menghasilkan Chromosome invalid
        // hal tersebut akan ditangani selfRepair() pada instansiasi Chromosome
        Chromosome newChromosome = new CombinationChromosome(arr, env.fireStationCount);
        return new SelectionIndividual(newChromosome);
    }

    @Override
    public Individual generateSpecificIndividual(Chromosome chromosome)
    {
        return new SelectionIndividual(chromosome);
    }
}