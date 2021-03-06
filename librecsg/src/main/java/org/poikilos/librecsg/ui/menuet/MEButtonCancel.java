package org.poikilos.librecsg.ui.menuet;

import org.poikilos.librecsg.backend.data.utilities.ImageUtils;
import org.poikilos.librecsg.backend.global.AvoColors;
import org.poikilos.librecsg.ui.tools.ToolView;


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

/**
 * standardized Menuet Button for "CANCEL"
 */
public class MEButtonCancel extends MEButton{

	public final static int preferredHeight = 25;

	/**
	 * standardized Menuet Button for "CANCEL"
	 */
	public MEButtonCancel(Menuet menuet, int mode, ToolView toolView) {
		super(menuet, mode, toolView, false);
		this.mePreferredHeight = preferredHeight;
		this.minButtonHeight   = preferredHeight;
		this.meColorMouseOver  = AvoColors.COLOR_MENUET_CNCL_MO;
		this.meColorUnselected = AvoColors.COLOR_MENUET_CNCL_US;
		this.meLabel = "Cancel";
		this.meIcon = ImageUtils.getIcon("./menuet/Done.png", 24, 24);
		this.meDispOptions = MenuetElement.ME_TEXT_ONLY;;
	}

}
