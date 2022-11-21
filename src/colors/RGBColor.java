package colors;

public class RGBColor extends BaseColor<RGBColor> {

    public final float clamped;

    public RGBColor(int value) {
        this((value >> 0 & 255) / 255f, (value >> 8 & 255) / 255f, (value >> 16 & 255) / 255f, (value >> 24 & 255) / 255f);

    }

    public RGBColor(float r, float g, float b, float a) {
        super(Math.max(0,Math.min(r,1)), Math.max(0,Math.min(g,1)), Math.max(0,Math.min(b,1)), a);
        float c = 0;
        if(r<0)c-=0.1;
        if(g<0)c-=0.1;
        if(b<0)c-=0.1;
        if(r>1)c+=0.1;
        if(g>1)c+=0.1;
        if(b>1)c+=0.1;
        /*
        float max = Math.max(r,Math.max(g,b))-1;
        if(max>0){
            this.values[0] = r-max;
            this.values[1] = g-max;
            this.values[2] = b-max;
        }
        float min = Math.min(r,Math.min(g,b));

        if(min<0){
            this.values[0] = this.values[0]-max;
            this.values[1] = this.values[1]-max;
            this.values[2] = this.values[2]-max;
        }

        if(r<0||r>1 || g<0||g>1 || b<0||b>1){
            int aaa = 1;
        }*/

        this.clamped = c;
    }

    @Override
    public String toString() {
        return String.format("R: %s, G: %s, B %s",(int)(255*red()),(int)(255*green()),(int)(255*blue()));
    }

    public float red() {
        return v0;
    }

    public float green() {
        return v1;
    }

    public float blue() {
        return v2;
    }

    public float alpha() {
        return v3;
    }

    @Override
    public RGBColor with(float v1, float v2, float v3, float v4) {
        return new RGBColor(v1,v2,v3,v4);
    }

    public RGBColor withRed(float red) {
        return new RGBColor(red, green(), blue(), alpha());
    }

    public RGBColor withGreen(float green) {
        return new RGBColor(red(), green, blue(), alpha());
    }

    public RGBColor withBlue(float blue) {
        return new RGBColor(red(), green(), blue, alpha());
    }

    public RGBColor withAlpha(float alpha) {
        return new RGBColor(red(), green(), blue(), alpha);
    }


    @Override
    public RGBColor asRGB() {
        return this;
    }

    @Override
    public RGBColor averageColors(RGBColor... colors) {
        float size = colors.length + 1;
        float r = this.red(), g = this.green(), b = this.blue(), a = this.alpha();
        for (RGBColor c : colors) {
            r += c.red();
            g += c.green();
            b += c.blue();
            a += c.alpha();
        }
        return new RGBColor(r / size, g / size, b / size, a / size);
    }

    @Override
    public RGBColor average(RGBColor color, float bias) {
        float i = 1 - bias;
        float r = this.red() * i + color.red() * bias;
        float g = this.green() * i + color.green() * bias;
        float b = this.blue() * i + color.blue() * bias;
        float a = this.alpha() * i + color.alpha() * bias;

        return new RGBColor(r, g, b, a);
    }

    public int toInt() {
        return  color((int) (this.alpha() * 255), (int) (this.red() * 255),
                (int) (this.green() * 255), (int) (this.blue() * 255));
    }

    public static int color(int pAlpha, int pRed, int pGreen, int pBlue) {
        return pAlpha << 24 | pRed << 16 | pGreen << 8 | pBlue;
    }

    @Override
    public RGBColor fromRGB(RGBColor rgb) {
        return this;
    }
}
