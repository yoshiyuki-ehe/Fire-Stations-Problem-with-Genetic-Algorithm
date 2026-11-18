Cara menjalankan Eksperimen:
1. Masukkan nilai nilai berikut ke SpecificHyperparameters.txt:
100
0.8
0.05
0.2
2. Masukkan semua Maps yang akan dipakai untuk tahap 1 ke dalam folder Maps.
3. Masukkan general seed, number of seeds per test case, dan minimum relative change (untuk menghitung stagnasi) ke dalam OtherParameters.txt.
4. Jalankan TestAllModels.sh.
5. Masukkan semua Maps yang akan dipakai untuk tahap 2 ke dalam folder Maps.
6. Lakukan langkah 7-11 3 kali, masing masing untuk 3 Model terbaik dari langkah 3.
7. Masukkan kode model yang ingin diuji ke SpecificModel.txt.
8. Masukkan set Hyperparameter awal ke CombinationHyperparameters.txt.
9. Jalankan TestHyperparameterTuning.sh.
10. Masukkan 2 Population Size, 2 Crossover Rate, 1 Mutation Rate, dan 1 Elitism Rate terbaik dari BestParameterPerRun.txt.
11. Jalankan TestAllModels.sh.