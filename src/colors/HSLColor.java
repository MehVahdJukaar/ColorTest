package colors;

import colors.test.PolarColor;

import java.util.ArrayList;
import java.util.Arrays;

//like LAB but has circular values
public class HSLColor extends PolarColor<HSLColor> {

    public HSLColor(float h, float s, float l, float a) {
        super(h, s, l, a);
    }

    @Override
    public String toString() {
        return String.format("H: %s, S: %s, L %s", (int)(255*hue()), (int)(255*saturation()), (int)(255*lightness()));
    }

    public float hue() {
        return v0;
    }

    public float saturation() {
        return v1;
    }

    public float lightness() {
        return v2;
    }

    @Override
    public HSLColor with(float v1, float v2, float v3, float v4) {
        return new HSLColor(v1,v2,v3,v4);
    }

    public HSLColor withHue(float hue) {
        return new HSLColor(hue, saturation(), lightness(), alpha());
    }

    public HSLColor withSaturation(float saturation) {
        return new HSLColor(hue(), saturation, lightness(), alpha());
    }

    public HSLColor withLightness(float lightness) {
        return new HSLColor(hue(), saturation(), lightness, alpha());
    }

    public HSLColor withAlpha(float alpha) {
        return new HSLColor(hue(), saturation(), lightness(), alpha);
    }


    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HSLtoRGB(this);
    }

    @Override
    public HSLColor asHSL() {
        return this;
    }

    @Override
    public HSLColor averageColors(HSLColor... colors) {
        float size = colors.length + 1;
        var list = new ArrayList<>(Arrays.stream(colors).map(HSLColor::hue).toList());
        list.add(this.hue());
        Float[] hues = list.toArray(Float[]::new);
        float s = this.saturation(), b = this.lightness(), a = this.alpha();
        for (HSLColor c : colors) {
            s += c.saturation();
            b += c.lightness();
            a += c.alpha();
        }
        return new HSLColor(averageAngles(hues), s / size, b / size, a / size);
    }

    @Override
    public HSLColor average(HSLColor color, float bias) {
        float i = 1 - bias;
        assert bias>=0 && bias<=1;
        float h = weightedAverageAngles(this.hue(), color.hue(), bias);
        while(h<0)++h;
        float s = this.saturation() * i + color.saturation() * bias;
        float l = this.lightness() * i + color.lightness() * bias;
        float a = this.alpha() * i + color.alpha() * bias;

        return new HSLColor(h, s, l, a);
    }

    @Override
    public HSLColor fromRGB(RGBColor rgb) {
        return rgb.asHSL();
    }

    @Override
    public float distTo(HSLColor other) {
        float h = this.hue();
        float h2 = other.hue();
        float c = this.saturation();
        float c2 = other.saturation();
        double x = c * Math.cos(h * Math.PI * 2) - c2 * Math.cos(h2 * Math.PI * 2);
        double y = c * Math.sin(h * Math.PI * 2) - c2 * Math.sin(h2 * Math.PI * 2);

        return (float) Math.sqrt(x * x + y * y +
                (this.lightness() - other.lightness()) * (this.lightness() - other.lightness()));
    }
}
