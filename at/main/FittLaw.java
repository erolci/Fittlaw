package at.fhooe.mc.it.pro2;

import com.livescribe.geom.Point;
import com.livescribe.geom.Rectangle;
import com.livescribe.util.MathFunctions;

public class FittLaw {

	private Rectangle target;
	private Rectangle base;
	private Point cursor;
	private double a =53;
	private double b = 148;
	
	
	/**
	 * default constructor
	 */
	public FittLaw(){
		target = new Rectangle();
		base = new Rectangle();
		cursor = new Point();
		this.target=null;
		this.base = null;
		this.cursor =null;
	}
	/**
	 * calculate the fitts law between a rectangle and a single point
	 * @return the time in ms
	 */
	public int calculate(){
		
		double targetWidth=0;
		double distance =0;
		targetWidth = target.getWidth();
		Point centerTarget=new Point(target.getX()+target.getWidth()/2,target.getY()+target.getHeight()/2);
		distance = calculateDistance(centerTarget,cursor);
		/*if(cursor.getX()>target.getX()){
			distance = (-1)*(target.getX() - this.cursor.getX());
		}else{
			distance = target.getX() - this.cursor.getX();
		}*/
		this.target =null;
		this.base =null;
		return (int)(a+(b*(log2(distance/targetWidth +1))));
	}
	/**
	 * calculate the fitts law between two rectangles
	 * @return the time in ms
	 */
	public int calculateFitt(){
		double targetWidth =0;
		double distance  = 0;
		targetWidth = target.getWidth();
		Point centerTarget=new Point(target.getX()+target.getWidth()/2,target.getY()+target.getHeight()/2);
		Point centerBase=new Point(base.getX()+base.getWidth()/2,base.getY()+base.getHeight()/2);
		distance = calculateDistance(centerTarget,centerBase);
		/*if(target.getX()<base.getX()){
			distance = (-1)*(target.getX() - (base.getWidth() + base.getX()));
		}else{
			distance = target.getX() - (base.getWidth() + base.getX());	
		}*/
		this.target =null;
		this.base = null;
		return (int)(a+(b*(log2(distance/targetWidth +1))));
		
	}
	/**
	 * calculate the distance between to points
	 * @param _p1 point one
	 * @param _p2 point two
	 * @return the distance between the two points
	 */
	public double calculateDistance(Point _p1, Point _p2)
	{
		double xDiff=_p2.getX()-_p1.getX();
		double yDiff=_p2.getY()-_p1.getY();
		return Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
	}
	
	/**
	 * calculate the log2 of a value
	 * @param value
	 * @return
	 */
	public double log2(double value){
		return MathFunctions.log(value)/MathFunctions.log(2);
	}
	
	
	public boolean isTwoRectangleSelected(){
		
		return (this.target !=null & this.base !=null);
	}
	public boolean isOneRectangleOnePointSelected(){
		return (this.target !=null && this.cursor!=null);
	}
	public boolean isAllSelected(){
		return (this.target !=null && this.base !=null && this.cursor!=null);
	}

	public Rectangle getTarget() {
		return target;
	}

	public void setTarget(Rectangle target) {
		this.target = target;
	}

	public Rectangle getBase() {
		return base;
	}

	public void setBase(Rectangle base) {
		this.base = base;
	}

	public Point getCursor() {
		return cursor;
	}

	public void setCursor(Point cursor) {
		this.cursor = cursor;
	}
	
	
}
