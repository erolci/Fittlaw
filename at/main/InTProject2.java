package at.fhooe.mc.it.pro2;


import com.livescribe.afp.PageInstance;
import com.livescribe.display.Display;
import com.livescribe.event.MenuEvent;
import com.livescribe.event.MenuEventListener;
import com.livescribe.event.PenTipListener;
import com.livescribe.event.RegionEnterExitListener;
import com.livescribe.event.StrokeListener;
import com.livescribe.ext.util.Log;
import com.livescribe.geom.Point;
import com.livescribe.geom.Rectangle;
import com.livescribe.geom.Stroke;
import com.livescribe.penlet.Penlet;
import com.livescribe.penlet.Region;
import com.livescribe.penlet.RegionCollection;
import com.livescribe.storage.StrokeStorage;
import com.livescribe.ui.ScrollLabel;



/**
 * Class to calculate fitts law and steering law. It also inclucdes the function to make a case study
 * for the coefficients of these laws.  
 * @author Erol Ciftci
 *
 */
public class InTProject2 extends Penlet  implements PenTipListener, StrokeListener, MenuEventListener, RegionEnterExitListener {
		private static final int AREA_ID = 100;   
		/**
		 * Helper class for the coefficient calculation
		 */
		private CalculationHelper tapAndPlayHelper;
		/**
		 * Store the starting point
		 */
	    private Point mStartPoint;
	    /**
	     * Finishing rectangle at the coefficient calculation
	     */
	    private Rectangle mFinishRectangle;
	    /**
	     * Store time when a stroke is started
	     */
	    private long mStartTime;
	    /**
	     * Time when the stroke ended
	     */
	    private long mFinishTime;
	    
	    
	    /**
	     * Reference to the display and the label 
	     */
	    private Display display;
	    private ScrollLabel label;
	    
	    /**
	     * First rect is stored for the region enter method 
	     */
	    private Rectangle m_firstRect=null;
	    
	    /**
	     * Pointer to the current menu point
	     */
	    private int m_menuPointer=0;
	    
	    /**
	     * Menu options
	     */
	    private String[] m_menuOptions={"Regression Calc","Steering Law","Fitts Law"};
	    
	    /**
	     * helper class to calculate the fitts law
	     */
	    private FittLaw fitt = null;
	    
	    public InTProject2() {   
	    }

	    /**
	     * Invoked when the application is initialized.  This happens once for an application instance.
	     */
	    public void initApp() {
	        this.display = this.context.getDisplay();
	        this.label = new ScrollLabel();
	        
	    	Log.setLogger(this.logger);	// set the logger so the helper will have access if needed
	        this.tapAndPlayHelper = new CalculationHelper(context);
	        this.fitt= new FittLaw();
	    }
	    
	    /**
	     * Invoked each time the penlet is activated.  Only one penlet is active at any given time.
	     */
	    public void activateApp(int reason, Object[] args) {

	    	if (reason == Penlet.ACTIVATED_BY_MENU) {
	    		this.label.draw(m_menuOptions[m_menuPointer]);
	    		this.display.setCurrent(this.label);
	    		
	        }
	    	else
	    		m_menuPointer = 0;
	    	
	        this.context.addPenTipListener(this);        
	        this.context.addStrokeListener(this);
	        this.context.addRegionEnterExitListener(this);
			mStartPoint = new Point();
	
	    }
	    
	    /**
	     * Invoked when the application is deactivated.
	     */
	    public void deactivateApp(int reason) {
			context.removePenTipListener(this);        
			context.removeStrokeListener(this);
			context.removeRegionEnterExitListener(this);
	    }
	    
	    /**
	     * Invoked when the application is destroyed.  This happens once for an application instance.  
	     * No other methods will be invoked on the instance after destroyApp is called.
	     */
	    public void destroyApp() {
	    }

		public void penUp(long time, Region region, PageInstance page) {
			if(m_menuPointer==0)
			{
				if (!penDownEventDelegator(time, region, page)) {
					/*
					 * Calculate Fitts law here. 
					 */
	
			        if(region.getAreaId() == 22 )
			        {
			        	mFinishTime = time;
			        	return;
			        }
			        
					mFinishRectangle = getContext().getCurrentRegionCollection().getBoundingBox(region);		
	
					if(mFinishRectangle.getX()+mFinishRectangle.getWidth()/2 == mStartPoint.getX() 
							&& mFinishRectangle.getY()+mFinishRectangle.getHeight()/2 == mStartPoint.getY())
						return; //The target rectangle is the same as the starting rectangle. Ignore!
					
			        tapAndPlayHelper.processFittsCalc(mStartPoint, mFinishRectangle, mStartTime, time);
	
				}
			}
		}

		public void penDown(long time, Region region, PageInstance page) {
			if(m_menuPointer==0)
			{
				mStartTime = time;
		        if (!penDownEventDelegator(time, region, page)) {
					
					if(tapAndPlayHelper==null)
						return;
					if(region.getAreaId()==22)
					{ //We have to calculate the steering law. Don't do this here. It is inside the strokecreated listener
					}
					else
					{
						/*
						 * Calculate the Fitts law here. But first store the starting point 
						 */
						Rectangle r = getContext().getCurrentRegionCollection().getBoundingBox(region);		
				        mStartPoint.setX(r.getX()+r.getWidth()/2);
				        mStartPoint.setY(r.getY()+r.getHeight()/2);
					}
				}
			}
		}

		public void singleTap(long time, int x, int y) {
			if(m_menuPointer==2)
			{
				this.logger.info("doubleTap(x = " + x + ", y = " + y + ")");
		         Point cursor = new Point();
		         cursor.setX(x);
		         cursor.setY(y);
		         fitt.setCursor(cursor);
			}
		}

		public void doubleTap(long time, int x, int y) {
			
		}

		// *** GENERATED METHOD -- DO NOT MODIFY ***
		/**
		 * Responsible for delegating generic PEN_DOWN events to area specific pen down methods based upon the area in which
		 * they occurred. This method is generated and managed by the penlet project nature. Users should modify the
		 * individual area event handlers (e.g. on<AreaName>PenDown) or the generic event handler (e.g. penDown)).
		
		 * @param time The time at which the event occurred
		 * @param region The region in which the event occurred
		 * @param page The page on which the event occurred
		 * @return true if the event was successfully handled, false otherwise
		 */
		protected boolean penDownEventDelegator(long time, Region region, PageInstance page) {
			boolean eventHandled = true;
			switch (region.getAreaId()) {
			case 26:
				eventHandled = onArea26PenDown(time, region, page);
				break;
			case 27:
				eventHandled = onArea27PenDown(time, region, page);
				break;
			default:
				eventHandled = false;
			}
			return eventHandled;
		}
		// *** END OF GENERATED CODE ***

		
		/**
		 * Stroke was created. Check if it is a steering control and calculate the values
		 */
		public void strokeCreated(long time, Region region,
				PageInstance pageInstance) {
			
			if(m_menuPointer==0) //Regression Calculation
			{
				if(region.getAreaId()==22)
				{
					Rectangle rect = getContext().getCurrentRegionCollection().getBoundingBox(region);		
	
					StrokeStorage strokeStorage = new StrokeStorage(pageInstance);
					// Get the stroke that was just created
					Stroke stroke = strokeStorage.getStroke(time);
					
					Rectangle boundingBox = stroke.getBoundingBox();
					if(rect.getBoundingBox().contains(boundingBox.getX(), boundingBox.getY()))
					{
						if(stroke.getBoundingBox().getX()<boundingBox.getX()+20 && 
								stroke.getBoundingBox().getY()<boundingBox.getY()+boundingBox.getHeight() &&
								stroke.getBoundingBox().getX()+stroke.getBoundingBox().getWidth()> boundingBox.getX()+boundingBox.getWidth()-20 &&
								stroke.getBoundingBox().getY()+stroke.getBoundingBox().getHeight()>boundingBox.getY()+boundingBox.getHeight()-20)
						{
							tapAndPlayHelper.processSteeringCalc(rect, mStartTime, mFinishTime);
							
						}
						else
						{ //Stroke was not inside the correct area
							tapAndPlayHelper.showText("Wrong Steering Calc ");
						}
					}
					
				}
			}
			else if(m_menuPointer==1||m_menuPointer==2) //Steering Law or Fitts Law
			{
				
				// If this is a stroke in a region we already created we'll skip it.  If this is a stroke in an area
		         // we haven't seen before it will have an areaId of 0.
		         if (region.getAreaId() > 0) return;

		         // Get the strokes for the page on which this new stroke was written
		         StrokeStorage strokeStorage = new StrokeStorage(pageInstance);

		         // Get the stroke that was just created
		         Stroke stroke = strokeStorage.getStroke(time);

		         Rectangle boundingBox = stroke.getBoundingBox();
		         if(boundingBox.getSize()>0) //only create areas if their size is greater than 0, so no points will be converted into areas
		         {
			         // Get the regions currently on this page
			         RegionCollection regions = this.context.getCurrentRegionCollection();
			
			         // Create a Region and add it to the document on the pen
			         // Note:  We always use the same area id because the function each region is to perform (telling us
			         // where it is on the paper) is the same.  The regions will still have unique IDs however because
			         // the Z-Order is always unique.
			         Region newRegion = new Region(AREA_ID, false);
			         regions.addRegion(boundingBox, newRegion);
			         //m_firstRect=null;//clearing
			         
		         }
			}
		}

		/**
		 * Listener for the calculate button
		 * @param time
		 * @param region
		 * @param page
		 * @return
		 */
		protected boolean onArea26PenDown(long time, Region region, PageInstance page) {
			tapAndPlayHelper.calculateRegLine();
			return true;
		}

		/**
		 * Listener for the Save button
		 * @param time
		 * @param region
		 * @param page
		 * @return
		 */
		protected boolean onArea27PenDown(long time, Region region, PageInstance page) {
			tapAndPlayHelper.storeData();
			return true;
		}

		/**
		 * corrects the pointer that it won't go out of range
		 * @param _pointer the pointer
		 * @param _min the minimal value that can be reached
		 * @param _max the maximal value that can be reached
		 * @return the corrected pointer
		 */
		public int correctPointer(int _pointer, int _min, int _max)
		{
			if(_pointer<_min)
			{
				_pointer=_max;
			}
			if(_pointer>_max)
			{
				_pointer=_min;
			}
			return _pointer;
		}

		/**
		 * Menu event occurred
		 */
		public boolean handleMenuEvent(MenuEvent _event) {
			if(_event.equals(MenuEvent.Down))
			{
				m_menuPointer--;
				m_menuPointer=correctPointer(m_menuPointer,0,m_menuOptions.length-1);
				
			}
			else if(_event.equals(MenuEvent.Up))
			{
				m_menuPointer++;
				m_menuPointer=correctPointer(m_menuPointer,0,m_menuOptions.length-1);
			}
			this.label.draw(m_menuOptions[m_menuPointer]);
			return false;
		}

		
		public void regionEnter(long time, Region region, PageInstance page) {
			int areaId = region.getAreaId();
	         this.logger.info("regionEnter(region = " + region.getIdString() + ", areaId = " + region.getAreaId() + ")");
	         if (areaId > 0) {
	             RegionCollection regions = this.context.getCurrentRegionCollection();
	             Rectangle boundingBox = regions.getBoundingBox(region);
	             if(m_menuPointer==1)
	             {
	            	 
	            	
	            	 //saveData();
		             this.label.draw("time: "+calculateSteering(boundingBox,m_firstRect,tapAndPlayHelper.getA(),tapAndPlayHelper.getB())+" ms");
		             if(m_firstRect==null)
		             {
		            	 m_firstRect=boundingBox; //set the previous rectangle
		             }
		             else
		             {
		            	 if(!m_firstRect.equals(boundingBox))
		            	 {
		            		 m_firstRect=null; //only reset the previous rectangle if the current wasn't pressed more than once
		            	 }
		             }
	             }
	             else if(m_menuPointer==2)
	             {
	            	 
	            	if(fitt.getCursor()!=null){ 
	            		fitt.setTarget(boundingBox);
	            	}else{
	            		if(fitt.getBase()==null){
	            			
	            			fitt.setBase(boundingBox);
	            		}else{
	            			fitt.setTarget(boundingBox);
	            		}
	            	}
	            	 
	            	 if(fitt.isOneRectangleOnePointSelected()){
	            		 this.label.draw("MT: " + fitt.calculate() + " ms");
	            	 }if(fitt.isTwoRectangleSelected()){
	            		 this.label.draw("MTR: " + fitt.calculateFitt() + " ms");
	            	 }
	             }
	         } 
			
		}

		
		public void regionExit(long time, Region region, PageInstance page) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * calculates the time for the steering law, it either works with one rectangle or with two
		 * @param _currentRect the current rectangle for the calculation
		 * @param _previosRect a possible previous selection, null if not available
		 * @param _a the a coefficient for the calculation
		 * @param _b the b coefficient for the calculation
		 * @return the time in ms
		 */
		public int calculateSteering(Rectangle _currentRect, Rectangle _previousRect, double _a, double _b)
		{
			int width=0;
	        int height=0;
	        
	        if(_previousRect==null || _previousRect.equals(_currentRect)) //check if there is only one rectangle
	        {
	       	 width=_currentRect.getWidth();
	       	 height=_currentRect.getHeight();
	        }
	        else
	        {
	       	 Rectangle upper=null;
	       	 Rectangle lower=null;
	       	 if(_previousRect.getY()<_currentRect.getY()) //find which rectangle is above the other
	       	 {
	       		 upper=_currentRect;
	       		 lower=_previousRect;
	       		 
	       	 }
	       	 else
	       	 {
	       		 upper=_previousRect;
	       		 lower=_currentRect;
	       	 }
	       	 height=upper.getY()-(lower.getY()+lower.getHeight());
	       	 width=lower.getWidth();
	        }
	        //steering law calculation
			double id= (double)width/(double)height;
			return (int) (_a+_b*id);
		}
		
		public boolean canProcessOpenPaperEvents() {
	        return true;
	    }
		
}
