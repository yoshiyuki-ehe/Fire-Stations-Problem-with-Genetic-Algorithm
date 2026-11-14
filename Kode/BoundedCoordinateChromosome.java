import java.util.Arrays;

public class BoundedCoordinateChromosome extends Chromosome {
    private int[][] genes;
    private int maxFirst;
    private int maxSecond;

    public BoundedCoordinateChromosome(int[][] genes, int maxFirst, int maxSecond) {
        super(genes.length * 2);

        this.genes = genes;
        // memastikan setiap "baris" memiliki minimal 2 "kolom"
        for (int i = 0; i < genes.length; i++) if (genes[i].length < 2) genes[i] = Arrays.copyOf(genes[i], 2);

        // memastikan nilai maxFirst dan maxSecond masuk batasan
        this.maxFirst = Math.max(1, maxFirst);
        this.maxSecond = Math.max(1, maxSecond);

        this.selfRepair();
    }

    // sebutlah p jumlah titik yang direpresentasikan (sebanyak ukuran dimensi pertama genes)
    // index 0 .. p-1      = row
    // index p .. 2*p-1    = col
    @Override
    public int getGene(int index) {
        int id = index % genes.length;
        int dimension = index / genes.length;

        return genes[id][dimension];
    }

    @Override
    public void setGene(int index, int value) {
        int id = index % genes.length;
        int dimension = index / genes.length;

        genes[id][dimension] = value;
    }

    @Override
    public void selfRepair() {
        for (int i = 0; i < genes.length; i++) {
            //clamping menggunakan modulo agar efek dari increment dari IncrementMutation tidak hilang

            // ((a % bound) + b) % bound untuk menangani kasus nilai a negatif tetap menghasilkan nilai [0, bound]
            //memastikan koordinat pertama berada dalam batas
            genes[i][0] = (genes[i][0] % maxFirst + maxFirst) % maxFirst;

            //memastikan koordinat kedua berada dalam batas
            genes[i][1] = (genes[i][1] % maxSecond + maxSecond) % maxSecond;
        }
    }

    @Override
    public boolean crossable(Chromosome other) {
        if (!(other instanceof BoundedCoordinateChromosome)) {
            return false;
        }

        BoundedCoordinateChromosome downcast = (BoundedCoordinateChromosome) other;

        return genes.length * 2 == other.length
            && maxFirst == downcast.maxFirst
            && maxSecond == downcast.maxSecond;
    }

    @Override
    public Chromosome duplicate() {
        return new BoundedCoordinateChromosome(genes.clone(), maxFirst, maxSecond);
    }
}
