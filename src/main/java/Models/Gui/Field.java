package Models.Gui;



import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Field extends Canvas {
    private static double size = 100;
    private static double bold = convertPercentToNumber(15);
    private static double margin = convertPercentToNumber(20);
    private GraphicsContext graphicsContext;

    public static void setSize(double width, double boldPercent, double marginPercent){
        size=width;
        bold=convertPercentToNumber(boldPercent);
        margin=convertPercentToNumber(marginPercent);
    }
    public static void resetView(){
        size=100;
        bold=convertPercentToNumber(15);
        margin=convertPercentToNumber(20);
    }
    public Field() {
        super();
        this.setWidth(size);
        this.setHeight(size);
        graphicsContext = super.getGraphicsContext2D();
        graphicsContext.setLineWidth(bold);

    }

    public void clear(){
        graphicsContext.clearRect(0,0, size, size);
    }
    public void drawCircle(Color color){
        graphicsContext.setStroke(color);
        double width = size - margin*2;
        graphicsContext.strokeOval(margin,margin,width,width);
    }
    private static double convertPercentToNumber(double percent){
        return (percent*size)/100;
    }
    public void drawCross(Color color){

        graphicsContext.setStroke(color);
        double y = size-margin;
        graphicsContext.strokeLine(margin,y,y,margin);
        graphicsContext.strokeLine(margin,margin,y,y);

    }
    public void addLigtingEffect(){
        Lighting lighting = new Lighting();
        this.setEffect(lighting);
    }
    public void clearEffect(){
        this.setEffect(null);
    }

}
