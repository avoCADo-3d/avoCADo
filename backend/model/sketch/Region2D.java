package backend.model.sketch;

import java.util.LinkedList;

import backend.adt.Point2D;
import backend.geometry.Geometry2D;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Vertex;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
//
//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class Region2D implements Comparable{

	private Prim2DCycle prim2DCycle = new Prim2DCycle();
	
	public Region2D(Prim2DCycle cycle){
		this.prim2DCycle = cycle;
	}
	
	public double getRegionArea(){
		// TODO: Big HACK.. only considering 3-sided regions.
		if(prim2DCycle.size() == 3){
			// area of triangle = 0.5*base*height
			// base = prim2DCycle.get(0);
			Prim2D base = prim2DCycle.get(0);
			Point2D otherVert = prim2DCycle.get(1).hasPtGetOther(base.ptB);
			if(otherVert == null){
				System.out.println("error getting triangle area.. ??");
				return 0.0;
			}
			double height = Geometry2D.distFromLineSeg(base.ptA, base.ptB, otherVert);
			return 0.5*base.ptA.computeDist(base.ptB)*height;
		}
		return 0.0;
	}
	
	public boolean regionContainsPoint2D(Point2D pt){
		// TODO: Big HACK.. only considering 3-sided regions
		if(prim2DCycle.size() == 3){
			// consider barycentric coordinates to determine if point is inside triangle.
			Prim2D base = prim2DCycle.get(0);
			Point2D pt1 = base.ptA;
			Point2D pt2 = base.ptB;
			Point2D pt3 = prim2DCycle.get(1).hasPtGetOther(base.ptB);
			
			double A = pt1.getX()-pt3.getX();
			double B = pt2.getX()-pt3.getX();
			double C = pt3.getX()-pt.getX();
			double D = pt1.getY()-pt3.getY();
			double E = pt2.getY()-pt3.getY();
			double F = pt3.getY()-pt.getY();
			double G =  0.0; // pt1.getZ()-pt3.getZ(); // for 3D point
			double H =  0.0; // pt2.getZ()-pt3.getZ(); // for 3D point
			double I =  0.0; // pt3.getZ()-pt.getZ();  // for 3D point
			
			double lambda1 = (B*(F+I) - C*(E+H)) / (A*(E+H) - B*(D+G));
			double lambda2 = (A*(F+I) - C*(D+G)) / (B*(D+G) - A*(E+H));
			double lambda3 = 1.0 - lambda1 - lambda2;
			
			// pt is in Triangle ptA,ptB,ptC iff: lambda1 && lambda2 && lambda3 are all 0 < lambda < 1
			if(lambda1 > 0.0 && lambda1 < 1.0 && lambda2 > 0.0 && lambda2 < 1.0 && lambda3 > 0.0 && lambda3 < 1.0){
				return true;
			}
			
		}		
		return false;
	}

	
	public int compareTo(Object o) {
		if(o instanceof Region2D){
			Region2D regionB = (Region2D)o;
			if(regionB.getRegionArea() > this.getRegionArea()){
				return -1;
			}
			if(regionB.getRegionArea() < this.getRegionArea()){
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * generate and return a list of triangles that can be 
	 * used to fill the region when drawing or used otherwise
	 * to compute the total area of the region.
	 * @return a list of verticies (3*n) specifying triangles that comprise the region2D.
	 */
	public Point2DList getPoint2DListTriangles(){
		// TODO: cache this, perhaps?
		Point2DList p2DList = new Point2DList();
		// TODO: HACK, just for triangular regions...
		if(prim2DCycle.size() == 3){
			Point2D ptA = prim2DCycle.get(0).ptA;
			Point2D ptB = prim2DCycle.get(0).ptB;
			Point2D ptC = prim2DCycle.get(1).hasPtGetOther(ptB);
			if(prim2DCycle.isCCW()){
				p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
				p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
				p2DList.add(new Point2D(ptC.getX(), ptC.getY()));
			}else{
				p2DList.add(new Point2D(ptC.getX(), ptC.getY()));				
				p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
				p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
			}	
		}
		return p2DList;
	}
	
	/**
	 * generate and return a list of point2D pairs that specify
	 * the region's outline.
	 * @return a point2DList of the regions defining points, in pairs.
	 */
	public Point2DList getPoint2DListEdges(){
		// TODO: HACK, doesn't take into account faces that may be on the inside of an object (drilled hole)
		Point2DList p2DList = new Point2DList();
		Point2D conPt = new Point2D(0.0, 0.0);
		if(prim2DCycle.size() > 0){
			conPt = prim2DCycle.getFirst().ptA;
		}
		for(Prim2D prim : prim2DCycle){
			p2DList.add(conPt);
			conPt = prim.hasPtGetOther(conPt);
			p2DList.add(conPt);
		}		
		return p2DList;
	}
	
	/**
	 * generate and return a list of point2D quads that specify
	 * the region's outline. (a,b,b,a) for each line segment.
	 * @return a point2DList of the regions defining points, in quads.
	 */
	public Point2DList getPoint2DListEdgeQuad(){
		// TODO: HACK, doesn't take into account faces that may be on the inside of an object (drilled hole)
		Point2DList p2DList = new Point2DList();
		Point2D conPt = new Point2D(0.0, 0.0);
		if(prim2DCycle.size() > 0){
			conPt = prim2DCycle.getFirst().ptA;
		}
		for(Prim2D prim : prim2DCycle){
			Point2D lastPt = conPt;
			p2DList.add(conPt);
			conPt = prim.hasPtGetOther(conPt);
			p2DList.add(conPt);
			p2DList.add(conPt);
			p2DList.add(lastPt);
		}		
		return p2DList;
	}
	
	public CSG_Face getCSG_Face(){
		CSG_Face face = null;
		
		LinkedList<Point2D> pointList = new LinkedList<Point2D>();
		for(Prim2D prim : prim2DCycle){
			if(pointList.size() <= 0){
				pointList.add(prim.ptA);
				pointList.add(prim.ptB);
			}else{
				pointList.add(prim.hasPtGetOther(pointList.getLast()));
			}			
		}
		if(pointList.getFirst().getX() != pointList.getFirst().getX() || 
				pointList.getFirst().getY() != pointList.getFirst().getY()){
			// Start and end were not the same!
			System.out.println("Region2D(getCSG_Face): Invalid cycle.. start and end were not the same point!");
			return null;
		}
		if(pointList.size() <= 3){
			System.out.println("Region2D(getCSG_Face): Invalid cycle.. Not enough points in list!");
			return null;
		}
		
		pointList.removeLast(); // last point is a repeat of the first.
		
		//
		// pseudo-code for "convexize polygon" method
		// 
		// while(at least 3 points to consider and index < numberOfPoints)
		//   ptA,ptB,ptC starting at index%numberOfPoints
		//   if(angle{ABC} > 0)
		//     construct potential polygon(A,B,C)
		//     if(poly doesn't overlap/contain any other point)
		//       for(every remaining pointD)
		//         if(angle{poly.LastLastPt, poly.LastPt, pointD} > 0 && 
		//            angle{poly.LastPt, pointD, poly.firstPoint} > 0 &&
		//            angle{pointD, poly.firstPoint, poly.secPoint} > 0)
		//           if(poly doesn't contains any other point)
		//             poly.add(ptD);
		//           else
		//             break;
		//         else
		//           break;
		//       face.addPoly(poly);
		//       remove all of poly's middle points from pointList
		//       index = 0;
		//     else
		//       index++
		//   else
		//     index++;
		//   
		
		
		double TOL = 1e-10;
		LinkedList<Point2D> polyPoints = new LinkedList<Point2D>();
		int index = 0;
		while(pointList.size() >= 3 && index < pointList.size()){
			//System.out.println("starting; Index: " + index + ", pointList.size():" + pointList.size());
			polyPoints.clear();
			
			int listSize = pointList.size();
			Point2D ptA = pointList.get(index%listSize);
			Point2D ptB = pointList.get((index+1)%listSize);
			Point2D ptC = pointList.get((index+2)%listSize);
			polyPoints.add(ptA);
			polyPoints.add(ptB);
			polyPoints.add(ptC);
			
			double angle = Geometry2D.threePtAngle(ptA, ptB, ptC);
			if(angle > TOL){ // points going clockwise
				CSG_Polygon poly = new CSG_Polygon(new CSG_Vertex(ptA, 0.0), new CSG_Vertex(ptB, 0.0), new CSG_Vertex(ptC, 0.0));
				if(!polygonContainPoints(poly, pointList, polyPoints)){
					int indexStart = index;
					for(int i=index; i-indexStart < (listSize-3); i++){
						Point2D ptD = pointList.get((i+3)%listSize);
						CSG_Vertex vertLastLast = poly.getVertAtModIndex(poly.getNumberVertices()-2);
						CSG_Vertex vertLast = poly.getVertAtModIndex(poly.getNumberVertices()-1);
						Point2D ptLastLast = new Point2D(vertLastLast.getX(), vertLastLast.getY());
						Point2D ptLast = new Point2D(vertLast.getX(), vertLast.getY());
						double angleA = Geometry2D.threePtAngle(ptLastLast, ptLast, ptD);
						double angleB = Geometry2D.threePtAngle(ptLast, ptD, ptA);
						double angleC = Geometry2D.threePtAngle(ptD, ptA, ptB);
						if(angleA > TOL && angleB > TOL && angleC > TOL){			
							CSG_Polygon polyCopy = poly.deepCopy();
							polyCopy.addVertex(new CSG_Vertex(ptD, 0.0));
							if(!polygonContainPoints(polyCopy, pointList, polyPoints)){
								poly.addVertex(new CSG_Vertex(ptD, 0.0));
								polyPoints.add(ptD);
							}else{
								// Polygon contained another vertex in the list!
								break;
							}							
						}else{
							// Angle was negative (non-convex)
							break;
						}
					}
					System.out.println("adding polygon: " + poly);
					if(face == null){
						face = new CSG_Face(poly);
					}else{
						face.addPolygon(poly);
					}
					//  remove all polygon's midpoints from the pointList
					polyPoints.removeLast();
					polyPoints.removeFirst();
					for(Point2D p : polyPoints){
						pointList.remove(p);
					}
					index = 0;
				}else{
					index++;
				}
			}else{
				index++;
			}
			
			//System.out.println("ending; Index: " + index + ", pointList.size():" + pointList.size());
		} // end while loop

		return face;
	}
	
	private boolean polygonContainPoints(CSG_Polygon poly, LinkedList<Point2D> pointList, LinkedList<Point2D> invalidPoints){
		boolean polyOverlapsOtherPoints = false;
		for(Point2D point : pointList){
			if(!invalidPoints.contains(point) && poly.vertexIsInsidePolygon(new CSG_Vertex(point, 0.0))){
				polyOverlapsOtherPoints = true;
			}
		}
		return polyOverlapsOtherPoints;
	}

	
}
