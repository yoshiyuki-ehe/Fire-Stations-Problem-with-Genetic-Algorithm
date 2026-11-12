import java.util.Random;

public abstract class Crossover {
    public abstract Chromosome[] crossover(Chromosome parent1, Chromosome parent2);

    //single-Point Crossover (ChromosomeA)
    public static class CrossoverA extends Crossover {
        @Override
        public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
            if (!(parent1 instanceof Chromosome.ChromosomeA) || !(parent2 instanceof Chromosome.ChromosomeA)) {
                return new Chromosome[0];
            }

            Chromosome.ChromosomeA p1 = (Chromosome.ChromosomeA) parent1;
            Chromosome.ChromosomeA p2 = (Chromosome.ChromosomeA) parent2;

            //cek apakah kedua parent bisa dicross
            if (!p1.crossable(p2)) {
                return new Chromosome[0];
            }

            int totalGenes = p1.length();

            //buat anak dari salinan parent
            Chromosome.ChromosomeA child1 = p1.duplicate();
            Chromosome.ChromosomeA child2 = p2.duplicate();

            //mengambil random dari singleton
            Random random = FireStationsSingleton.INSTANCE.getRandom();

            //single-point crossover
            if (totalGenes > 1) {
                int point = random.nextInt(totalGenes - 1) + 1;

                for (int i = point; i < totalGenes; i++) {
                    int gene1 = p1.getGene(i);
                    int gene2 = p2.getGene(i);

                    child1.setGene(i, gene2);
                    child2.setGene(i, gene1);
                }
            }

            child1.selfRepair();
            child2.selfRepair();

            return new Chromosome[]{child1, child2};
        }
    }

    //Arithmetic Average Crossover (ChromosomeB)
    public static class CrossoverB extends Crossover {
        @Override
        public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
            if (!(parent1 instanceof Chromosome.ChromosomeB) || !(parent2 instanceof Chromosome.ChromosomeB)) {
                return new Chromosome[0];
            }

            Chromosome.ChromosomeB p1 = (Chromosome.ChromosomeB) parent1;
            Chromosome.ChromosomeB p2 = (Chromosome.ChromosomeB) parent2;

            //cek apakah kedua parent bisa dicross
            if (!p1.crossable(p2)) {
                return new Chromosome[0];
            }

            //buat anak dari salinan parent
            Chromosome.ChromosomeB child1 = p1.duplicate();
            Chromosome.ChromosomeB child2 = p2.duplicate();

            //arithmetic average crossover
            for (int i = 0; i < p1.length(); i++) {
                int[] gene1 = p1.getGene(i);
                int[] gene2 = p2.getGene(i);

                int firstAverage = (int) Math.round((gene1[0] + gene2[0]) / 2.0);
                int secondAverage = (int) Math.round((gene1[1] + gene2[1]) / 2.0);

                int[] averageGene = {firstAverage, secondAverage};

                child1.setGene(i, averageGene);
                child2.setGene(i, averageGene);
            }

            child1.selfRepair();
            child2.selfRepair();

            return new Chromosome[]{child1, child2};
        }
    }
}
