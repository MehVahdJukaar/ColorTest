package colors.test;

import colors.BaseColor;
import colors.HCLColor;
import colors.LABColor;
import colors.RGBColor;

public class PaletteColor implements Comparable<PaletteColor> {

    //caches all of these, so we don't have to look them up constantly
    private final int value;
    private final RGBColor color;
    private final LABColor lab;
    private final HCLColor hcl;

    public int occurrence = 0;

    public PaletteColor(int color) {
        this(new RGBColor(color));
    }

    public PaletteColor(BaseColor<?> color) {
        var c = color.asRGB();
        if (c.alpha() == 0) this.color = new RGBColor(0);
        else this.color = c;
        this.lab = this.color.asLAB();
        this.value = this.color.toInt();
        this.hcl = lab.asHCL();
    }

    public int value() {
        return value;
    }

    public RGBColor rgb() {
        return color;
    }

    public LABColor lab() {
        return lab;
    }

    public HCLColor hcl() {
        return hcl;
    }

    public float luminance() {
        return lab.luminance();
    }

    public float distanceTo(PaletteColor color) {
        return this.lab.distTo(color.lab);
    }

    @Override
    public int compareTo(PaletteColor o) {
        return Float.compare(this.lab.luminance(), o.lab.luminance());
        //return (int)(distanceTo(o)*255f)*Float.compare(this.lab.luminance(), o.lab.luminance());
    }

}