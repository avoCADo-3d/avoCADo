package backend.model.CSG;

import java.util.LinkedList;
import java.util.List;


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
* @created Mar. 2007
*/

/**
 * Constructive Solid Geometry :: Vertex<br/><br/>
 * 
 * A 3D point in space used as a stand-alone point, 
 * part of a line, or a node on a polygon/face.
 * Adjacent vertices are also stored in a list.<br/><br/> 
 * 
 * The CSG_Vertex also stores status information about
 * being on the inside/outside/boundary of another solid. 
 * Initially this status is set to unknown.
 */
public class CSG_Vertex {

	public final static int STATUS_INSIDE   = 5;
	public final static int STATUS_OUTSIDE  = 6;
	public final static int STATUS_BOUNDARY = 7;
	public final static int STATUS_UNKNOWN  = 8;
	
	final double x,y,z;	
	List<CSG_Vertex> adjacentVertices = new LinkedList<CSG_Vertex>();
	int status = STATUS_UNKNOWN;
	
	public CSG_Vertex(double x, double y, double z){
		this.x = x;
		this.y = y; 
		this.z = z;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
	
	
}