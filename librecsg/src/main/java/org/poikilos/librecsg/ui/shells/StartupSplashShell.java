package org.poikilos.librecsg.ui.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.poikilos.librecsg.backend.data.utilities.ImageUtils;


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
public class StartupSplashShell {

	public static Shell shell;

	/**
	 * create the startup splash shell and display it
	 * @param display
	 */
	public StartupSplashShell(Display display){
		shell = new Shell(display, SWT.NO_TRIM);

		setupShell(); 				// place components in the main avoCADo shell

		shell.setText("avoCADo");
		Label label = new Label(shell, SWT.NONE);
		label.setImage(ImageUtils.getIcon("./avoCADo-Splash.jpg", 360, 298));
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		FormData labelData = new FormData();
        labelData.right = new FormAttachment(100, 0);  // 100%
        labelData.bottom = new FormAttachment(100, 0);  // 100%
        label.setLayoutData(labelData);
		shell.setSize(360, 298);	//TODO: set initial size to last known size
		Rectangle b = display.getBounds();
		int xPos = Math.max(0, (b.width-360)/2);
		int yPos = Math.max(0, (b.height-298)/2);
		shell.setLocation(xPos, yPos);
		shell.setImage(ImageUtils.getIcon("./avoCADo.png", 32, 32));
		shell.open();

		// handle events while the shell is not disposed
		//while (!shell.isDisposed()) {
		//	if (!display.readAndDispatch())
		//		display.sleep();
		//}
	}

	public static void closeSplash(){
		if(shell != null && !shell.isDisposed()){
			shell.dispose();
		}
	}

	/**
	 * setup the shell
	 */
	void setupShell(){
		// --populate the splash shell--
	}

}
