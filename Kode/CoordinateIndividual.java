// Individual B
public class CoordinateIndividual extends Individual {

    public CoordinateIndividual(Chromosome chromosome) {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();
        int p = env.fireStationCount;
        fireStations = new Point[p];

        // BoundedCoordinateChromosome menyimpan:
        // index 0 .. p-1      = row
        // index p .. 2*p-1    = col
        int i, r, c;
        for (i = 0; i < p; i++) {
            r = chromosome.getGene(i);
            c = chromosome.getGene(p + i);
            fireStations[i] = new Point(r, c);
        }
        
        super(chromosome, fireStations);
    }
}