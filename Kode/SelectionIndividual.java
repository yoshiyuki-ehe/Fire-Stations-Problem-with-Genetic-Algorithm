// IndividualA
import java.util.ArrayList;

public class SelectionIndividual extends Individual {

    public SelectionIndividual(Chromosome chromosome) {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();
        fireStations = new Point[env.fireStationCount];

        // Scan bitstring: setiap bit 1 adalah fire station
        int i, j = 0;
        for (i = 0; i < chromosome.length; i++)
            if (chromosome.getGene(i) == 1) fireStations[j++] = env.getFreePosition(i);
        
        super(chromosome, fireStations);
    }
}