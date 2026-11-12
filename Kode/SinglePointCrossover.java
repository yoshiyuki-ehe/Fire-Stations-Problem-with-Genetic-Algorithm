import java.util.Random;

public class SinglePointCrossover extends Crossover {
    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        //cek apakah kedua parent bisa dicross
        if (!parent1.crossable(parent2)) {
            return new Chromosome[0];
        }

        int totalGenes = parent1.length;

        //buat anak dari salinan parent
        Chromosome child1 = parent1.duplicate();
        Chromosome child2 = parent2.duplicate();

        //mengambil random dari singleton
        Random random = FireStationsSingleton.INSTANCE.getRandom();

        //single-point crossover
        int point = random.nextInt(totalGenes - 1) + 1;

        for (int i = point; i < totalGenes; i++) {
            int gene1 = parent1.getGene(i);
            int gene2 = parent2.getGene(i);

            child1.setGene(i, gene2);
            child2.setGene(i, gene1);
        }

        child1.selfRepair();
        child2.selfRepair();

        return new Chromosome[]{child1, child2};
    }
}
