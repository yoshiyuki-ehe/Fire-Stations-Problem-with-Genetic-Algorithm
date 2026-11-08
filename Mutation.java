import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Kelas utama (public)
public abstract class Mutation {
    /**
     * Memutasi kromosom dan mengembalikan array kromosom baru hasil mutasi.
     * 
     * @param chromosome Array kromosom asal
     * @return Array kromosom baru (mutated)
     */
    public abstract int[] mutate(int[] chromosome);
}

//encodinb 1: array koordinat (2p)
class CoordinateRelocationMutation extends Mutation {

    @Override
    public int[] mutate(int[] chromosome) {
        FireStationsSingleton singleton = FireStationsSingleton.INSTANCE;
        Environment env = singleton.getEnvironment();
        Random rand = singleton.getRandom();
        double mutationRate = singleton.getMutationRate();

        int mutated[] = chromosome.clone();

        //Cek probabilitas mutasi
        if (rand.nextDouble() >= mutationRate) {
            return mutated;
        }

        int numberFireStations = mutated.length / 2; //di individual di deklarasi dengan k
        if (numberFireStations == 0){
            return mutated;  
        } 

        int n = env.mapHeight; //height tinggi
        int m = env.mapWidth; //width lebar

        //buat tau lokasi pemadam kebakarannya ada dimana aja, supaya nanti engga nimpa di tempat pemadam kebakaran yang sama
        boolean occupied[][] = new boolean[n][m];
        for (int i = 0; i < numberFireStations; i++) {
            int r = mutated[2 * i];      //row baris
            int c = mutated[2 * i + 1];  //column kolom
            if (r >= 0 && r < n && c >= 0 && c < m) {
                occupied[r][c] = true;
            }
        }

        //ini buat nyari slot yang kosong engga ada apa apa di kotak itu
        List<int[]> validEmptyCells = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                if (env.isFree(r, c) && !occupied[r][c]) {
                    validEmptyCells.add(new int[]{r, c});
                }
            }
        }

        //Pindahkan 1 stasiun acak ke petak kosong acak
        if (!validEmptyCells.isEmpty()) {
            int selectedStation = rand.nextInt(numberFireStations);
            int[] newPos = validEmptyCells.get(rand.nextInt(validEmptyCells.size()));

            mutated[2 * selectedStation] = newPos[0];     // Baris baru
            mutated[2 * selectedStation + 1] = newPos[1]; // Kolom baru
        }

        return mutated;
    }
}


//encoding 2: bitstring (n x m)
class BitstringRelocationMutation extends Mutation {

    @Override
    public int[] mutate(int[] chromosome) {
        FireStationsSingleton singleton = FireStationsSingleton.INSTANCE;
        Environment env = singleton.getEnvironment();
        Random rand = singleton.getRandom();
        double mutationRate = singleton.getMutationRate();

        int[] mutated = chromosome.clone();

        //Cek probabilitas mutasi
        if (rand.nextDouble() >= mutationRate) {
            return mutated;
        }

        int m = env.mapWidth;
        List<Integer> stationIndices = new ArrayList<>();
        List<Integer> validEmptyIndices = new ArrayList<>();

        //buat nyari slot yang kosong 
        for (int i = 0; i < mutated.length; i++) {
            if (mutated[i] == 1) {
                stationIndices.add(i);
            } 
            else {
                int r = i / m;
                int c = i % m;
                //Hanya masukkan petak kalo kosong engga ada rumah atau pohon
                if (env.isFree(r, c)) {
                    validEmptyIndices.add(i);
                }
            }
        }

        //Tukar posisi 1 stasiun ke posisi 0 yang valid
        if (!stationIndices.isEmpty() && !validEmptyIndices.isEmpty()) {
            int fromIdx = stationIndices.get(rand.nextInt(stationIndices.size()));
            int toIdx = validEmptyIndices.get(rand.nextInt(validEmptyIndices.size()));

            mutated[fromIdx] = 0; //Hapus dari lokasi lama
            mutated[toIdx] = 1;   //Pindahkan ke lokasi baru
        }

        return mutated;
    }
}