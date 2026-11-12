public class CrossoverB extends Crossover {
    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        //cek apakah kedua parent bisa dicross
        if (!parent1.crossable(parent2)) {
            return new Chromosome[0];
        }

        //buat anak dari salinan parent
        Chromosome child1 = parent1.duplicate();
        Chromosome child2 = parent2.duplicate();

        //arithmetic average crossover
        for (int i = 0; i < parent1.length; i++) {
            int gene1 = parent1.getGene(i);
            int gene2 = parent2.getGene(i);

            int averageGene = (gene1 + gene2 + 1) / 2;

            child1.setGene(i, averageGene);
            child2.setGene(i, averageGene);
        }

        child1.selfRepair();
        child2.selfRepair();

        return new Chromosome[]{child1, child2};
    }
}
