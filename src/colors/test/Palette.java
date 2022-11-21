package colors.test;

import colors.BaseColor;
import colors.HCLColor;
import colors.LABColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Palette {

    private float tolerance = 0;
    //ordered from darkest to lightest (luminance)
    private final ArrayList<PaletteColor> internal = new ArrayList<>();

    public Palette(Collection<PaletteColor> colors) {
        this.internal.addAll(colors);
        this.sort();
    }

    /**
     * Changes tolerance settings and merge all colors that are close enough
     *
     * @param tolerance at what distance colors will be merged and consolidated into one
     */
    public void updateTolerance(float tolerance) {
        this.tolerance = tolerance;
        boolean recalculate;
        do {
            recalculate = false;
            for (int i = 1; i < this.size(); i++) {
                PaletteColor c0 = this.get(i - 1);
                PaletteColor c1 = this.get(i);
                if (c0.distanceTo(c1) <= tolerance) {
                    Palette tempPal = new Palette(List.of(c0, c1));
                    int after = i + 1;
                    while (after < this.size() && tempPal.calculateAverage().distanceTo(this.get(after)) <= tolerance) {
                        tempPal.add(this.get(after));
                        after++;
                    }
                    tempPal.getValues().forEach(this::remove);
                    this.add(tempPal.calculateAverage());
                    recalculate = true;
                }
            }
        } while (recalculate);
    }


    public int size() {
        return internal.size();
    }

    public List<PaletteColor> getValues() {
        return internal;
    }

    private void sort() {
        Collections.sort(internal);
    }

    public void add(PaletteColor color) {
        if (color.rgb().alpha() == 0) return;
        if (!hasColor(color)) {
            internal.add(color);
            this.sort();
        }
    }

    public void set(int index, PaletteColor color) {
        if (color.rgb().alpha() == 0) return;
        if (!hasColor(color)) internal.set(index, color);
    }

    public PaletteColor get(int index) {
        return internal.get(index);
    }

    public boolean hasColor(PaletteColor color) {
        return this.hasColor(color, this.tolerance);
    }

    public boolean hasColor(PaletteColor color, float tolerance) {
        if (color.rgb().alpha() != 0) {
            for (PaletteColor c : this.getValues()) {
                if (c.distanceTo(color) <= tolerance) {
                    return true;
                }
            }
        }
        return false;
    }

    public PaletteColor getDarkest() {
        return get(0);
    }

    public PaletteColor getLightest() {
        return get(internal.size() - 1);
    }

    public void remove(int index) {
        internal.remove(index);
        this.sort();
    }

    public void remove(PaletteColor color) {
        if (internal.remove(color)) {
            this.sort();
        }
    }

    public PaletteColor calculateAverage() {
        return new PaletteColor(LABColor.averageColors1(this.getValues().stream().map(PaletteColor::lab).toArray(LABColor[]::new)));
    }

    /**
     * Gets the color within this palette that most closely matches the average center color
     */
    public PaletteColor getCenterColor() {
        PaletteColor center = calculateAverage();
        return getColorClosestTo(center);
    }

    /**
     * Gets the color within this palette that most closely matches the given color
     */
    private PaletteColor getColorClosestTo(PaletteColor target) {
        PaletteColor bestMatch = target;
        float lastDist = 10000;
        for (var c : this.getValues()) {
            float dist = target.distanceTo(c);
            if (dist < lastDist) {
                lastDist = dist;
                bestMatch = c;
            }
        }
        return bestMatch;
    }




    /*
    public void matchSize(int targetSize) {
        if (this.size() <= 0 || targetSize <= 0) {
            throw new UnsupportedOperationException("Palette size can't be 0");
        }
        this.maybeReducePalette(targetSize);
        this.maybeIncreasePalette(targetSize);
    }*/

    private void maybeReducePalette(int targetSize) {
        //remove the one with least occurrence
        while (this.internal.size() > targetSize) {
            PaletteColor toRemove = internal.get(0);
            for (var p : internal) {
                if (p.occurrence < toRemove.occurrence) {
                    toRemove = p;
                }
            }
            internal.remove(toRemove);
        }
        this.sort();
    }

    /*
    private void maybeIncreasePalette(int targetSize) {
        //adds a color in the space between the two colors that differ the most
        while (internal.size() < targetSize) {
            float lastLum = 0;
            int ind = 0;
            for (int i = 1; i < internal.size(); i++) {
                float d = internal.get(i).luminance - internal.get(i - 1).luminance;
                if (d > lastLum) {
                    lastLum = d;
                    ind = i;
                }
            }

            int newColor = SpriteUtils.averageColors(internal.get(ind - 1).color, internal.get(ind).color);

            internal.add(new PaletteColor(0, 0, newColor));
            this.sort();
        }
    }*/

    /**
     * Calculates the average luminance different between each color. Ideally it should be somewhat constant
     */
    public float calculateAverageDeltaLuminance() {
        List<Float> list = new ArrayList<>();
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            list.add(l - lastLum);
            lastLum = l;
        }
        float total = 0;
        for (var v : list) total += v;
        return total / (float) list.size();
    }

    //add a highlight color
    public PaletteColor increaseUp() {
        assert (this.size() < 2);
        float averageDeltaLum = this.calculateAverageDeltaLuminance();
        HCLColor lightest = this.getLightest().hcl();
        HCLColor secondLightest = this.get(this.size() - 2).hcl();
        var cc = getNextColor(averageDeltaLum, lightest, secondLightest);
        PaletteColor pl = new PaletteColor(cc);
        this.add(pl);
        return pl;
    }

    public PaletteColor increaseDown() {
        assert (this.size() < 2);
        float averageDeltaLum = this.calculateAverageDeltaLuminance();
        HCLColor darkest = this.getDarkest().hcl();
        HCLColor secondDarkest = this.get(1).hcl();
        var cc = getNextColor(-averageDeltaLum, darkest, secondDarkest);
        PaletteColor pl = new PaletteColor(cc);
        this.add(pl);
        return pl;
    }


    private <T extends PolarColor<T>> T getNextColor(float lumIncrease, T source, T previous) {
        float newLum = source.getY() + lumIncrease;
        float h1 = source.getAngle();
        float c1 = source.getX();
        float a1 = source.alpha();
        float h2 = previous.getAngle();
        float c2 = previous.getX();
        float a2 = previous.alpha();
        float hueIncrease = (float) (-MthUtils.signedAngleDiff(h1 * Math.PI * 2, h2 * Math.PI * 2) / (Math.PI * 2.0));
        //better be conservative here. some hue increase might look bad even if they are the same as the last hue diff
        float newH = h1 + hueIncrease*0.5f;
        while (newH < 0) ++newH;

        float newC = c1 + (c1 - c2);
        float newA = a1 + (a1 - a2);
        return source.with(newH, newC, newLum, newA);
    }

    public PaletteColor increaseInner() {
        assert (this.size() < 2);
        int index = 1;
        float maxDelta = 0;
        float lastLum = this.get(0).luminance();
        for (int i = 1; i < this.size(); i++) {
            float l = this.get(i).luminance();
            float dl = l - lastLum;
            if (dl > maxDelta) {
                index = i;
                maxDelta = dl;
            }
            lastLum = l;
        }
        var c1 = this.get(index).hcl();
        var c2 = this.get(index-1).hcl();
        var newC = new PaletteColor(c1.average(c2,0.5f));
        //always adds, ignoring tolerance since we do want to add something
        this.add(newC);
        return newC;
    }

    /*
        //add a dark color
        public void increaseDown() {
            //float averageDeltaLum = (this.getLightest().luminance - this.getDarkest().luminance)/this.size()-1;
            var darkest = this.getDarkest();
            var secondDarkest = this.get(1);
            //float newLum = lightest.luminance+averageDeltaLum;
            var h2 = SpriteUtils.RGBtoHSV(darkest.color);
            var h1 = SpriteUtils.RGBtoHSV(secondDarkest.color);
            //float lum1 = lightest.luminance;
            // float lum2 = secondLightest.luminance;
            float v1 = h1[2];
            float v2 = h2[2];
            float dv = v2 - v1;
            float hue1 = h1[0];
            float hue2 = h2[0];
            float dh = hue2 - hue1;
            float sat1 = h1[1];
            float sat2 = h2[1];
            float ds = sat2 - sat1;
            float newHue = hue2 - (dh * dv);
            float newSat = sat2 - (ds * dv);
            float newVal = v2 + dv;
            this.add(new PaletteColor(SpriteUtils.HSVtoRGB(newHue, newSat, newVal)));
        }
    */

}