public abstract class Chromosome {
    //ambil jumlah fire station
    public abstract int getFireStationCount();

    //Chromosome A (n x m)
    public static class ChromosomeA extends Chromosome {
        private int[][] genes;

        public ChromosomeA(int[][] genes) {
            this.genes = new int[genes.length][genes[0].length];

            for (int i = 0; i < genes.length; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        public int[][] getGenes() {
            int[][] copy = new int[genes.length][genes[0].length];

            for (int i = 0; i < genes.length; i++) {
                copy[i] = genes[i].clone();
            }

            return copy;
        }

        public int getRows() {
            return genes.length;
        }

        public int getCols() {
            return genes[0].length;
        }

        //mengambil gene berdasarkan index
        public int getGene(int index) {
            int row = index / getCols();
            int col = index % getCols();

            return genes[row][col];
        }

        //mengubah gene berdasarkan index
        public void setGene(int index, int value) {
            int row = index / getCols();
            int col = index % getCols();

            genes[row][col] = value;
        }

        @Override
        public int getFireStationCount() {
            int count = 0;

            for (int i = 0; i < genes.length; i++) {
                for (int j = 0; j < genes[i].length; j++) {
                    if (genes[i][j] == 1) {
                        count++;
                    }
                }
            }

            return count;
        }
    }

    //Chromosome B (2 x p)
    public static class ChromosomeB extends Chromosome {
        private int[][] genes;

        public ChromosomeB(int[][] genes) {
            this.genes = new int[2][genes[0].length];

            for (int i = 0; i < 2; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        public int[][] getGenes() {
            int[][] copy = new int[2][genes[0].length];

            for (int i = 0; i < 2; i++) {
                copy[i] = genes[i].clone();
            }

            return copy;
        }

        //mengambil gene 
        public int[] getGene(int index) {
            return new int[] {
                genes[0][index],
                genes[1][index]
            };
        }

        //mengubah gene
        public void setGene(int index, int[] value) {
            genes[0][index] = value[0];
            genes[1][index] = value[1];
        }

        public int getX(int index) {
            return genes[0][index];
        }

        public int getY(int index) {
            return genes[1][index];
        }

        public void setStation(int index, int x, int y) {
            genes[0][index] = x;
            genes[1][index] = y;
        }

        public int getStationCount() {
            return genes[0].length;
        }

        @Override
        public int getFireStationCount() {
            return getStationCount();
        }
    }
}

// tambahkan abstract void selfRepair()
// tambahkan abstract boolean crossable(Chromosome other)
// tambahkan abstract Chromosome duplicate()
// tambahkan abstract int length()
// remove method getFireStationCount()

// Buat Chromosome A menjadi Bitstring Combination (Kombinasi disimpan dalam Bitstring)
// harus memiliki atribut r
// selfRepair() memastikan:
//    hanya ada 1 dan 0 di dalam array
//    ada tepat r angka 1 dalam array

// Buat Chromosome B menjadi Pair Array matriks (2 x n)
// harus memiliki atribut maxFirst, maxSecond
// selfRepair() memastikan semua elemen arr[0][i] < maxFirst dan semua elemen arr[i][1] < maxSecond
