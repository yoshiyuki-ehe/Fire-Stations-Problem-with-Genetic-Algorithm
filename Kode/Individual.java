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

    // ukuran peta + 0.0001 - objectiveFunction (buat roulette wheel, selalu > 0)
    private double fitness() {
        return FireStationsSingleton.getEnvironment().unreachablePenalty + 0.0001 - objectiveValue;
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

// comment from your leader:

// mungkin aku gila melakukan ini tapi
// karena initFitness() dan initObjective() tidak akan pernah dipanggil kelas lain
// tidak perlu dibuat pada abstract class
// tapi nanti bikin aja pada kelas konkretnya private

// kelas konkret hanya menerima 1 parameter constructor, yaitu Chromosome
// kelas konret tersebut akan menghitung 3 parameter lain sendiri
// gw ada bikin kelas Point, liat di main kalau sebagai referensi
// kalo lu butuh ngambil koordinat pakai itu
// contoh getFireStations jadinya return Point[] fireStations
// jadinya Individual gada selfCorrect()

// liat detail Chromosome A dan Chromosme B di branch Chromosome
// Individual A pakai Chromosome A
// basically that's it

// Individual B pakai Chromosome B
// abaikan posisi fire station yang invalid
