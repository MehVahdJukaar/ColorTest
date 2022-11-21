import colors.*;
import colors.test.Palette;
import colors.test.PaletteColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JComponent implements ActionListener, MouseMotionListener, KeyListener {

    private static final int SCALE = 20;
    public static final int SIDE = 10;
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;


    public static void main(String[] args) {

        JFrame wind = new JFrame("Color space thingie");
        Main g = new Main();
        wind.add(g);
        wind.pack();
        wind.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        wind.setLocationRelativeTo(null);
        wind.setVisible(true);
        //wind.addMouseMotionListener(g);
        wind.addKeyListener(g);
        Timer tt = new Timer(17, g);
        tt.start();

        refresh();

    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }


    public static float lerp(float pDelta, float pStart, float pEnd) {
        return pStart + pDelta * (pEnd - pStart);
    }

    @Override
    protected void paintComponent(Graphics g) {

        /*
        for (int x=0; x< pixels.length; x++){
            for (int y=0; y< pixels[x].length; y++){
                g.setColor(pixels[x][y].getColor());
                g.fillRect(x*SCALE, y*SCALE, SCALE, SCALE);
            }
        }*/

        //averageColors color test

        var c = color.asLUV();

        var cc = c.asRGB();

        int y = 50;
        int dist = 70;


        int center = WIDTH/2;
        int left = center-200;
        int right = center+200;

        int size = 18;
        g.setFont(new Font("Arial", 8, size));

/*
        interpolateColor(g, color, color2, left, right, y, 0);
        interpolateColor2(g, color, color2, left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("RGB", left-100, y+20);
        g.drawString(color.toString(), left-200, y+40);
        g.drawString(color2.toString(), left+430, y+40);
        y += dist;

        interpolateColor(g, color.asLAB(), color2.asLAB(),  left, right, y, 0);
        interpolateColor2(g, color.asLAB(), color2.asLAB(), left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("LAB", left-100, y+20);
        g.drawString(color.asLAB().toString(), left-200, y+40);
        g.drawString(color2.asLAB().toString(), left+430, y+40);
        y += dist;

        var H = color.asHCL();
        var H2 = color2.asHCL();
        interpolateColor(g, H, H2,  left, right, y, 0);
        interpolateColor2(g, H, H2, left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("HCL", left-100, y+20);
        g.drawString(H.toString(), left-200, y+40);
        g.drawString(H2.toString(), left+430, y+40);
        y += dist;


        interpolateColor(g, color.asLUV(), color2.asLUV(),  left, right, y, 0);
        interpolateColor2(g, color.asLUV(), color2.asLUV(), left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("LUV", left-100, y+20);
        g.drawString(color.asLUV().toString(), left-200, y+40);
        g.drawString(color2.asLUV().toString(), left+430, y+40);
        y += dist;

        var Hv = color.asHCLV();
        var H2v = color2.asHCLV();
        interpolateColor(g, Hv, H2v,  left, right, y, 0);
        interpolateColor2(g, Hv, H2v, left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("HCLV", left-100, y+20);
        g.drawString(Hv.toString(), left-200, y+40);
        g.drawString(H2v.toString(), left+430, y+40);
        y += dist;


        interpolateColor(g, color.asHSL(), color2.asHSL(),  left, right, y, 0);
        interpolateColor2(g, color.asHSL(), color2.asHSL(), left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("HSL", left-100, y+20);
        g.drawString(color.asHSL().toString(), left-200, y+40);
        g.drawString(color2.asHSL().toString(), left+430, y+40);
        y += dist;

        interpolateColor(g, color.asHSV(), color2.asHSV(),  left, right, y, 0);
        interpolateColor2(g, color.asHSV(), color2.asHSV(), left, right, y+30);
        g.setColor(Color.BLACK);
        g.drawString("HSV", left-100, y+20);
        g.drawString(color.asHSV().toString(), left-200, y+40);
        g.drawString(color2.asHSV().toString(), left+430, y+40);
*/

        ArrayList<PaletteColor> list = new ArrayList<>();

        Random r = new Random(jjj);


        for(int i = 0; i<6; i++){
            float rr = (color.red()+(r.nextFloat()-0.5f)*0.02f*i)%1;
            float gg = (color.green()+(r.nextFloat()-0.5f)*0.2f*i)%1;
            float bb = (color.blue()+(r.nextFloat()-0.5f)*0.02f*i)%1;
            list.add(new PaletteColor(new HSLColor(rr,gg,bb,1)));
        }
        Palette p = new Palette(list);
        var hh = p.increaseInner();

        for(int i = 0; i<p.size(); i++){
            var pc = p.get(i);
            drawColorAt(g,20+40*i, 20, pc.rgb(), 40);
            drawColorAt(g,20+40*i, 60, new RGBColor(pc.luminance(),pc.luminance(),pc.luminance(),1), 40);
            drawColorAt(g,20+40*i, 100, new RGBColor(pc.hcl().hue(),pc.hcl().hue(),pc.hcl().hue(),1), 40);
            drawColorAt(g,20+40*i, 140, new RGBColor(pc.hcl().chroma(),pc.hcl().chroma(),pc.hcl().chroma(),1), 40);
        }
        drawColorAt(g,20 , 80, hh.rgb(), 40);


        p.updateTolerance(tol);
        for(int i = 0; i<p.size(); i++) {
            var pc = p.get(i);
            drawColorAt(g, 20 + 40 * i, 200, pc.rgb(), 40);
        }

        y += dist+150;
        g.drawString("tol"+tol, 100, y + 20);
        /*
        int steps = 100;
        int r = 90;
        int w = 70;

        circle(g, steps, r, w, color.asHCL(),-20,-20);

        circle(g, steps, r, w, color.asHCLV(),300,-20);

        circle(g, steps, r, w, color.asHSL(),-20,250);

        circle(g, steps, r, w, color.asHSV(),300,250);


        y += dist+400;
        */

        g.setColor(Color.BLACK);

        float distr = color.asHCL().distTo(color2.asHCL());
        float distl = color.asLAB().distTo(color2.asLAB());

        var hclMid = color.asHCL().average(color2.asHCL(),0.5f);

        /*
        g.drawString("dist HCL: "+distr +" dist LAB: "+distl, 100, y + 20);

        g.drawString(hclMid.toString(), 100, y + 60);

        g.drawString("press '1' to pick 2 random colors", 100, y + 40);


        */
        drawColorAt(g, 400, y+60,color.asRGB(),30);
    }

    private <T extends BaseColor<T>> void circle(Graphics g, int steps, int r, int w,T color, int x0, int y0) {

        for (float a = 0; a < 1; a += 1f / steps) {

            int x = (int) (r * Math.sin(a * 2 * Math.PI))+x0;
            int y = (int) (r * Math.cos(a * 2 * Math.PI))+y0;

            var rgb = color.asRGB();
            g.setColor(new Color(rgb.red(), rgb.green(), rgb.blue()));
            g.fillRoundRect(r + x + w / 2, r + y + w / 2, w, w, r, r);

            if(color instanceof HCLVColor c){
                color = (T) c.withHue(a);
            }
            else if(color instanceof HCLColor c){
                color = (T) c.withHue(a);
            }
            else if(color instanceof HSVColor c){
                color = (T) c.withHue(a);
            }
            else if(color instanceof HSLColor c){
                color = (T) c.withHue(a);
            }
        }
        g.setColor(Color.BLACK);
        g.drawString(color.toString(), r+x0,r+y0);
    }

    //take values from 0 to 1
    public static <T extends BaseColor<T>> void interpolateColor2(Graphics g, T a, T b, int x, int x2, int y) {

        int max = 300;
        for(int i = 0; i<max; i++){
            float p = i/(float)max;
            var med = a.average(b, p);
            RGBColor rgb = med.asRGB();
            float px = x + (x2-x)*p;
            if(rgb.clamped != 0){
                float v = rgb.clamped+0.5f;
                drawColorAt(g, (int) px, y+10, new RGBColor(v,1-v,0,1), 2);
            }
            drawColorAt(g, (int) px, y, rgb, 2);
        }
    }

    public static <T extends BaseColor<T>> void interpolateColor(Graphics g, T a, T b, int x, int x2, int y, int maxRec) {
        var med = a.average(b, 0.5f);
        RGBColor rgb = med.asRGB();
        if (maxRec > 7) {

            if(rgb.clamped != 0){
                float v = rgb.clamped+0.5f;
                drawColorAt(g, x, y+10, new RGBColor(v,1-v,0,1), 2);
            }
            drawColorAt(g, x, y, rgb, 2);
            return;
        }
        maxRec++;
        interpolateColor(g, a, med.fromRGB(rgb), x, (x + x2) / 2, y, maxRec);
        interpolateColor(g,  med.fromRGB(rgb), b, (x + x2) / 2, x2, y, maxRec);

    }

    ;

    public static void drawColorAt(Graphics g, int x, int y, RGBColor color, int width) {
        try {
            g.setColor(new Color(color.red(), color.green(), color.blue()));
            g.fillRoundRect(x, y, width, 30, 0, 0);
        } catch (Exception e) {
            int aa = 1;
        }
    }

    public static double dist(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
    }


    public float tol = 0;
    public   int jjj = 0;
    public   int fff = 0;
    public static final Random rand = new Random(23422);
    public static RGBColor color = new HCLColor(209/255f,23/255f,219/255f,1).asRGB();
    public static RGBColor color2 = new HCLColor(101/255f,46/255f,206/255f,1).asRGB();

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == '1') {
            fff = jjj;
            jjj = rand.nextInt(200);
           // color = new RGBColor(rand.nextInt());
            color2 = new RGBColor(rand.nextInt());
        }else if(c == 'w'){
            var v = color.asHSV();
            color = v.withSaturation((v.saturation()+0.02f) % 1).asRGB();
        }else if(c == 's'){
            var v = color.asHSV();
            color = v.withSaturation((v.saturation()-0.02f) % 1).asRGB();
        }else if(c == 'a'){
            var v = color.asHSV();
            color = v.withValue((v.value()+0.02f) % 1).asRGB();
        }else if(c == 'd'){
            var v = color.asHSV();
            color = v.withValue((v.value()-0.02f) % 1).asRGB();
        }else if(c == 'e'){
            var v = color.asHSV();
            color = v.withHue((v.hue()+0.02f) % 1).asRGB();
        }else if(c == 'q'){
            var v = color.asHSV();
            color = v.withHue((v.hue()-0.02f) % 1).asRGB();
        }else if(c == 'c'){
            tol +=0.0001;
        }else if(c == 'v'){
            tol -=0.0001;
        }   else     if (c == 'g') {
            jjj = fff;
        }

        refresh();

        repaint();
    }

    private static void refresh() {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}