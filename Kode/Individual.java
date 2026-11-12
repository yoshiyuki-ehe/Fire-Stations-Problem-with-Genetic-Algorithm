public abstract class Individual implements Comparable<Individual> {
    public final double objectiveValue; // average of min distances
    public final double fitnessValue; // n*m + 0.0001 - objectiveFunction (buat roulette wheel, selalu > 0)
    public final Chromosome chromosome;
    // array firestations dibuat private agar isinya tidak dapat dimodifikasi objek lain
    private Point[] fireStations; // refer to kelas Point pada branch main

    // method abstract wajib buat nanti di override
    // protected abstract void initFitness(); // map chromosome ke fireStations, hitung objectiveFunction dan fitness
    // protected abstract void initObjective();

    // comment from your leader:
    
    // mungkin aku gila melakukan ini tapi
    // karena initFitness() dan initObjective() tidak akan pernah dipanggil kelas lain
    // tidak perlu dibuat pada abstract class
    // tapi nanti bikin aja pada kelas konkretnya private
    
    // kelas konkret hanya menerima 1 parameter constructor, yaitu Chromosome
    // kelas konret tersebut akan menghitung 3 parameter lain sendiri
    public Individual(Chromosome chromosome, double fitnessValue, double objectiveValue, Point[] firestations) {
        this.chromosome = chromosome;
        this.fitnessValue = fitnessValue;
        this.objectiveValue = objectiveValue;
        this.firestations = firestations;
    }
    
    public Point getFireStation(int i) {
        return fireStations[i];
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
// abaikan posisi fire station yang invalid
