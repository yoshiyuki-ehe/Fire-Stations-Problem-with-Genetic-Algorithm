public abstract class Chromosome {
    public abstract void selfRepair();
    public abstract boolean crossable(Chromosome other);
    public abstract Chromosome duplicate();
    public abstract int length();

    //Chromosome A (n x m)
    public static class ChromosomeA extends Chromosome {
        private int[][] genes;
        private int r;

        //constructor dengan r
        public ChromosomeA(int[][] genes, int r) {
            this.genes = new int[genes.length][genes[0].length];
            this.r = r;

            for (int i = 0; i < genes.length; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        //constructor without r
        public ChromosomeA(int[][] genes) {
            this(genes, countOnes(genes));
        }

        //menghitung jumlah angka 1 dalam genes
        private static int countOnes(int[][] genes) {
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

        //mengambil salinan genes
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

        public int getR() {
            return r;
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
        public void selfRepair() {
            int count = 0;
            int target = Math.max(0, Math.min(r, length()));

            //memastikan hanya ada angka 0 dan 1
            for (int i = 0; i < genes.length; i++) {
                for (int j = 0; j < genes[i].length; j++) {
                    genes[i][j] = genes[i][j] == 1 ? 1 : 0;

                    if (genes[i][j] == 1) {
                        count++;
                    }
                }
            }

            //mengurangi jumlah angka 1 jika melebihi r
            if (count > target) {
                for (int i = genes.length - 1; i >= 0 && count > target; i--) {
                    for (int j = genes[i].length - 1; j >= 0 && count > target; j--) {
                        if (genes[i][j] == 1) {
                            genes[i][j] = 0;
                            count--;
                        }
                    }
                }
            }
            //menambah jumlah angka 1 jika kurang dari r
            else if (count < target) {
                for (int i = 0; i < genes.length && count < target; i++) {
                    for (int j = 0; j < genes[i].length && count < target; j++) {
                        if (genes[i][j] == 0) {
                            genes[i][j] = 1;
                            count++;
                        }
                    }
                }
            }
        }

        @Override
        //cek apakah kedua parent bisa dicross
        public boolean crossable(Chromosome other) {
            if (!(other instanceof ChromosomeA)) {
                return false;
            }

            ChromosomeA chromosome = (ChromosomeA) other;

            return getRows() == chromosome.getRows()
                && getCols() == chromosome.getCols()
                && r == chromosome.r;
        }

        @Override
        //mengambil salinan ChromosomeA
        public ChromosomeA duplicate() {
            return new ChromosomeA(getGenes(), r);
        }

        @Override
        //mengambil panjang dari ChromosomeA
        public int length() {
            return getRows() * getCols();
        }
    }

    //Chromosome B (2 x n)
    public static class ChromosomeB extends Chromosome {
        private int[][] genes;
        private int maxFirst;
        private int maxSecond;

        //constructor dengan maxFirst dan maxSecond
        public ChromosomeB(int[][] genes, int maxFirst, int maxSecond) {
            this.genes = new int[2][genes[0].length];
            this.maxFirst = Math.max(1, maxFirst);
            this.maxSecond = Math.max(1, maxSecond);

            for (int i = 0; i < 2; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        //constructor without maxFirst and maxSecond
        public ChromosomeB(int[][] genes) {
            this(genes, findMax(genes[0]) + 1, findMax(genes[1]) + 1);
        }

        //mencari nilai maksimum dalam array
        private static int findMax(int[] values) {
            int max = 0;

            for (int value : values) {
                if (value > max) {
                    max = value;
                }
            }

            return max;
        }

        //mengambil salinan genes
        public int[][] getGenes() {
            int[][] copy = new int[2][genes[0].length];

            for (int i = 0; i < 2; i++) {
                copy[i] = genes[i].clone();
            }

            return copy;
        }

        public int getMaxFirst() {
            return maxFirst;
        }

        public int getMaxSecond() {
            return maxSecond;
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

        //mengambil koordinat x dan y dari station
        public int getX(int index) {
            return genes[0][index];
        }

        public int getY(int index) {
            return genes[1][index];
        }

        //mengubah koordinat x dan y dari station
        public void setStation(int index, int x, int y) {
            genes[0][index] = x;
            genes[1][index] = y;
        }

        public int getStationCount() {
            return genes[0].length;
        }

        @Override
        public void selfRepair() {
            for (int i = 0; i < length(); i++) {
                //memastikan koordinat pertama berada dalam batas
                genes[0][i] = Math.max(0, Math.min(genes[0][i], maxFirst - 1));

                //memastikan koordinat kedua berada dalam batas
                genes[1][i] = Math.max(0, Math.min(genes[1][i], maxSecond - 1));
            }
        }

        @Override
        //cek apakah kedua parent bisa dicross
        public boolean crossable(Chromosome other) {
            if (!(other instanceof ChromosomeB)) {
                return false;
            }

            ChromosomeB chromosome = (ChromosomeB) other;

            return length() == chromosome.length()
                && maxFirst == chromosome.maxFirst
                && maxSecond == chromosome.maxSecond;
        }

        @Override
        //mengambil salinan ChromosomeB
        public ChromosomeB duplicate() {
            return new ChromosomeB(getGenes(), maxFirst, maxSecond);
        }

        @Override
        public int length() {
            return getStationCount();
        }
    }
}
