public class CustomStatistics
{
    static double[] meanAndStdev(double[] arr)
    {
        int i;
        double mean = 0.0;
        double stdev = 0.0;

        // variabel mean menyimpan sum sementara
        for (i = 0; i < arr.length; i++) mean += arr[i];
        // mean = sum / n
        mean /= arr.length;

        // stdev = sqrt(sigma{(xi - mean)^2} / (n - 1))
        for (i = 0; i < arr.length; i++) stdev += Math.pow((arr[i] - mean), 2);

        return new double[] {mean, stdev};
    }
}