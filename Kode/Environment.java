import java.util.ArrayList;
import java.util.LinkedList;

public class Environment
{
    // precompute directions (atas, bawah, kiri, kanan)
    // dibuat static karena semua Environment pasti menggunakan direction yang sama meskipun dalam kasus ini hanya ada 1
    private static final int[][] dir = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private char[][] map;
    private int[][][] dist;
    private ArrayList<Point> freePositions;

    public final int unreachablePenalty;
    public final int fireStationCount;
    public final int houseCount;
    public final int mapHeight;
    public final int mapWidth;
    public final int freePositionCount;

    public Environment(char[][] map, int fireStationCount)
    {
        // asumsi map 0-indexed
        this.map = map;
        this.fireStationCount = fireStationCount;
        mapHeight = map.length;
        mapWidth = map[0].length;
        // asumsi n (tinggi map) dan m (lebar map) pada setiap testcase >= 1
        unreachablePenalty = this.mapHeight * this.mapWidth;
        precomputeDistance();
        houseCount = dist.length;
        findFreePositions();
        freePositionCount = freePositions.size();
    }

    // precompute jarak setiap rumah ke setiap petak agar jarak suatu rumah ke setiap petak dapat diakses dengan kompleksitas O(1)
    private void precomputeDistance()
    {
        // pre-declaration
        int i, j, k, l, depth;

        // houses dicatat menggunakan Queue (diimplementasi dengan LinkedList) karena pada awalnya jumlah rumah tidak diketahui
        // sebenarnya dapat dilakukan dengan ArrayList tetapi sedikit lebih lambat (tidak murni O(1))
        ArrayList<Point> houses = new ArrayList<>();

        for (i = 0; i < this.mapHeight; i++)
        {
            for (j = 0; j < this.mapWidth; j++)
            {
                // mencatat koordinat setiap rumah
                if (map[i][j] == 'H') houses.add(new Point(i, j));
            }
        }

        dist = new int[houses.size()][this.mapHeight][this.mapWidth];
        for (l = houses.size() - 1; l >= 0; l--)
        {
            LinkedList<Point> bfs = new LinkedList<>();
            // masukkan petak sumber (rumah saat ini)
            // urutan rumah yang diproses terlebih dahulu tidak penting
            bfs.offer(houses.get(l));

            for (i = 0; i < this.mapHeight; i++)
            {
                for (j = 0; j < this.mapWidth; j++)
                {
                    // asumsikan di awal semua petak unreachable sampai dibuktikan sebaliknya
                    dist[l][i][j] = unreachablePenalty;
                }
            }

            // perhitungan dimulai dengan adjacent neighbour dari masing masing house (yang memiliki jarak 1)
            depth = 1;
            // pre-declaration
            Point cur, nex;
            // jarak rumah ke dirinya sendiri tidak diset 0 karena ada obstacle (rumah)
            // jika ada fire station diletakkan pada petak tersebut maka penempatan invalid dan tidak perlu menghitung jarak
            while (!bfs.isEmpty())
            {
                // k menunjukkan jumlah petak yang akan diperhitungkan pada satu kedalaman
                // petak yang masih tersisa pada bfs saat k bernilai 0 berada pada kedalaman selanjutnya
                for (k = bfs.size(); k > 0; k--)
                {
                    cur = bfs.poll();
                    
                    // generate tetangga di 4 arah
                    for (i = 0; i < 4; i++)
                    {
                        nex = new Point(cur.row + dir[i][0], cur.col + dir[i][1]);
                        // boundary check dan obstacle check dan redundancy check
                        if (nex.row >= 0 && nex.col >= 0 && nex.row < this.mapHeight && nex.col < this.mapWidth
                            && map[nex.row][nex.col] == '.' && dist[k][nex.row][nex.col] > depth)
                        {
                            bfs.offer(nex);
                            dist[k][nex.row][nex.col] = depth;
                        }
                    }
                }
                depth++;
            }
        }
    }

    // mencari posisi kosong pada peta yang dapat digunakan untuk keperluan self repair Individu
    private void findFreePositions()
    {
        freePositions = new ArrayList<>();
        
        int i, j;
        for (i = 0; i < mapHeight; i++)
        {
            for (j = 0; i < mapWidth; j++)
            {
                if (map[i][j] == '.') freePositions.add(new Point(i, j));
            }
        }
    }
    
    // jarak rumah ke-ref ke petak row, col
    public int getDist(int index, int row, int col)
    {
        return dist[index][row][col];
    }

    // memeriksa jika di sebuah posisi di map bebas obstacle
    // namanya bukan isEmpty() agar tidak menjadi distraksi saat menggunakan struktur data yang memiliki method isEmpty()
    public boolean isFree(int row, int col)
    {
        return this.map[row][col] == '.';
    }

    // memberikan salah satu posisi kosong secara out of place
    public Point getFreePosition(int index)
    {
        return freePositions.get(index);
    }
}
