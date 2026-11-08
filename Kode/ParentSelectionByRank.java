import java.util.Random;

public class ParentSelectionByRank extends ParentSelection{
    //karena gw ga tau bentukan Population, method di bawah masih harus direfactor.
    @Override
    public Individual[] selectParents(Population population, Random random, int nOfParents)
    {
        //mengambil asumsi Population Array sudah disort, di method ini tidak perlu disort lagi. (Kredit: other team member)
        Individual[] sortedPopulation = population.getArray();
        //Arrays.sort(sortedPopulation);
        
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
                selectedParents[i] = sortedPopulation[selectedParentIndex];
                selectedCandidates[selectedParentIndex] = true;

            }else{
                //ulangi cycle karena kandidat yang terpilih tidak valid.
                i--;
            }


        }

        return selectedParents;
    }

    //Credit to another team member: ganti nama method supaya sesuai istilah benarnya dan mengubah complexity dari O(n) ke O(1)
    private int termial(int n){
        return n*(n+1)/2;
    }

    private int inverseTermial(int y){
        //some study reference used into the implementation below and to understand the possible risks of it:
        //https://stackoverflow.com/questions/1348892/why-does-math-floordouble-return-a-value-of-type-double
        return (-1 + (int)(Math.sqrt(1 - 4 * 2 * y))) / 2;
    }
}

// remove sort karena dilakukan sendiri oleh Population
// memilih angka dari (1, Termial(n) + 1)
// pilih kandidat dari inverse function Termial
// f(n) = (n * (n + 1) / 2)
// f(n) = (n^2 + n) / 2
// f(n) = (n/2)^2 + n/2
// 0 = (n/2)^2 + n/2 + f(n)
// 0 = n^2 + n + 2*f(n)
// n = (-1 + sqrt(1 - 4 * 2 * f(n))) / 2
// source: https://www.geeksforgeeks.org/maths/x-intercepts-of-a-quadratic-fuction/
// (population size - n - 1) jadi indeks winner candidate
