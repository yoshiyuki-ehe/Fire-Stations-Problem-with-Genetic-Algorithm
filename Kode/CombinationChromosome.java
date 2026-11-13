public class CombinationChromosome extends Chromosome {
    //Kombinasi direpresentasikan sebagai BitString yang memiliki nilai 1 sebanyak tepat r
    private int[] genes;
    public final int r;

    public CombinationChromosome(int[] genes, int r) {
        super(genes.length);
        this.genes = genes;
        // membatasi nilai r agar menjadi nilai r yang valid untuk Kombinasi dengan panjang gen
        this.r = Math.max(0, Math.min(r, genes.length));
        this.selfRepair();
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
        int i, count = 0;

        //memastikan hanya ada angka 0 dan 1 sambil menghitung jumlah 1
        for (i = 0; i < genes.length; i++) {
            genes[i] = (genes[i] % 2 + 2) % 2;
            // nilai gen biner (0 atau 1)
            // setiap gen bernilai 0 akan menambah count sebanyak 0 (tidak menambah)
            // setiap gen bernilai 1 akan menambah count sebanyak 1
            count += genes[i];
        }

        //mengurangi jumlah angka 1 jika melebihi r
        for (i = 0; count > r; i--) {
            // jika nilai gen 1, kurangi count sebanyak 1
            count -= genes[i];
            genes[i] = 0;
        }

        //menambah jumlah angka 1 jika kurang dari r
        for (i = 0; count < r; i++) {
            // jika nilai gen 0, tambah count sebanyak 1
            count += 1 - genes[i];
            genes[i] = 1;
        }
    }

    @Override
    public boolean crossable(Chromosome other) {
        if (!(other instanceof CombinationChromosome)) {
            return false;
        }

        CombinationChromosome downcast = (CombinationChromosome) other;

        return genes.length == other.length && r == downcast.r;
    }

    @Override
    public Chromosome duplicate() {
        return new CombinationChromosome(genes.clone(), r);
    }
}
