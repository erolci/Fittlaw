package at.fh.ooe.mcm.it.pro2.regression;

public class RegressionPoint {
	private double m_id=0;
	private double m_mt=0;
	
	/**
	 * default constructor
	 */
	public RegressionPoint()
	{
		m_id=0;
		m_mt=0;
	}
	/**
	 * constructor
	 * @param _id idex of difficulty in bit
	 * @param _mt movement time in ms
	 */
	public RegressionPoint(double _id, double _mt)
	{
		m_id=_id;
		m_mt=_mt;
	}
	/**
	 * sets the ID 
	 * @param _id idex of difficulty in bit
	 */
	public void setID(double _id)
	{
		m_id=_id;
	}
	/**
	 * returns the ID
	 * @return index of difficulty in bit
	 */
	public double getID()
	{
		return m_id;
	}
	/**
	 * sets the MT
	 * @param _mt movement time in ms
	 */
	public void setMT(double _mt)
	{
		m_mt=_mt;
	}
	/**
	 * returns the MT
	 * @return movement time in ms
	 */
	public double getMT()
	{
		return m_mt;
	}
}
