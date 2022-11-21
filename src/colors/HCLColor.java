package colors;

import colors.test.PolarColor;

import java.util.ArrayList;
import java.util.Arrays;

public class HCLColor extends PolarColor<HCLColor> {

    public HCLColor(float h, float c, float l, float a) {
        super(h, c, l, a);
    }

    @Override
    public String toString() {
        return String.format("H: %s, C: %s, L %s", (int) (255 * hue()), (int) (255 * chroma()), (int) (255 * luminance()));
    }

    public float hue() {
        return v0;
    }

    public float chroma() {
        return v1;
    }

    public float luminance() {
        return v2;
    }

    @Override
    public HCLColor with(float v1, float v2, float v3, float v4) {
        return new HCLColor(v1,v2,v3,v4);
    }

    public HCLColor withHue(float hue) {
        return new HCLColor(hue, chroma(), luminance(), alpha());
    }

    public HCLColor withChroma(float chroma) {
        return new HCLColor(hue(), chroma, luminance(), alpha());
    }

    public HCLColor withLuminance(float luminance) {
        return new HCLColor(hue(), chroma(), luminance, alpha());
    }

    public HCLColor withAlpha(float alpha) {
        return new HCLColor(hue(), chroma(), luminance(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HCLtoLAB(this).asRGB();
    }

    @Override
    public HCLColor asHCL() {
        return this;
    }

    @Override
    public HCLColor averageColors(HCLColor... colors) {
        float size = colors.length + 1;
        var list = new ArrayList<>(Arrays.stream(colors).map(HCLColor::hue).toList());
        list.add(this.hue());
        Float[] hues = list.toArray(Float[]::new);
        float s = this.chroma(), v = this.luminance(), a = this.alpha();
        for (HCLColor c : colors) {
            s += c.chroma();
            v += c.luminance();
            a += c.alpha();
        }
        return new HCLColor(averageAngles(hues), s / size, v / size, a / size);
    }

    @Override
    public HCLColor average(HCLColor color, float bias) {
        float i = 1 - bias;
        float h = weightedAverageAngles(this.hue(), color.hue(),  bias);
        while (h < 0) ++h;
        float c = this.chroma() * i + color.chroma() * bias;
        float b = this.luminance() * i + color.luminance() * bias;
        float a = this.alpha() * i + color.alpha() * bias;

        return new HCLColor(h, c, b, a);
    }

    @Override
    public HCLColor fromRGB(RGBColor rgb) {
        return rgb.asHCL();
    }

    @Override
    public float distTo(HCLColor other) {
        float h = this.hue();
        float h2 = other.hue();
        float c = this.chroma();
        float c2 = other.chroma();
        double x = c * Math.cos(h * Math.PI * 2) - c2 * Math.cos(h2 * Math.PI * 2);
        double y = c * Math.sin(h * Math.PI * 2) - c2 * Math.sin(h2 * Math.PI * 2);

        return (float) Math.sqrt(x * x + y * y +
                (this.luminance() - other.luminance()) * (this.luminance() - other.luminance()));
    }
}
