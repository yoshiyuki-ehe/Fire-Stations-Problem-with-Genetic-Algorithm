import java.util.Random;

public class ParentSelectionByFitness extends ParentSelection{
    
    @Override
    public Individual[] selectParents(Population population, int nOfParents)
    {
        Random random = FireStationsSingleton.INSTANCE.getRandom();

        boolean[] selectedCandidates = new boolean[population.getPopulationSize()];
        double totalFitness = population.getTotalFitness();

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];

        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++) 
        {    
            //ambil nilai sembarang roulleteResult dari 0 sampai total nilai fitness.
            double roulleteResult = random.nextDouble(0, totalFitness);
            
            //for berikut mengambil sebuah kandidat acak untuk dimasukkan ke selectedParents.
            for (int j = 0; j < population.getTotalFitness(); j++) {

                //ganti kandidat setiap sisa nilai roulleteResult - nilai fitness dari kandidat masih bersisa.
                Individual curCandidate = population.getIndividual(j);
                roulleteResult -= curCandidate.getFitness();

                //kalau nilai roulleteResult sudah habis, anggap seperti wheel sudah berhenti berputar.
                if (roulleteResult <= 0 ){

                    //kalau curCandidate belum terpilih,
                    if (!selectedCandidates[j]) {

                        //ambil kandidat menjadi salah satu parent terpilih dan hilangkan dari sisa kandidat (dengan ditandai bahwa sudah terpilih).
                        selectedParents[i] = curCandidate;
                        selectedCandidates[j] = true;
                        break;
                    
                    }else{
                        //ulangi cycle pemilihan, curCandidate tidak valid.
                        i--;
                        break;
                    }



                }
            }
        }

        return selectedParents;
    }
}
