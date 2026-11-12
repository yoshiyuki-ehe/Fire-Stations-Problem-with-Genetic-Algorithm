import java.util.Random;
public class MutationSwapGenes extends Mutation {
    @Override
    public void mutate(Chromosome chromosome) {
        FireStationsSingleton singleton = FireStationsSingleton.INSTANCE;
        Random rand = singleton.getRandom();

        if (chromosome.length == 1) {
            return;
        }

        //pilih 2 index secara acak
        int index1 = rand.nextInt(chromosome.length);
        int index2 = rand.nextInt(chromosome.length);

        //2 index yang dipilih tidak boleh sama
        while (index2 == index1) {
            index2 = rand.nextInt(chromosome.length);
        }

        //swap posisi 2 gen tersebut
        int temp = chromosome.getGene(index1);
        chromosome.setGene(index1, chromosome.getGene(index2));
        chromosome.setGene(index2, temp);
    }
}