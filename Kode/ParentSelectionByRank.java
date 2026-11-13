import java.util.Random;

public class ParentSelectionByRank extends ParentSelection{
    @Override
    public Individual[] selectParents(Population population, int nOfParents)
    {
        Random random = FireStationsSingleton.INSTANCE.getRandom();

        //masukkan population yang telah disort sebagai kandidat parent
        boolean[] selectedCandidates = new boolean[population.getPopulationSize()];
        int totalRank = termial(population.getPopulationSize());

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];
        
        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++) 
        {    
            //ambil nilai sembarang roulleteResult dari 1 sampai total nilai fitness.
            int roulleteResult = random.nextInt(1, totalRank+1);
            
            //untuk mengambil sebuah kandidat acak untuk dimasukkan ke selectedParents, 
            //pakai inverse function termial untuk mendapatkan index dari nilai (index diambil dari belakang)
            int selectedParentIndex = population.getPopulationSize() - inverseTermial(roulleteResult) - 1;
            //kalau kandidat belum pernah terpilih
            if (!selectedCandidates[selectedParentIndex]) {

                //kandidat valid menjadi parent, maka masukkan sebagai selected parent.
                selectedParents[i] = population.getIndividual(selectedParentIndex);
                selectedCandidates[selectedParentIndex] = true;

            }else{
                //ulangi cycle karena kandidat yang terpilih tidak valid.
                i--;
            }
        }

        return selectedParents;
    }

    private int termial(int n){
        return n * (n + 1) / 2;
    }

    // perhitungan invers termial dengan kompleksitas O(1)
    private int inverseTermial(int y){
        // rumus mencari nilai x pada fungsi kuadrat
        // 0 = ax^2 + bx + c
        // x = (-1 +- sqrt(b^2 - 4ac)) / 2a
        // Termial(n) = n * (n + 1) / 2
        // 2 * Termial(n) = n^2 + n
        // 0 = n^2 + n - 2 * Termial(n)
        // nilai invers yang dibutuhkan merupakan nilai positif
        // n = (-1 + sqrt(1 - 4 * 2 * Termial(n))) / 2

        return (-1 + (int)(Math.sqrt(1 - 4 * 2 * y))) / 2;
        //some study reference used into the implementation below and to understand the possible risks of it:
        //https://stackoverflow.com/questions/1348892/why-does-math-floordouble-return-a-value-of-type-double
    }
}