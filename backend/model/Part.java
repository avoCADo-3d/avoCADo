package backend.model;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import backend.global.AvoGlobal;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_BooleanOperator;
import backend.model.CSG.CSG_Plane;
import backend.model.CSG.CSG_Solid;
import backend.model.CSG.CSG_Vertex;
import backend.model.ref.ModRef_Plane;
import backend.model.ref.ModRef_PlaneFixed;
import backend.model.sketch.SketchPlane;


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
public class Part {

	protected List<SubPart> subPartList = new LinkedList<SubPart>();
	
	protected int activeSubPart = -1;
	
	protected Group group;
	public final int ID;
	private int subPartCounter = 1;
	
	private CSG_Vertex origin = new CSG_Vertex(0.0, 0.0, 0.0);
	private CSG_Vertex xAxis  = new CSG_Vertex(1.0, 0.0, 0.0);
	private CSG_Vertex yAxis  = new CSG_Vertex(0.0, 1.0, 0.0);
	private CSG_Vertex zAxis  = new CSG_Vertex(0.0, 0.0, 1.0);
	private final SketchPlane sketchPlaneXY = new SketchPlane(new CSG_Plane(zAxis, 0.0));
	private final SketchPlane sketchPlaneYZ = new SketchPlane(new CSG_Plane(xAxis, 0.0));
	private final SketchPlane sketchPlaneZX = new SketchPlane(new CSG_Plane(yAxis, 0.0));

	public final ModRef_Plane planeXY = new ModRef_PlaneFixed(sketchPlaneXY);
	public final ModRef_Plane planeYZ = new ModRef_PlaneFixed(sketchPlaneYZ);
	public final ModRef_Plane planeZX = new ModRef_PlaneFixed(sketchPlaneZX);
	
	private ModRef_Plane selectedPlane = null;
	
	private CSG_Solid partSolid = new CSG_Solid(); 
	
	
	public Part(Group group, int ID){
		this.group = group;
		this.ID = ID;
	}
	
	public Group getParentGroup(){
		return this.group;		
	}
	
	public void setSelectedPlane(ModRef_Plane selectedPlane){
		this.selectedPlane = selectedPlane;
	}
	
	public ModRef_Plane getSelectedPlane(){
		return selectedPlane;
	}
	
	public int addNewSketchOnSelectedPlane(){
		if(selectedPlane == null){
			// default plane if nothing is selected: XY plane
			selectedPlane = planeXY;
		}
		subPartList.add(new Sketch(this, subPartCounter++, selectedPlane));
		AvoGlobal.modelEventHandler.notifyElementAdded();
		int newIndex = subPartList.size()-1;
		activeSubPart = newIndex;
		return newIndex;
	}
	
	public int addNewFeat2D3D(int sketchID){
		if(sketchID > 0 && sketchID < subPartCounter && getSketchByID(sketchID) != null){
			subPartList.add(new Feature2D3D(this, sketchID, subPartCounter++));
			AvoGlobal.modelEventHandler.notifyElementAdded();
			return subPartList.size()-1;
		}else{
			System.out.println(" *** PART *** could not add a New Feature2D3D since sketchID was invalid! sketchID=" + sketchID);
			return -1;
		}
	}
	
	public int addNewFeat3D3D(){
		subPartList.add(new Feature3D3D(this, null, null, subPartCounter++));
		AvoGlobal.modelEventHandler.notifyElementAdded();
		return subPartList.size()-1;
	}
	
	/**
	 * get the SubPart at a give index
	 * @param i index
	 * @return the SubPart, or null if index was invalid.
	 */
	public SubPart getAtIndex(int i){
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return null;
		}
		return subPartList.get(i);
	}
	
	/**
	 * @return the size() of the list of SubParts.
	 */
	public int getSubPartListSize(){
		return subPartList.size();
	}
	
	/**
	 * @return true if a CSG_Solid exists yet for the part.
	 */
	public boolean solidExists(){
		if(partSolid.getNumberOfFaces() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Modify/Build on this part's current solid representation.
	 * @param solid the CSG_Solid to add/subtract/intersect with this part's existing solid
	 * @param boolop the boolean operation to apply. 
	 */
	public void updateSolid(CSG_Solid solid, BoolOp boolop){
		switch(boolop){
			case Union:{
				partSolid = CSG_BooleanOperator.Union(partSolid, solid);
				break;
			}
			case Intersection:{
				partSolid = CSG_BooleanOperator.Intersection(partSolid, solid);
				break;
			}
			case Subtracted:{
				partSolid = CSG_BooleanOperator.Subtraction(partSolid, solid);
				break;
			}
			case SubtractFrom:{
				partSolid = CSG_BooleanOperator.Subtraction(solid, partSolid);
				break;
			}
			default:{
				System.out.println("Part(updateSolid): Unknown boolean operation, aborting solid update.");
				break;
			}
		}
		AvoGlobal.glView.updateGLView = true;
	}
	
	public void glDrawSolid(GL gl){
		partSolid.glDrawSolid(gl);
	}
	
	// TODO: Hack!, don't actually pass the solid (but it's big so copying may be problematic...)
	public CSG_Solid getSolid(){
		return partSolid;
	}
	
	/**
	 * set the index of the SubPart that should be set to Active.
	 * @param i index
	 */
	public void setActiveSubPart(int i){
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return;
		}
		activeSubPart = i;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active SubPart to none
	 */
	public void setActiveToNone(){
		activeSubPart = -1;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active SubPart
	 * @return the active SubPart, or null if no SubPart is active
	 */
	public SubPart getActiveSubPart(){
		return this.getAtIndex(activeSubPart);
	}
	
	/**
	 * Remove the SubPart at the index if present.
	 * @param i index
	 */
	public void removeSubPartAtIndex(int i){
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return;
		}
		subPartList.remove(i);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * remove the active SubPart from the list.
	 */
	public void removeActiveSubPart(){
		removeSubPartAtIndex(activeSubPart);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * Get a sketch by its unique ID.
	 * @param id the ID of the sketch to retreive
	 * @return the sketch, if it exists, or null otherwise.
	 */
	public Sketch getSketchByID(int id){
		for(SubPart subPart : subPartList){
			Sketch sketch = subPart.getSketch();
			if(sketch != null && sketch.getID() == id){
				return sketch;
			}
		}
		System.out.println("Part(getSketchByID): there was no Sketch at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
	/**
	 * Get a Feature2D3D by its unique ID.
	 * @param id the ID of the Feature2D3D to retreive
	 * @return the Feature2D3D, if it exists, or null otherwise.
	 */
	public Feature2D3D getFeat2D3DByID(int id){
		for(SubPart subPart : subPartList){
			Feature2D3D feat2D3D = subPart.getFeature2D3D();
			if(feat2D3D != null && feat2D3D.ID == id){
				return feat2D3D;
			}
		}
		System.out.println("Part(getFeat2D3DByID): there was no Feat2D3D at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
	/**
	 * Get a Feature3D3D by its unique ID.
	 * @param id the ID of the Feature3D3D to retreive
	 * @return the Feature3D3D, if it exists, or null otherwise.
	 */
	public Feature3D3D getFeat3D3DByID(int id){
		for(SubPart subPart : subPartList){
			Feature3D3D feat3D3D = subPart.getFeature3D3D();
			if(feat3D3D != null && feat3D3D.ID == id){
				return feat3D3D;
			}
		}
		System.out.println("Part(getFeat3D3DByID): there was no Feat3D3D at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
}
