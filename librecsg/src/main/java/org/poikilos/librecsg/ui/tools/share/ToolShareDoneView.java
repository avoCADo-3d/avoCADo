package org.poikilos.librecsg.ui.tools.share;

import org.poikilos.librecsg.ui.menuet.MEButtonDone;
import org.poikilos.librecsg.ui.menuet.Menuet;
import org.poikilos.librecsg.ui.tools.ToolViewShare;


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
* @created Mar. 2007
*/
public class ToolShareDoneView extends ToolViewShare{

	public ToolShareDoneView(Menuet menuet){
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the Share mode.");

		this.applyToolGroupSettings();	// APPLY SKETCH GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_PROJECT);
	}

}
