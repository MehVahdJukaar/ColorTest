package colors.test;

import colors.BaseColor;

public abstract class PolarColor <T extends BaseColor<T> > extends BaseColor<T> {
    protected PolarColor(float v0, float v1, float v2, float v3) {
        super(v0, v1, v2, v3);
    }

    public float getAngle(){
        return v0;
    }

    public float getX(){
        return v1;
    }

    public float getY(){
        return v2;
    }
}
