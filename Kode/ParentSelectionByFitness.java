import java.util.Random;

// Roulette Wheel proportional by Fitness
public class ParentSelectionByFitness extends ParentSelection{
    
    @Override
    public Individual[] selectParents(Population population, int nOfParents)
    {
        Random random = FireStationsSingleton.INSTANCE.getRandom();

        double totalFitness = population.getFitnessPrefixSum(population.getPopulationSize());

        //array hasil parent-parent terpilih (yang di-return)
        Individual[] selectedParents = new Individual[nOfParents];

        //ambil parent sebanyak nOfParents
        for (int i = 0; i < nOfParents; i++)
        {    
            //ambil nilai sembarang roulleteResult dari 0 sampai total nilai fitness.
            double rouletteResult = random.nextDouble(0, totalFitness);
            int selectedParentIndex = searchRouletteIndex(population, rouletteResult);
            selectedParents[i] = population.getIndividual(selectedParentIndex);
        }

        return selectedParents;
    }

    private int searchRouletteIndex(Population population, double rouletteResult) {
        int left = 0;
        int right = population.getPopulationSize() - 1;
        int mid = (left + right) / 2;

        // interval ke-i dinyatakan sebagai (prefixSum[i - 1], prefixSum[i]] dengan interval ke-0 (0, prefixSum[i]]
        // kita ingin mengetahui rouletteResult jatuh di interval mana
        // kita dapat melakukan hal tersebut dengan mencari nilai terendah yang tidak kurang dari rouletteResult dengan Bisection
        while (left < right)
        {
            mid = (left + right) / 2;
            // interval rouletteResult ada di kanan batas kanan interval yang diperiksa sekarang (tidak mungkin berada di dalamnya)
            //           mid
            // | interval | rouletteResult |
            if (population.getFitnessPrefixSum(mid) < rouletteResult) left = mid + 1;
            // jika tidak rouletteResult berada di kiri batas kanan interval yang diperiksa sekarang (bisa saja berada di dalamnya)
            // atau bahkan bisa berada di batas kanan interval yang diperiksa sekarang jika fitnessPrefixSum(mid) == rouletteResult
            //                 mid
            // | rouletteResult |
            //                            mid
            // | rouletteResult | interval |
            //           mid
            //      rouletteResult
            // | interval | interval |
            else mid = right;
        }

        return left;
    }
}
