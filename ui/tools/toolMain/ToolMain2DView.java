package ui.tools.toolMain;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewMain;
import backend.data.utilities.ImageUtils;


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
//along with AvoCADo; if not, write to the Free Softwares
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class ToolMain2DView extends ToolViewMain{

	public ToolMain2DView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "2D";
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2D.png", 24, 24);
		mElement.setToolTipText("2D Sketch Mode");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_2D, new ToolMain2DCtrl());
	}

}