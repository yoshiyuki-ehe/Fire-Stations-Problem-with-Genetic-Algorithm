import java.util.LinkedList;

public class Environment
{
    // precompute directions (atas, bawah, kiri, kanan)
    // dibuat static karena semua Environment pasti menggunakan direction yang sama meskipun dalam kasus ini hanya ada 1
    private static final int[][] dir = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private final String[] map;
    private final int[][][] dist;

    public final int unreachablePenalty;
    public final int obstaclePenalty;
    public final int p;
    public final int h;

    public Environment(String[] map, int p)
    {
        // asumsi map 0-indexed
        this.map = map;
        this.p = p;
        // asumsi n (tinggi map) dan m (lebar map) pada setiap testcase >= 1
        this.unreachablePenalty = map.length * map[0].length();
        this.obstaclePenalty = this.unreachablePenalty + 1;
        this.dist = precomputeDistance();
        this.h = this.dist.length;
    }

    // precompute jarak setiap rumah ke setiap petak agar jarak suatu rumah ke setiap petak dapat diakses dengan kompleksitas O(1)
    private int[][][] precomputeDistance()
    {
        // pre-declaration
        int i, j, k, depth;
        // house menggunakan Queue (diimplementasi dengan LinkedList) karena pada awalnya jumlah rumah tidak diketahui
        // sebenarnya dapat dilakukan dengan ArrayList tetapi sedikit lebih lambat (tidak murni O(1))
        LinkedList<int[]> houses = new LinkedList<>();

        for (i = 0; i < map.length; i++)
        {
            for (j = 0; j < map[0].length(); j++)
            {
                // mencatat koordinat setiap rumah
                if (map[i].charAt(j) == 'H') houses.offer(new int[] {i, j});
            }
        }

        int[][][] dist = new int[houses.size()][map.length][map[0].length()];
        while (!houses.isEmpty())
        {
            LinkedList<int[]> bfs = new LinkedList<>();
            // masukkan petak sumber (rumah saat ini)
            // ukuran h akan digunakan sebagai indeks
            // urutan rumah yang diproses terlebih dahulu tidak penting karena semua rumah akan diperiksa pada fitness function
            bfs.offer(houses.poll());

            for (i = 0; i < map.length; i++)
            {
                for (j = 0; j < map[0].length(); j++)
                {
                    if (map[i].charAt(j) == '.')
                    {
                        // penalti jika petak unreachable = n * m
                        dist[houses.size()][i][j] = unreachablePenalty;
                    }
                    else
                    {
                        // penalti jika terdapat halangan = n * m + 1
                        dist[houses.size()][i][j] = obstaclePenalty;
                    }
                }
            }

            depth = 1;
            int[] cur, nex;
            // jarak rumah ke dirinya sendiri tidak diset 0 karena ada obstacle (rumah)
            // jika ada fire station diletakkan pada petak tersebut maka penempatan invalid (tidak perlu menghitung jarak)
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
                        for (j = 0; j < 2; j++)
                        {
                            nex = new int[] {cur[0] + dir[i][j], cur[1] + dir[i][j]};
                            // boundary check dan obstacle check dan redundancy check
                            if (nex[0] >= 0 && nex[1] >= 0 && nex[0] < map.length && nex[1] < map[0].length()
                                && map[nex[0]].charAt(nex[1]) == '.' && dist[houses.size()][nex[0]][nex[1]] > depth)
                            {
                                bfs.offer(nex);
                                dist[houses.size()][nex[0]][nex[1]] = depth;
                            }
                        }
                    }
                }
                depth++;
            }
        }

        return dist;
    }

    // jarak rumah ke-href ke petak row, col
    public int getDist(int href, int row, int col)
    {
        return dist[href][row][col];
    }
}