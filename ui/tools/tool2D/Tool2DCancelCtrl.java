package ui.tools.tool2D;

import org.eclipse.swt.events.MouseEvent;

import backend.global.AvoGlobal;

import ui.tools.ToolCtrl2D;

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
public class Tool2DCancelCtrl implements ToolCtrl2D {
	/**
	 * All of the tool's main controller functionality:
	 * (mouse handling, button clicking, etc.) 
	 */
	public Tool2DCancelCtrl() {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		// TODO: remove sketch when Cancel is pushed.
		//       this should also force the TreeViewer to rebuild itself.
		
		AvoGlobal.setActiveParamSet(null);					
		AvoGlobal.glView.updateGLView = true;
	}



}