public class ArithmeticAverageCrossover implements Crossover {
    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        //cek apakah kedua parent dapat dicross
        if (!parent1.crossable(parent2)) {
            return new Chromosome[0];
        }

        //buat anak dari salinan parent
        Chromosome child = parent1.duplicate();
        int gene1, gene2;

        //arithmetic average crossover
        for (int i = 0; i < parent1.length; i++) {
            gene1 = parent1.getGene(i);
            gene2 = parent2.getGene(i);

            // gene1 dan gene2 bilangan bulat
            // sifat pembagian integer membulatkan ke bawah
            // jika gene1 + gene2 ganjil seharusnya dibulatkan ke satuan terdekat (ke atas)
            // maka ditambah 1 terlebih dahulu
            // jika gene1 + gene2 genap, maka (gene1 + gene2 + 1) / 2 akan dibulatkan ke bawah
            int averageGene = (gene1 + gene2 + 1) / 2;

            child.setGene(i, averageGene);
        }
        
        child.selfRepair();

        return new Chromosome[]{child};
    }
}
