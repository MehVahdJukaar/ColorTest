package colors.test;



public class MthUtils {

    public static float[] polarToCartesian(float a, float r){
        float x = (float) (r * Math.cos(a));
        float y = (float) (r * Math.sin(a));
        return new float[]{x,y};
    }

    public static float signedAngleDiff(double to, double from){
        float x1 = (float) Math.cos((float) to);
        float y1 = (float) Math.sin((float) to);
        float x2 = (float) Math.cos((float) from);
        float y2 = (float) Math.sin((float) from);
        return (float) Math.atan2( x1*y1 - y1*x2, x1*x2 + y1*y2 );
    }
}
