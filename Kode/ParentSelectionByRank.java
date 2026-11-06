import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class ParentSelectionByRank extends ParentSelection{
    //karena gw ga tau bentukan Population, method di bawah masih harus direfactor.
    @Override
    //TODO: ini masih select parents yang by fitness, harus beneran diganti ke by rank.
    public Individual[] selectParents(Population population, Random random, int nOfParents)
    {
        //sort terlebih dahulu population berdasarkan fitnessnya, ascending
        Individual[] sortedPopulation = population.getArray().clone();
        Arrays.sort(sortedPopulation);

        //masukkan population yang telah disort sebagai kandidat parent
        LinkedList<Individual> unselectedCandidates = linkedListConvert(sortedPopulation);
        double remainingTotalRank = plustorial(population.getPopulationSize());

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];
        
        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++) 
        {    
            //ambil nilai sembarang roulleteResult dari 1 sampai total nilai fitness.
            double roulleteResult = random.nextDouble(1, remainingTotalRank+1);
            
            //for berikut mengambil sebuah kandidat acak untuk dimasukkan ke selectedParents.
            for (int j = 1; j < unselectedCandidates.size()-1; j++) {

                //ganti kandidat setiap sisa nilai roulleteResult - nilai fitness dari kandidat masih bersisa.
                Individual curCandidate = unselectedCandidates.get(j);
                roulleteResult -= j;

                //kalau nilai roulleteResult sudah habis, anggap seperti wheel sudah berhenti berputar.
                if (roulleteResult <= 0){
                    
                    //ambil kandidat menjadi salah satu parent terpilih dan hilangkan dari sisa kandidat.
                    selectedParents[i] = curCandidate;
                    remainingTotalRank -= j;
                    unselectedCandidates.remove(j);
                    
                    break;
                }
            }
        }

        return selectedParents;
    }

    private <T> LinkedList<T> linkedListConvert(T[] array){
        LinkedList<T> ll = new LinkedList<>();
        for (T individual : array) {
            ll.offer(individual);
        }
        return ll;
    }

    //TODO: ada yang tau nama faktorial yang dijumlahkan bukan dikalikan? Yang tahu mohon refactor.
    private int plustorial(int n){
        if (n > 1) {
            return plustorial(n-1)+n;    
        }
        return 1;
    }
}