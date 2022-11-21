package colors;

import colors.test.PolarColor;

import java.util.ArrayList;
import java.util.Arrays;

public class HSVColor extends PolarColor<HSVColor> {

    public HSVColor(float h, float s, float b, float a) {
        super(h, s, b, a);
    }

    @Override
    public String toString() {
        return String.format("H: %s, S: %s, V %s", (int) (255 * hue()), (int) (255 * saturation()), (int) (255 * value()));
    }

    //color
    public float hue() {
        return v0;
    }

    public float saturation() {
        return v1;
    }

    //how intense this color is
    public float value() {
        return v2;
    }

    public float alpha() {
        return v3;
    }

    @Override
    public HSVColor with(float v1, float v2, float v3, float v4) {
        return new HSVColor(v1,v2,v3,v4);
    }

    public HSVColor withHue(float hue) {
        return new HSVColor(hue, saturation(), value(), alpha());
    }

    public HSVColor withSaturation(float saturation) {
        return new HSVColor(hue(), saturation, value(), alpha());
    }

    public HSVColor withValue(float value) {
        return new HSVColor(hue(), saturation(), value, alpha());
    }

    public HSVColor withAlpha(float alpha) {
        return new HSVColor(hue(), saturation(), value(), alpha);
    }

    @Override
    public HSVColor asHSV() {
        return this;
    }

    @Override
    public RGBColor asRGB() {
        return ColorSpaces.HSVtoRGB(this);
    }

    @Override
    public HSVColor averageColors(HSVColor... colors) {
        float size = colors.length + 1;
        var list = new ArrayList<>(Arrays.stream(colors).map(HSVColor::hue).toList());
        list.add(this.hue());
        Float[] hues = list.toArray(Float[]::new);
        float s = this.saturation(), v = this.value(), a = this.alpha();
        for (HSVColor c : colors) {
            s += c.saturation();
            v += c.value();
            a += c.alpha();
        }
        return new HSVColor(averageAngles(hues), s / size, v / size, a / size);
    }

    @Override
    public HSVColor average(HSVColor color, float bias) {
        float i = 1 - bias;
        float h = weightedAverageAngles(this.hue(), color.hue(), bias);
        while (h < 0) ++h;
        float s = this.saturation() * i + color.saturation() * bias;
        float v = this.value() * i + color.value() * bias;
        float a = this.alpha() * i + color.alpha() * bias;

        return new HSVColor(h, s, v, a);
    }

    @Override
    public HSVColor fromRGB(RGBColor rgb) {
        return rgb.asHSV();
    }

    @Override
    public float distTo(HSVColor other) {
        float h = this.hue();
        float h2 = other.hue();
        float c = this.saturation();
        float c2 = other.saturation();
        double x = c * Math.cos(h * Math.PI * 2) - c2 * Math.cos(h2 * Math.PI * 2);
        double y = c * Math.sin(h * Math.PI * 2) - c2 * Math.sin(h2 * Math.PI * 2);

        return (float) Math.sqrt(x * x + y * y +
                (this.value() - other.value()) * (this.value() - other.value()));
    }
}
