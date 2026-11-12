import java.util.Random;

public class SinglePointCrossover implements Crossover {
    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        //cek apakah kedua parent bisa dicross
        //kalau tidak jangan lakukan Crossover 
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
        int point = random.nextInt(totalGenes);
        int gene1, gene2;

        // mulai dari single-point yang telah dipilih
        // memberikan gen parent2 ke child1 dan
        // memberikan gen parent1 ke child2
        for (int i = point; i < totalGenes; i++) {
            gene1 = parent1.getGene(i);
            gene2 = parent2.getGene(i);

            child1.setGene(i, gene2);
            child2.setGene(i, gene1);
        }

        // crossover dapat menciptakan Chromosome yang tidak valid, sehingga harus direpair
        child1.selfRepair();
        child2.selfRepair();

        return new Chromosome[]{child1, child2};
    }
}
