package org.poikilos.librecsg.backend.model;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
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
public interface SubPart {

	/**
	 * @return the unique ID of this SubPart.
	 */
	abstract int getUniqueID();


	/**
	 * @return the Sketch if this SubPart is one, or null otherwise.
	 */
	abstract Sketch getSketch();

	/**
	 * @return the Feature2D3D if this SubPart is one, or null otherwise.
	 */
	abstract Build getBuild();

	/**
	 * @return the Feature3D3D if this SubPart is one, or null otherwise.
	 */
	abstract Modify getModify();

	/**
	 * @return the Part under which this SubPart was created.
	 */
	abstract public Part getParentPart();


}
