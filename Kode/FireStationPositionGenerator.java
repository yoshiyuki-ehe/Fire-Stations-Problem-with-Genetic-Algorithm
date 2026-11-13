import java.util.Random;

public class FireStationPositionGenerator extends IndividualGenerator
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
        int[][] arr = new int[env.fireStationCount][2];
        // memilih posisi acak pada peta untuk setiap fire station
        for (int i = 0; i < arr.length; i++)
        {
            arr[i][0] = rand.nextInt(env.mapHeight);
            arr[i][1] = rand.nextInt(env.mapWidth);
        }

        Chromosome newChromosome = new PointChromosome(arr, env.mapHeight, env.mapWidth);
        return new FireStationPositionIndividual(newChromosome);
    }

    @Override
    public Individual generateSpecificIndividual(Chromosome chromosome)
    {
        return new FireStationPositionIndividual(chromosome);
    }
}