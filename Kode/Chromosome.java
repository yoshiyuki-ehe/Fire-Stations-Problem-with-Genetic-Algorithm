public abstract class Chromosome {
    public abstract void selfRepair();
    public abstract boolean crossable(Chromosome other);
    public abstract Chromosome duplicate();
    public abstract int length();

    public final int length;
    
    // length simpan sebagai final int di Chromosome
    // pastikan kelas ekstensi memanggil super(int length)
    public Chromosome(int length) {
        this.length = length;
    }
    
    // btw ini gw ralat sedikit: gw bilang n * m maksudnya array 1 dimensi yang panjangnya (n * m)
    // tapi n * m nya dihitungnya di luar, bukan di dalam
    
    //Chromosome A (n x m)
    // ChromosomeA bikin di file yang berbeda
    public static class ChromosomeA extends Chromosome {
        private int[][] genes;
        private int r;

        // int r harus ada karena Kombinasi
        // parameter genes dalam Constructor dibuat 1 dimensi
        //constructor dengan r
        public ChromosomeA(int[][] genes, int r) {
            this.genes = new int[genes.length][genes[0].length];
            this.r = r;

            for (int i = 0; i < genes.length; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        // harus ada r dammit
        //constructor without r
        public ChromosomeA(int[][] genes) {
            this(genes, countOnes(genes));
        }

        // ini static sok pintar ngl
        // gw tau static gunanya apa tapi kita hampir gakan pakai
        // google static buat apa
        
        // btw tadi gw scroll ke bawah 
        // ujung ujugnya coutnOnes gak kepakai anjg
        
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

        // jangan ada getGenes
        // kita tidak ingin objek lain bisa ngedit isi genes
        // selain itu beda chromosome bisa menyimpan gene dalam bentuk yang berbeda
        // kita ingin bisa mengakses array 1D, 2D, 3D, etc dengan cara yang sama
        
        //mengambil salinan genes
        public int[][] getGenes() {
            int[][] copy = new int[genes.length][genes[0].length];

            for (int i = 0; i < genes.length; i++) {
                copy[i] = genes[i].clone();
            }

            return copy;
        }

        // no
        public int getRows() {
            return genes.length;
        }
        // no
        public int getCols() {
            return genes[0].length;
        }
        // no, maybe
        // palingan r dibikin public final int kalau mau
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

        // tadi gw bilang jangan pakai getRows dan getCols
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
        // return type Chromosome, bukan ChromosomeA
        //mengambil salinan ChromosomeA
        public ChromosomeA duplicate() {
            return new ChromosomeA(getGenes(), r);
        }

        @Override
        // like I said forget method length
        //mengambil panjang dari ChromosomeA
        public int length() {
            return getRows() * getCols();
        }
    }

    // ChromosomeB bikin di file terpisah
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

            // jangan fucking clone it here
            for (int i = 0; i < 2; i++) {
                this.genes[i] = genes[i].clone();
            }
        }

        // apalah kontol ini lagi
        //constructor without maxFirst and maxSecond
        public ChromosomeB(int[][] genes) {
            this(genes, findMax(genes[0]) + 1, findMax(genes[1]) + 1);
        }

        // fuck you
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

        // no
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

        // getGene harusnya return int goddammit
        // gen itu 1 unit informasi
        //mengambil gene
        public int[] getGene(int index) {
            return new int[] {
                genes[0][index],
                genes[1][index]
            };
        }

        // value harusnya int
        //mengubah gene
        public void setGene(int index, int[] value) {
            genes[0][index] = value[0];
            genes[1][index] = value[1];
        }

        // fuck mengambil koordinat
        //mengambil koordinat x dan y dari station
        public int getX(int index) {
            return genes[0][index];
        }

        public int getY(int index) {
            return genes[1][index];
        }

        // gada station station dalam Chromosome
        //mengubah koordinat x dan y dari station
        public void setStation(int index, int x, int y) {
            genes[0][index] = x;
            genes[1][index] = y;
        }

        public int getStationCount() {
            return genes[0].length;
        }

        // ini cool, no comment
        // ada beberapa hal, tapi kinda debatable
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

            // ingat length pakai final int di kelas parent Chromosome
            return length() == chromosome.length()
                && maxFirst == chromosome.maxFirst
                && maxSecond == chromosome.maxSecond;
        }

        @Override
        //mengambil salinan ChromosomeB
        public ChromosomeB duplicate() {
            // jangan masukkin original array di sini
            // masukin cloned array ke dalam sini
            return new ChromosomeB(getGenes(), maxFirst, maxSecond);
        }

        // like I said forget method length
        @Override
        public int length() {
            return getStationCount();
        }
    }
}
