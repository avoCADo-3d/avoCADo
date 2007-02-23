package ui.tools.DDtoDDD;

import javax.media.opengl.GL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import ui.menuet.Menuet;
import ui.tools.ToolInterface2D3D;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Feature2D3D;
import backend.model.Sketch;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Region2D;


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
public class Tool2D3DRevolveInt implements ToolInterface2D3D{

	public void draw3DFeature(GL gl, Feature2D3D feat2D3D) {
		// TODO Auto-generated method stub
	}

	public void finalize(ParamSet paramSet) {
		// finalize revolve and return to main menu
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				// TODO: only keep feature and consume sketch if selectionLists are all satisfied as well.
				sketch.isConsumed = true;
			}else{
				AvoGlobal.project.getActivePart().removeActiveSubPart();				
			}
			
			AvoGlobal.menuet.disableAllTools();
			AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
			AvoGlobal.paramDialog.setParamSet(null);
			AvoGlobal.menuet.currentTool = null;			
			AvoGlobal.menuet.updateToolModeDisplayed();
			AvoGlobal.glView.updateGLView = true;		
			
		}else{
			System.out.println("I have no idea what's going on?!?  the active feature2D3D was null!?!");
		}
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){	
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				ParamSet paramSet = feat2D3D.paramSet;
				if(!paramSetIsValid(paramSet)){
					// paramSet is not valid for this feature, create a new one.
					paramSet = new ParamSet("Revolve", this);
					paramSet.addParam("regions", new Param("Regions", new SelectionList()));
					paramSet.addParam("centerline", new Param("CenterLine", new SelectionList()));
					paramSet.addParam("angle", new Param("Angle", 360.0));
					paramSet.addParam("offset", new Param("OffsetAngle", 0.0));
					feat2D3D.paramSet = paramSet;
				}
				
				try{
					SelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					SelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					if(centerline.hasFocus){
						// try to select a 2D line for the centerline if clicked...
						for(int i = 0; i < sketch.getFeat2DListSize(); i++){
							Feature2D f2D = sketch.getAtIndex(i);
							for(Prim2D prim2D : f2D.prim2DList){
								// TODO: hack (hardcoded distance from line for selection.
								if(prim2D instanceof Prim2DLine && prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
									// TODO: somehow store index of the primative...(int feat2d, int prim2D)
									System.out.println("you selected a line.. I am just ignorant and don't know how to select it. :[");
									centerline.isSatisfied = true;
								}
							}
						}
					}else{
						// selectiong regions...
						if((e.stateMask & SWT.SHIFT) != 0){
							// shift is down
						}else{
							// shift is not down
							regions.clearList();
						}					
						
						Point2D clickedPoint = new Point2D(x,y);
						for(int i=0; i < sketch.getRegion2DListSize(); i++){
							Region2D reg  = sketch.getRegAtIndex(i);
							if(reg.regionContainsPoint2D(clickedPoint) && !regions.contains(String.valueOf(i))){
								regions.add(String.valueOf(i));
							}
						}
						if(regions.getSelectionSize() > 0){
							regions.isSatisfied = true;
						}else{
							regions.isSatisfied = false;
						}
						
						// TODO: shouldn't need to directly indicate param modified??
						AvoGlobal.paramEventHandler.notifyParamModified();
					}
				}catch(Exception ex){
					System.out.println("Revolve(mousedown): " + ex.getClass().getName());
				}
				
				AvoGlobal.paramDialog.setParamSet(feat2D3D.paramSet);
			}
		}		
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Revolve"
		// --------------------------------
		// # "regions"     ->  "Regions"     <SelectionList>
		// # "centerline"  ->  "CenterLine"  <SelectionList>
		// # "angle"       ->  "Angle"       <Double>
		// # "offset"      ->  "OffsetAngle" <Double>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Revolve" &&
							paramSet.hasParam("regions", PType.SelectionList) &&
							paramSet.hasParam("centerline", PType.SelectionList) &&
							paramSet.hasParam("angle", PType.Double) &&
							paramSet.hasParam("offset", PType.Double));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		// no derive parameters for this feature.	
	}

}