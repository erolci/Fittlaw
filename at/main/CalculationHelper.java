package at.fhooe.mc.it.pro2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import at.fh.ooe.mcm.it.pro2.regression.RegressionLine;
import at.fh.ooe.mcm.it.pro2.regression.RegressionPoint;

import com.livescribe.display.Display;
import com.livescribe.geom.Point;
import com.livescribe.geom.Rectangle;
import com.livescribe.penlet.PenletContext;
import com.livescribe.storage.PenletStorage;
import com.livescribe.ui.ScrollLabel;
import com.livescribe.util.MathFunctions;

/**
 * Helper for our coefficient calculation penlets
 */
public class CalculationHelper {
	/**
	 * Reference to the display and a label for showing text
	 */
    private Display mDisplay;
    private ScrollLabel mLabel;
    /**
     * Main penlet context
     */
    private PenletContext mContext;

    /**
     * Current calculated Coefficient a
     */
    private double mCurrentA;

    /**
     * Current calculated Coefficient b
     */
    private double mCurrentB;

    /**
     * Starting a Value
     */
    private static final double INIT_A = 10;
    /**
     * Starting b Value
     */
    private static final double INIT_B = 650;
    
    /**
     * Vector to store the regression points
     */
    private Vector mRegressionPoints;

    /**
     * Filename for storing the a and b coefficients on the penletstorage
     */
    private static final String STORE_FILENAME = "InTCoefficientStore.int";
    
    
    /**
     * Constructor for labels, images, and sounds
     * @param context Pass the penlet context
	 * @param player Pass the media player or null to skip sound processing
     */
    public CalculationHelper(PenletContext context) {
    	this.mDisplay = context.getDisplay();
    	this.mLabel = new ScrollLabel();
    	this.mContext = context;
        mRegressionPoints = new Vector();
        
        readData(); //Read data from penletstorage
    }
    /**
     * Helper function: Calculate the power of exp to the base
     * @param base Base
     * @param exp exponent
     * @return calculated power
     */
    private static double pow(double base, int exp){
        if(exp == 0) return 1;
        double res = base;
        for(;exp > 1; --exp)
            res *= base;
        return res;
    }

    /**
     * Calculate log2 of the given value
     * @param _num Value to calculate the log2
     * @return log2 value of _num
     */
   public static double log2(double _num)
    {
    	return (MathFunctions.log(_num)/MathFunctions.log(2));
    } 

    
    /**
     * Process the fitts law calculation and at it to the regression line calculation vector
     * @param _startPoint Where is the stroke started
     * @param _finishRectangle in which rectangle it did end
     * @param _startTime Starting time of the stroke
     * @param _endTime Ending time of the stroke
     * @return boolean if the processing was successful
     */
    public boolean processFittsCalc(Point _startPoint, Rectangle _finishRectangle, long _startTime, long _endTime)
    {
    	if(_startPoint == null|| _finishRectangle==null)
    	{
            this.mLabel.draw("no rect", true);
            this.mDisplay.setCurrent(this.mLabel);    
    		return false;
    	}
    	int xFinish = _finishRectangle.getX()+_finishRectangle.getWidth()/2;
    	int yFinish = _finishRectangle.getY()+_finishRectangle.getHeight()/2;
    	double distToObj = Math.sqrt(pow(_startPoint.getX()-xFinish,2)+pow(_startPoint.getY()-yFinish,2));
    	double bit = log2((distToObj/_finishRectangle.getWidth())+0.5);
    	
    	updateData(bit,_endTime-_startTime);
    	calculateRegLine();
        
    	return true;
    }
    /**
     * process the calculation of the steering law 
     * @param _rect rectangle in which the steering calculation should happen
     * @param _StartTime when did the stroke start
     * @param _finishTime whed did it end
     */
    public void processSteeringCalc(Rectangle _rect, long _StartTime, long _finishTime) {
		double bit = _rect.getWidth()/_rect.getHeight();
    	updateData(bit,_finishTime-_StartTime);
    	calculateRegLine();
        
	}
    
    /**
     * Updates the data. save the current Regressionpoint in our vector 
     * @param _bit the bit value which should be stored
     * @param _mt the calculated time in ms
     */
    private void updateData(double _bit, double _mt)
    {
    	if(mRegressionPoints==null)
    		return;
    	mRegressionPoints.addElement(new RegressionPoint(_bit, _mt));
        
    }
    /**
     * Calculate the regression line
     */
    public void calculateRegLine()
    {
    	if(mRegressionPoints==null)
    		return;
    	RegressionPoint[] regArray = new RegressionPoint[mRegressionPoints.size()];
    	
        mRegressionPoints.copyInto(regArray);
        
        RegressionLine line = new RegressionLine(regArray);
        line.calculateRegressionLine();
        mCurrentA= line.getA();
        mCurrentB= line.getB();

        showText("A: "+mCurrentA+" B: "+mCurrentB);
        
    }
    
    /**
     * Store the current values to the penletsotrage
     */
    public void storeData()
    {
    	 PenletStorage ps = mContext.getInternalPenletStorage();
    	 try {
			DataOutputStream stream = new DataOutputStream(ps.openOutputStream(STORE_FILENAME));
			stream.writeDouble(mCurrentA);
			stream.writeDouble(mCurrentB);
			stream.flush();
			stream.close();
			showText("Data successfully stored");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    }

    /**
     * Read data from penletstorage and restore the currentA and currentB values
     */
    public void readData()
    {
    	
    	PenletStorage ps = mContext.getInternalPenletStorage();

 		try {
			if(!ps.exists(STORE_FILENAME))
			{
				mCurrentA=INIT_A;
				mCurrentB=INIT_B;
				return; 
			}
			
 			DataInputStream stream = new DataInputStream(ps.openInputStream(STORE_FILENAME));

 			mCurrentA = stream.readDouble();
 			mCurrentB = stream.readDouble();
 		    showText("A: "+mCurrentA+" B: "+mCurrentB);
 		     
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    }
    
    /**
     * Display text on the pen.
     * @param _text Text to display
     */
    public void showText(String _text)
    {
        this.mLabel.draw(_text, true);
        this.mDisplay.setCurrent(this.mLabel);    		
    }
    /**
     * returns the a coefficient
     * @return
     */
    public double getA()
    {
    	return mCurrentA;
    }
    /**
     * returns the a coefficient
     * @return
     */
    public double getB()
    {
    	return mCurrentB;
    }
}
