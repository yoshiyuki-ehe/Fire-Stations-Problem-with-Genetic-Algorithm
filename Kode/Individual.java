public abstract class Individual implements Comparable<Individual> {
    protected int[][] fireStations; // int[p][2]
    protected double objectiveFunction = -1; // sum of min distances (total jarak)
    protected double fitness = -1; // n*m + 0.0001 - objectiveFunction (buat roulette wheel, selalu > 0)

    // method abstract wajib buat nanti di override
    protected abstract void selfCorrect(); // koreksi chromosome agar valid
    protected abstract void initFitness(); // map chromosome ke fireStations, hitung objectiveFunction dan fitness
    protected abstract Chromosome getChromosome(); // method untuk return chromosome

    public int[][] getFireStations() {
        return this.fireStations;
    }

    public double getObjectiveFunction() {
        return this.objectiveFunction;
    }

    public double getFitness() {
        return this.fitness;
    }


    @Override
    public int compareTo(Individual other) {
        // fitness tertinggi yg didepan
        return Double.compare(other.fitness, this.fitness);
    }
}

// gw ada bikin kelas Point, liat di main kalau sebagai referensi
// kalo lu butuh ngambil koordinat pakai itu
// contoh getFireStations jadinya return Point[] fireStations
// jadinya Individual gada selfCorrect()

// liat detail Chromosome A dan Chromosme B di branch Chromosome
// Individual A pakai Chromosome A
// basically that's it

// Individual B pakai Chromosome B
// khusus ini ada "self repair"
// semua posisi fire station harus berada di free position
