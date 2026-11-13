// Individual B
public class CoordinateIndividual extends Individual {

    public CoordinateIndividual(Chromosome chromosome) {
        super(chromosome);
        initFireStations();
        initFitness();
    }

    private void initFireStations() {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();
        int p = env.fireStationCount;
        this.fireStations = new Point[p];

        // CoordinateChromosome menyimpan:
        // index 0 .. p-1      = row
        // index p .. 2*p-1    = col
        for (int i = 0; i < p; i++) {
            int r = chromosome.getGene(i);
            int c = chromosome.getGene(p + i);
            this.fireStations[i] = new Point(r, c);
        }
    }

    private void initFitness() {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();

        double totalMinDist = 0;
        for (int h = 0; h < env.houseCount; h++) {
            int minDist = Integer.MAX_VALUE;

            for (Point s : this.fireStations) {
                // Abaikan posisi invalid: jika koordinat di luar map atau obstacle,
                // env.getDist() / penalty akan menanganinya
                int d;
                if (s.row < 0 || s.row >= env.mapHeight || s.col < 0 || s.col >= env.mapWidth) {
                    d = env.unreachablePenalty;
                } else {
                    d = env.getDist(h, s.row, s.col);
                }

                if (d < minDist) {
                    minDist = d;
                }
            }
            totalMinDist += minDist;
        }

        this.objectiveValue = env.houseCount > 0 ? (totalMinDist / env.houseCount) : 0;

        this.fitnessValue = (env.mapHeight * env.mapWidth) + 0.0001 - this.objectiveValue;
    }
}