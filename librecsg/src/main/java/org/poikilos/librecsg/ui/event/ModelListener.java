package org.poikilos.librecsg.ui.event;

import java.util.Observable;
import java.util.Observer;


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
public abstract class ModelListener implements Observer{

	public void update(Observable o, Object arg) {
		if(arg.equals(ModelEventHandler.ELEMENT_ADDED)){
			elementAdded();
		}
		if(arg.equals(ModelEventHandler.ELEMENT_REMOVED)){
			elementAdded();
		}
		if(arg.equals(ModelEventHandler.ACTIVE_ELEMENT_CHANGED)){
			elementAdded();
		}
	}

	public abstract void elementAdded();

	public abstract void elementRemoved();

	public abstract void activeElementChanged();

}
