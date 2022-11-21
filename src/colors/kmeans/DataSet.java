package colors.kmeans;

import colors.BaseColor;
import colors.LABColor;
import colors.test.Palette;

import java.util.*;

public class DataSet<A> {

    static class ColorPoint<C extends BaseColor<C>> implements IDataEntry<ColorPoint<C>> {

        //how many pixels of this color there are. Same as having multiple of these color points
        private final int weight;
        private final C color;
        private int clusterNo;

        public ColorPoint(C color, int weight) {
            this.color = color;
            this.weight = weight;
        }

        public ColorPoint(C color) {
            this(color,1);
        }

        @Override
        public IDataEntry<ColorPoint<C>> average(List<IDataEntry<ColorPoint<C>>> others) {
            List<C> pixels = new ArrayList<>();
            for(int i = 0; i<weight; i++){
                pixels.add(this.color);
            }
            for(var c : others){
                if(c == this)continue;
                for(int i = 0; i<c.cast().weight; i++){
                    pixels.add(c.cast().color);
                }
            }
            return new ColorPoint<>(BaseColor.mixColors(pixels));
        }

        public void setClusterNo(int clusterNo) {
            this.clusterNo = clusterNo;
        }

        public int getClusterNo() {
            return clusterNo;
        }

        @Override
        public float distTo(IDataEntry<ColorPoint<C>> a) {
            return this.color.distTo(a.cast().color);
        }

        @Override
        public ColorPoint<C> cast() {
            return this;
        }

        public C getColor() {
            return color;
        }
    }

    private final List<IDataEntry<A>> colorPoints = new LinkedList<>();

    private final List<IDataEntry<A>> lastCentroids = new LinkedList<>();

    private final List<Integer> indicesOfCentroids = new LinkedList<>();
    private final Random random;

    public <T extends IDataEntry<A>> DataSet(List<T> colors) {
        this.colorPoints.addAll(colors);
        this.random = new Random(Objects.hash(this.colorPoints.get(0).distTo(this.colorPoints.get(this.colorPoints.size() - 1))));
    }

    public static <C extends BaseColor<C>> DataSet<ColorPoint<C>> fromColors(List<C> colors) {
        List<ColorPoint<C>> list = colors.stream().map(ColorPoint::new).toList();
        return new DataSet<>(list);
    }

    public static DataSet<ColorPoint<LABColor>> fromPalette(Palette palette) {
        return new DataSet<>(palette.getValues().stream().map(p -> new ColorPoint<>(p.lab(), p.occurrence)).toList());
    }

    public IDataEntry<A> calculateCentroid(int clusterNo) {

        List<IDataEntry<A>> colorsInCluster = new LinkedList<>();
        for (IDataEntry<A> colorPoint : colorPoints) {
            if (colorPoint.getClusterNo() == clusterNo) {
                //add colors to the same cluster
                colorsInCluster.add(colorPoint);
            }
        }
        return colorsInCluster.get(0).average(colorsInCluster);
    }

    public List<IDataEntry<A>> recomputeCentroids(int clusterSize) {
        lastCentroids.clear();
        for (int i = 0; i < clusterSize; i++) {
            lastCentroids.add(calculateCentroid(i));
        }
        return lastCentroids;
    }
/*
    public IDataEntry<A> randomDataPoint() {

        Double min = minimums.get(name);
        Double max = maximums.get(name);
        res.put(name, min + (max - min) * random.nextDouble());
    }*/

    public IDataEntry<A> randomFromDataSet() {
        int index = random.nextInt(colorPoints.size());
        return colorPoints.get(index);
    }

    public Double calculateClusterSSE(IDataEntry<A> centroid, int clusterNo) {
        double SSE = 0.0;
        for (IDataEntry<A> colorPoint : colorPoints) {
            if (colorPoint.getClusterNo() == clusterNo) {
                float dist = centroid.distTo(colorPoint);
                SSE += (dist * dist);
            }
        }
        return SSE;
    }

    public Double calculateTotalSSE(List<IDataEntry<A>> centroids) {
        Double SSE = 0.0;
        for (int i = 0; i < centroids.size(); i++) {
            SSE += calculateClusterSSE(centroids.get(i), i);
        }
        return SSE;
    }

    public IDataEntry<A> calculateWeighedCentroid() {
        double sum = 0.0;

        for (int i = 0; i < colorPoints.size(); i++) {
            if (!indicesOfCentroids.contains(i)) {
                double minDist = Double.MAX_VALUE;
                for (int ind : indicesOfCentroids) {
                    double dist = colorPoints.get(i).distTo(colorPoints.get(ind));
                    if (dist < minDist)
                        minDist = dist;
                }
                if (indicesOfCentroids.isEmpty()) {
                    sum = 0.0;
                }
                sum += minDist;
            }
        }

        double threshold = sum * random.nextDouble();

        for (int i = 0; i < colorPoints.size(); i++) {
            if (!indicesOfCentroids.contains(i)) {
                double minDist = Double.MAX_VALUE;
                for (int ind : indicesOfCentroids) {
                    double dist = colorPoints.get(i).distTo(colorPoints.get(ind));
                    if (dist < minDist)
                        minDist = dist;
                }
                sum += minDist;

                if (sum > threshold) {
                    indicesOfCentroids.add(i);
                    return colorPoints.get(i);
                }
            }
        }
        throw new UnsupportedOperationException("aa");
    }

    public List<IDataEntry<A>> getColorPoints() {
        return colorPoints;
    }

    public List<IDataEntry<A>> getLastCentroids() {
        return lastCentroids;
    }
}