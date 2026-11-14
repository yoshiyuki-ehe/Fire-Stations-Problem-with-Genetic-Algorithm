// IndividualA
import java.util.ArrayList;

public class GridIndividual extends Individual {

    public GridIndividual(Chromosome chromosome) {
        super(chromosome);
        initFireStations();
        initFitness();
    }

    private void initFireStations() {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();
        ArrayList<Point> stationsList = new ArrayList<>();

        // Scan bitstring: setiap bit 1 adalah fire station
        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome.getGene(i) == 1) {
                int r = i / env.mapWidth;
                int c = i % env.mapWidth;
                stationsList.add(new Point(r, c));
            }
        }

        this.fireStations = stationsList.toArray(new Point[0]);
    }

    private void initFitness() {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();

        double totalMinDist = 0;
        for (int h = 0; h < env.houseCount; h++) {
            int minDist = Integer.MAX_VALUE;

            if (this.fireStations.length == 0) {
                minDist = env.unreachablePenalty;
            } else {
                for (Point s : this.fireStations) {
                    int d = env.getDist(h, s.row, s.col);
                    if (d < minDist) {
                        minDist = d;
                    }
                }
            }
            totalMinDist += minDist;
        }

        this.objectiveValue = env.houseCount > 0 ? (totalMinDist / env.houseCount) : 0;

        this.fitnessValue = (env.mapHeight * env.mapWidth) + 0.0001 - this.objectiveValue;
    }
}