package at.fh.ooe.mcm.it.pro2.regression;


public class RegressionLine {
	private RegressionPoint[] m_points=null;
	private double m_a=0;
	private double m_b=0;
	/**
	 * constructor
	 * @param _points an array with the points from which the line should be created
	 */
	public RegressionLine(RegressionPoint[] _points)
	{
		m_points=_points;
		m_a=0;
		m_b=0;
	}
	/**
	 * calculates the regression line
	 */
	public void calculateRegressionLine()
	{
		if (m_points == null)
			return;
		double sumX=0;
		double sumX2=0;
		double sumY=0;
		double sumXY=0;
		for(int i=0; i<m_points.length;i++)
		{
			sumX+=m_points[i].getID();
			sumX2+=(m_points[i].getID()*m_points[i].getID());
			sumY+=m_points[i].getMT();
			sumXY+=(m_points[i].getID()*m_points[i].getMT());
		}
		double sxx=sumX2-((sumX*sumX)/m_points.length);
		double sxy=sumXY-((sumX*sumY)/m_points.length);
		m_a=sxy/sxx;
		m_b=(sumY/m_points.length)-(m_a*(sumX/m_points.length));
	}
	/**
	 * returns the a coefficient
	 * @return
	 */
	public double getA()
	{
		return m_a;
	}
	/**
	 * returns the b coefficient
	 * @return
	 */
	public double getB()
	{
		return m_b;
	}

}
