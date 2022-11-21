package colors.kmeans;

import colors.LABColor;
import colors.test.Palette;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KMeans {

    static final Double PRECISION = 0.0;

    /* K-Means++ implementation, initializes K centroids from data */
    static <A> LinkedList<IDataEntry<A>> kmeanspp(DataSet<A> data, int K) {
        LinkedList<IDataEntry<A>> centroids = new LinkedList<>();

        centroids.add(data.randomFromDataSet());

        for (int i = 1; i < K; i++) {
            centroids.add(data.calculateWeighedCentroid());
        }
        return centroids;
    }

    /* K-Means itself, it takes a dataset and a number K and adds class numbers
     * to records in the dataset */
    static <A> void kmeans(DataSet<A> data, int K) {
        // select K initial centroids
        List<IDataEntry<A>> centroids = kmeanspp(data, K);

        // initialize Sum of Squared Errors to max, we'll lower it at each iteration
        Double SSE = Double.MAX_VALUE;

        while (true) {

            // assign observations to centroids

            List<IDataEntry<A>> points = data.getColorPoints();

            // for each record
            for (IDataEntry<A> point : points) {
                float minDist = Float.MAX_VALUE;
                // find the centroid at a minimum distance from it and add the record to its cluster
                for (int i = 0; i < centroids.size(); i++) {
                    float dist = centroids.get(i).distTo(point);
                    if (dist < minDist) {
                        minDist = dist;
                        point.setClusterNo(i);
                    }
                }

            }

            // recompute centroids according to new cluster assignments
            centroids = data.recomputeCentroids(K);

            // exit condition, SSE changed less than PRECISION parameter
            Double newSSE = data.calculateTotalSSE(centroids);
            if (SSE - newSSE <= PRECISION) {
                break;
            }
            SSE = newSSE;
        }
    }

    public static void main(String[] args) {
        try {
            // read data
            Palette p = new Palette();
            DataSet<DataSet.ColorPoint<LABColor>> data = DataSet.fromPalette(p);

            // cluster
            kmeans(data, 2);

            Map<Integer, Integer> colorToColorMap = new HashMap<>();

            for(var c : data.getColorPoints()){
                var centroid = data.getLastCentroids().get(c.getClusterNo());
                colorToColorMap.put(c.cast().getColor().asRGB().toInt(),centroid.cast().getColor().asRGB().toInt());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
