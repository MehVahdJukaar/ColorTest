package colors;

import colors.test.PolarColor;

import java.util.ArrayList;
import java.util.Arrays;

//CIRCULAR LUV
public class HCLVColor extends PolarColor<HCLVColor> {

    public HCLVColor(float h, float c, float l, float a) {
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

    public float alpha() {
        return v3;
    }

    @Override
    public HCLVColor with(float v1, float v2, float v3, float v4) {
        return new HCLVColor(v1,v2,v3,v4);
    }

    public HCLVColor withHue(float hue) {
        return new HCLVColor(hue, chroma(), luminance(), alpha());
    }

    public HCLVColor withChroma(float chroma) {
        return new HCLVColor(hue(), chroma, luminance(), alpha());
    }

    public HCLVColor withLuminance(float luminance) {
        return new HCLVColor(hue(), chroma(), luminance, alpha());
    }

    public HCLVColor withAlpha(float alpha) {
        return new HCLVColor(hue(), chroma(), luminance(), alpha);
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HCLVtoLUV(this).asRGB();
    }

    @Override
    public HCLVColor asHCLV() {
        return this;
    }

    @Override
    public HCLVColor averageColors(HCLVColor... colors) {
        float size = colors.length + 1;
        var list = new ArrayList<>(Arrays.stream(colors).map(HCLVColor::hue).toList());
        list.add(this.hue());
        Float[] hues = list.toArray(Float[]::new);
        float s = this.chroma(), v = this.luminance(), a = this.alpha();
        for (HCLVColor c : colors) {
            s += c.chroma();
            v += c.luminance();
            a += c.alpha();
        }
        return new HCLVColor(averageAngles(hues), s / size, v / size, a / size);
    }

    @Override
    public HCLVColor average(HCLVColor color, float bias) {
        float i = 1 - bias;
        float h = weightedAverageAngles(this.hue(), color.hue(),  bias);
        while (h < 0) ++h;
        float c = this.chroma() * i + color.chroma() * bias;
        float b = this.luminance() * i + color.luminance() * bias;
        float a = this.alpha() * i + color.alpha() * bias;

        return new HCLVColor(h, c, b, a);
    }

    @Override
    public HCLVColor fromRGB(RGBColor rgb) {
        return rgb.asHCLV();
    }

    @Override
    public float distTo(HCLVColor other) {
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
