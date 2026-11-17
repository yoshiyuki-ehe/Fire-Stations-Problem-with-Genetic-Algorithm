public abstract class Individual implements Comparable<Individual> {
    public final double objectiveValue; 
    public final double fitnessValue;
    public final Chromosome chromosome;
    private Point[] fireStations;

    // pencarian fire station dilakukan oleh masing masing ekstensi Individual (karena jenis Chromosome yang berbeda)
    // ekstensi tersebut kemudian akan mengirimkan Chromosome dan daftar lokasi fire station ke kelas parent
    protected Individual(Chromosome chromosome, Point[] fireStations) {
        this.chromosome = chromosome;
        this.fireStations = fireStations;
        objectiveValue = objective();
        fitnessValue = fitness();
    }
    
    public Point getFireStation(int index) {
        return fireStations[index];
    }

    @Override
    public int compareTo(Individual other) {
        // fitness tertinggi yg didepan
        return Double.compare(other.fitnessValue, this.fitnessValue);
    }

    // nilai objective terburuk adalah ukuran map itu sendiri (sama dengan unreachablePenalty)
    // fungsi fitness di bawah memastikan dalam kasus terekstrim, nilai fitness terendah adalah 0.000001
    // justifikasi penambahan 0.000001 adalah untuk menghindari kasus dimana pada seluruh Populasi memiliki fitness 0 dan
    // Parent Selection by Fitness (Roulette Wheel) selalu mengambil individu pada indeks 0
    // jika hal tesebut terjadi individu yang masuk ke generasi baru adalah duplikat dari individu pada indeks 0
    private double fitness() {
        return FireStationsSingleton.INSTANCE.getEnvironment().unreachablePenalty - objectiveValue + 0.000001;
    }

    // average of min distances
    private double objective() {
        Environment env = FireStationsSingleton.INSTANCE.getEnvironment();

        double totalMinDist = 0;
        int minDist, h, d;

        for (h = 0; h < env.houseCount; h++) {
            minDist = env.unreachablePenalty;

            for (Point s : fireStations) {
                d = env.getDist(h, s.row, s.col);
                minDist = Math.min(minDist, d);
            }

            totalMinDist += minDist;
        }
        
        return env.houseCount > 0 ? (totalMinDist / env.houseCount) : 0;
    }
}