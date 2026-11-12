public class ChromosomeB extends Chromosome {
    private int[][] genes;
    private int maxFirst;
    private int maxSecond;

    public ChromosomeB(int[][] genes, int maxFirst, int maxSecond, int length) {
        super(length);

        this.genes = new int[2][genes[0].length];

        this.maxFirst = Math.max(1, maxFirst);
        this.maxSecond = Math.max(1, maxSecond);

        for (int i = 0; i < 2; i++) {
            this.genes[i] = genes[i].clone();
        }
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
        int stationCount = length / 2;

        for (int i = 0; i < stationCount; i++) {
            //memastikan koordinat pertama berada dalam batas
            genes[0][i] = Math.max(0, Math.min(genes[0][i], maxFirst - 1));

            //memastikan koordinat kedua berada dalam batas
            genes[1][i] = Math.max(0, Math.min(genes[1][i], maxSecond - 1));
        }
    }

    @Override
    public boolean crossable(Chromosome other) {
        if (!(other instanceof ChromosomeB)) {
            return false;
        }

        ChromosomeB chromosome = (ChromosomeB) other;

        return length == chromosome.length
            && maxFirst == chromosome.maxFirst
            && maxSecond == chromosome.maxSecond;
    }

    @Override
    public Chromosome duplicate() {
        return new ChromosomeB(genes, maxFirst, maxSecond, length);
    }
}
