import java.util.Arrays;

public class PointChromosome extends Chromosome {
    private int[][] genes;
    private int maxFirst;
    private int maxSecond;

    public PointChromosome(int[][] genes, int maxFirst, int maxSecond) {
        super(genes.length * 2);

        this.genes = genes;
        // memastikan setiap "baris" memiliki minimal 2 "kolom"
        for (int i = 0; i < genes.length; i++) if (genes[i].length < 2) genes[i] = Arrays.copyOf(genes[i], 2);

        // memastikan nilai maxFirst dan maxSecond masuk akal
        this.maxFirst = Math.max(1, maxFirst);
        this.maxSecond = Math.max(1, maxSecond);
    }

    @Override
    public int getGene(int index) {
        int row = index / (length / 2);
        int col = index % (length / 2);

        return genes[row][col];
    }

    @Override
    public void setGene(int index, int value) {
        int row = index / (length / 2);
        int col = index % (length / 2);

        genes[row][col] = value;
    }

    @Override
    public void selfRepair() {
        for (int i = 0; i < genes.length; i++) {
            //clamping menggunakan modulo agar efek dari increment dari IncrementMutation tidak hilang

            //memastikan koordinat pertama berada dalam batas
            genes[i][0] = (genes[i][0] % maxFirst + maxFirst) % maxFirst;

            //memastikan koordinat kedua berada dalam batas
            genes[i][1] = (genes[i][1] % maxSecond + maxSecond) % maxSecond;
        }
    }

    @Override
    public boolean crossable(Chromosome other) {
        if (!(other instanceof PointChromosome)) {
            return false;
        }

        PointChromosome downcast = (PointChromosome) other;

        return genes.length * 2 == other.length
            && maxFirst == downcast.maxFirst
            && maxSecond == downcast.maxSecond;
    }

    @Override
    public Chromosome duplicate() {
        return new PointChromosome(genes.clone(), maxFirst, maxSecond);
    }
}
