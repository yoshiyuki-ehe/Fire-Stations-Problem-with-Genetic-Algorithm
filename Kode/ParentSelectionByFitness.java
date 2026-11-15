//reference ada tertulis pada kode.
import java.util.Random;


public class ParentSelectionByFitness extends ParentSelection{
    
    @Override
    public Individual[] selectParents(Population population, Random random, int nOfParents)
    {
        //LinkedList<Individual> unselectedCandidates = linkedListConvert(population.getArray());

        boolean[] selectedCandidates = new boolean[population.getPopulationSize()];
        double totalFitness = population.getTotalFitness();

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];

        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++) 
        {    
            //ambil nilai sembarang roulleteResult dari 0 sampai total nilai fitness.
            double roulleteResult = random.nextDouble(0, totalFitness);
            
            //"panah wheel berhenti" pada curCandidate
            int curCandidateIndex = this.searchRoulleteIndex(population.getPrefixSumFitness(), roulleteResult);
            Individual curCandidate = population.getIndividual(curCandidateIndex);

            //kalau curCandidate belum terpilih,
            if (!selectedCandidates[curCandidateIndex]) {

                //ambil kandidat menjadi salah satu parent terpilih dan hilangkan dari sisa kandidat (dengan ditandai bahwa sudah terpilih).
                selectedParents[i] = curCandidate;
                selectedCandidates[curCandidateIndex] = true;
                    
            }else{
                //ulangi cycle pemilihan, curCandidate tidak valid.
                i--;
            }
        }

        return selectedParents;
    }

    //referensi: https://www.geeksforgeeks.org/dsa/program-for-bisection-method/ ,
    //https://www.geeksforgeeks.org/dsa/binary-search/
    private int searchRoulleteIndex(double[] prefixSumFitnessArray, double roulleteResult){
        
        int left = 0;
        int right = prefixSumFitnessArray.length-1;
        int mid = (left+right)/2;

        //kalau value di antara 2 elemen array yang saling bersebelahan (lantas, tidak ada elemen array lagi di antaranya), berarti indexnya left (yang fitnessnya lebih kecil (yang habis dikurangi roulleteResult))
        while (right-left == 1){
            if(roulleteResult < prefixSumFitnessArray[mid])
                right = mid;
            else
                left = mid;
        }
        return left;
    }
}

// buat random.nextDouble(0, totalFitness)
// buat boolean[] selectedCandidates untuk menandai kandidat yang sudah diselect, kalau populationnya besar lebih efisien daripada menghapus dari List
