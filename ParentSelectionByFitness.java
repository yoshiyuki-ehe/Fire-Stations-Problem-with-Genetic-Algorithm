import java.util.LinkedList;
import java.util.Random;

public class ParentSelectionByFitness extends ParentSelection{
    //karena gw ga tau bentukan Population, method di bawah masih harus direfactor.
    @Override
    public Individual[] selectParents(Population population, Random random, int nOfParents)
    {
        //dibuat method linkedListConvert (array --> LinkedList) karena tidak ditemukan librarynya.
        //kalau ada cara yang lebih baik / tidak memakai LinkedList silahkan diganti.
        LinkedList<Individual> unselectedCandidates = linkedListConvert(population.getArray());
        double remainingTotalFitness = population.getTotalFitness();

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];

        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++) 
        {    
            //ambil nilai sembarang roulleteResult dari 1 sampai total nilai fitness.
            double roulleteResult = random.nextDouble(1, remainingTotalFitness+1);
            
            //for berikut mengambil sebuah kandidat acak untuk dimasukkan ke selectedParents.
            for (int j = 0; j < unselectedCandidates.size(); j++) {

                //ganti kandidat setiap sisa nilai roulleteResult - nilai fitness dari kandidat masih bersisa.
                Individual curCandidate = unselectedCandidates.get(j);
                roulleteResult -= curCandidate.getFitness();

                //kalau nilai roulleteResult sudah habis, anggap seperti wheel sudah berhenti berputar.
                if (roulleteResult <= 0){
                    
                    //ambil kandidat menjadi salah satu parent terpilih dan hilangkan dari sisa kandidat.
                    selectedParents[i] = curCandidate;
                    remainingTotalFitness -= curCandidate.getFitness();
                    unselectedCandidates.remove(j);
                    
                    break;
                }
            }
        }

        return selectedParents;
    }

    private LinkedList<Individual> linkedListConvert(Individual[] array){
        LinkedList<Individual> ll = new LinkedList<>();
        for (Individual individual : array) {
            ll.offer(individual);
        }
        return ll;
    }
}