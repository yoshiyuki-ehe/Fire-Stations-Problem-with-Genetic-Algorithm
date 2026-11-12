public class ChromosomeA extends Chromosome {
    private int[] genes;
    public final int r;

    public ChromosomeA(int[] genes, int r, int length) {
        super(length);
        this.genes = genes.clone();
        this.r = r;
    }

    @Override
    public int getGene(int index) {
        return genes[index];
    }

    @Override
    public void setGene(int index, int value) {
        genes[index] = value;
    }

    @Override
    public void selfRepair() {
        int count = 0;
        int target = Math.max(0, Math.min(r, length));

        //memastikan hanya ada angka 0 dan 1
        for (int i = 0; i < length; i++) {
            genes[i] = genes[i] == 1 ? 1 : 0;

            if (genes[i] == 1) {
                count++;
            }
        }

        //mengurangi jumlah angka 1 jika melebihi r
        if (count > target) {
            for (int i = length - 1; i >= 0 && count > target; i--) {
                if (genes[i] == 1) {
                    genes[i] = 0;
                    count--;
                }
            }
        }

        //menambah jumlah angka 1 jika kurang dari r
        else if (count < target) {
            for (int i = 0; i < length && count < target; i++) {
                if (genes[i] == 0) {
                    genes[i] = 1;
                    count++;
                }
            }
        }
    }

    @Override
    public boolean crossable(Chromosome other) {
        if (!(other instanceof ChromosomeA)) {
            return false;
        }

        ChromosomeA chromosome = (ChromosomeA) other;

        return length == chromosome.length && r == chromosome.r;
    }

    @Override
    public Chromosome duplicate() {
        return new ChromosomeA(genes, r, length);
    }
}
