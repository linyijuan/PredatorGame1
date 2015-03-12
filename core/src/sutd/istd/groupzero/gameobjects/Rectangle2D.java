package sutd.istd.groupzero.gameobjects;

/**
 * Created by User on 3/12/2015.
 */

public class Rectangle2D {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    public double Area;
    public double Perimeter;

    public Rectangle2D() {
        X = 0; Y = 0;
        Width = 1; Height = 1;
    }

    public Rectangle2D(double x, double y, double width, double height){
        X=x; Y=y; Width = width; Height = height;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public void setX(double x){
        X = x;
    }

    public void setY(double y){
        Y = y;
    }

    public double getX(){
        return X;
    }

    public double getY(){
        return Y;
    }

    public double getArea() {
        Area = Width*Height;
        return Area;
    }

    public double getPerimeter() {
        Perimeter = 2*Width + 2*Height;
        return Perimeter;
    }

    public boolean contains(double x, double y){
        boolean result = false;
        if (x<=(X+(Width/2)) && x>=(X-(Width/2)) && y<=(Y+(Height/2)) && y>=(Y-(Height/2))){
            result = true;
        }
        return result;
    }

    public boolean contains(Rectangle2D r){
        boolean result = false;
        double RleftX = r.getX() - (r.getWidth()/2);
        double RrightX = r.getX() + (r.getWidth()/2);
        double RtopY = r.getY() + (r.getHeight()/2);
        double RbotY = r.getY() - (r.getHeight()/2);
        if ((RleftX>=(X-(Width/2))) && (RrightX<=(X+(Width/2))) && (RtopY<=(Y+(Width/2))) && (RbotY>=(Y-(Width/2)))){
            result = true;
        }
        return result;
    }

    public boolean overlaps(Rectangle2D r){
        boolean result = false;
        double leftX = r.getX() - (r.getWidth()/2);
        double rightX = r.getX() + (r.getWidth()/2);
        double topY = r.getY() + (r.getHeight()/2);
        double botY = r.getY() - (r.getHeight()/2);
        if (contains(leftX, topY) == true || contains(leftX, botY) == true || contains(rightX, topY) == true || contains(rightX, botY) == true){
            result = true;
        }
        return result;
    }

}

