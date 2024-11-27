package math.colors;

public class XYZColor extends BaseColor<XYZColor> {

    public XYZColor(float x, float y, float z, float a) {
        super(x, y, z, a);
    }

    @Override
    public String toString() {
        return String.format("x: %s,y: %s, z %s", x(), y(), z());
    }

    public float x() {
        return v0;
    }

    public float y() {
        return v1;
    }

    public float z() {
        return v2;
    }

    public float alpha() {
        return v3;
    }

    public XYZColor withX(float x) {
        return new XYZColor(x, y(), z(), alpha());
    }

    public XYZColor withY(float y) {
        return new XYZColor(x(), y, z(), alpha());
    }

    public XYZColor withZ(float z) {
        return new XYZColor(x(), y(), z, alpha());
    }

    public XYZColor withAlpha(float alpha) {
        return new XYZColor(x(), y(), z(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.XYZtoRGB(this);
    }

    @Override
    public XYZColor asXYZ() {
        return this;
    }

    @Override
    public XYZColor fromRGB(RGBColor rgb) {
        return rgb.asXYZ();
    }
}
