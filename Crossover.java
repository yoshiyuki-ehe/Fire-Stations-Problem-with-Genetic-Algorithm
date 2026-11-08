import java.util.Random;

public class Crossover {
    private static Random random = new Random();

    //single-Point Crossover (ChromosomeA)
    public static Chromosome.ChromosomeA[] crossoverA(Chromosome.ChromosomeA parent1, Chromosome.ChromosomeA parent2) {
        int rows = parent1.getRows();
        int cols = parent1.getCols();
        int totalGenes = rows * cols; 

        //array untuk simpan genes anak
        int[][] genes1 = new int[rows][cols];
        int[][] genes2 = new int[rows][cols];

        //random crossover point
        int point = random.nextInt(totalGenes - 1) + 1;

        //crossover
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;

                if (index < point) {
                    genes1[i][j] = parent1.getGene(index);
                    genes2[i][j] = parent2.getGene(index);
                }
                else {
                    genes1[i][j] = parent2.getGene(index);
                    genes2[i][j] = parent1.getGene(index);
                }
            }
        }

        //buat anak baru, dijadikan ChromosomeA
        Chromosome.ChromosomeA child1 = new Chromosome.ChromosomeA(genes1);
        Chromosome.ChromosomeA child2 = new Chromosome.ChromosomeA(genes2);

        return new Chromosome.ChromosomeA[]{child1, child2};
    }

    //uniform Crossover (ChromosomeB)
    public static Chromosome.ChromosomeB[] crossoverB(Chromosome.ChromosomeB parent1, Chromosome.ChromosomeB parent2) {
        int stations = parent1.getStationCount();

        //array untuk simpan genes anak
        int[][] genes1 = new int[2][stations];
        int[][] genes2 = new int[2][stations];

        //uniform crossover berdasarkan p
        for (int j = 0; j < stations; j++) {
            //random parent
            boolean chooseParent1 = random.nextBoolean();

            //ambil gene dari parent yg dipilih
            if (chooseParent1) {
                genes1[0][j] = parent1.getGene(j)[0];
                genes1[1][j] = parent1.getGene(j)[1];

                genes2[0][j] = parent2.getGene(j)[0];
                genes2[1][j] = parent2.getGene(j)[1];
            }
            else {
                genes1[0][j] = parent2.getGene(j)[0];
                genes1[1][j] = parent2.getGene(j)[1];

                genes2[0][j] = parent1.getGene(j)[0];
                genes2[1][j] = parent1.getGene(j)[1];
            }
        }

        //buat anak baru, dijadikan ChromosomeB
        Chromosome.ChromosomeB child1 = new Chromosome.ChromosomeB(genes1);
        Chromosome.ChromosomeB child2 = new Chromosome.ChromosomeB(genes2);

        return new Chromosome.ChromosomeB[]{child1, child2};
    }
}

// buat abstract class Crossover
// method crossover returns Chromosome

// Random jangan buat new Random(), ambil dari Singleton
// crossover hanya boleh memanggil getGene() dan setGene()

// buat crossoverA singlepoint crossover
// buat crossoverB arithmetic average crossover
// ilustrasi ada di dokumen LucidChart kita
