/* 
 * DISCLAIMER PLACEHOLDER 
 */
package com.ogprover.pp.tp.auxiliary;

import java.util.HashSet;

import com.ogprover.pp.tp.geoconstruction.FreePoint;
import com.ogprover.pp.tp.geoconstruction.Point;

/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for representing the Pythagoras difference between three points 
 * 		for the area method. If the three points are A, B and C, then this 
 * 		difference equals AB² + BC² - AC². Then, its value is 0 iff ABC is a
 * 		right angle.</dd>
 * </dl>
 */
public class AMPythagorasDifference extends AMExpression {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	
	/**
	 * Points which form the triangle
	 */
	protected Point a,b,c;

	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	public Point getA() {
		return a;
	}
	public Point getB() {
		return b;
	}
	public Point getC() {
		return c;
	}
	
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#getPoints()
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();
		points.add(a);
		points.add(b);
		points.add(c);
		return points;
	}
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param a		Point
	 * @param b		Point
	 * @param c		Point
	 */
	public AMPythagorasDifference(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	
	/*
	 * ======================================================================
	 * ======================= COMMON OBJECT METHODS ========================
	 * ======================================================================
	 */
	/**
	 * @see com.ogprover.pp.tp.auxiliary.AMExpression#toString()
	 */
	@Override
	public String print() {
		StringBuilder s = new StringBuilder();
		s.append("P_");
		s.append(a.getGeoObjectLabel());
		s.append(b.getGeoObjectLabel());
		s.append(c.getGeoObjectLabel());
		return s.toString();
	}
	
	@Override
	public boolean equals(AMExpression expr) {
		if (!(expr instanceof AMPythagorasDifference))
			return false;
		AMPythagorasDifference diff = (AMPythagorasDifference)expr;
		return (a.equals(diff.getA()) && b.equals(diff.getB()) && c.equals(diff.getC()));
	}
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	@Override
	public boolean containsOnlyFreePoints() {
		if (a instanceof FreePoint && b instanceof FreePoint && c instanceof FreePoint) {
			return true;
		}
		return false;
	}
	
	@Override
	public AMExpression uniformize() {
		if (c.compare(b) && b.compare(a)) {
			return new AMPythagorasDifference(c, b, a);
		}
		if (c.equals(a) && b.compare(a)) {
			return new AMPythagorasDifference(b, a, b);
		}
		return this;
	}
}