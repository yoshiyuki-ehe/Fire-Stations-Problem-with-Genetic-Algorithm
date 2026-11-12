import java.util.Random;
public class MutationIncrementGene extends Mutation {
    @Override
    public void mutate(Chromosome chromosome) {
        FireStationsSingleton singleton = FireStationsSingleton.INSTANCE;
        Random rand = singleton.getRandom();

        //pilih 1 index secara acak
        int randomIndex = rand.nextInt(chromosome.length);
        //menghasilkan nilai biner 0 dan 1
        int randomIncrement = rand.nextInt(2); 

        //harus memastikan mutasi benar-benar terjadi
        //pastikan nilai increment mutasi -1 atau 1, tidak boleh 0
        //jika nilai randomIncrement 0, gen didecrement
        //jika nilai randomIncrement 1, gen diincrement   
        chromosome.setGene(randomIndex, chromosome.getGene(randomIndex) - 1 + 2 * randomIncrement);
    }
}