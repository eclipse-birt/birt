/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.internal.ui.rulers.RulerEditPartFactory;


/**
 * add comment here
 * 
 */
public class EditorRulerEditPartFactory extends RulerEditPartFactory
{

	/**
	 * @param primaryViewer
	 */
	public EditorRulerEditPartFactory( GraphicalViewer primaryViewer )
	{
		super( primaryViewer );
	}

	

	protected EditPart createRulerEditPart(EditPart parentEditPart, Object model) {
		return new EditorRulerEditPart(model);
	}
	
	protected EditPart createGuideEditPart(EditPart parentEditPart, Object model) {
		return new EditorGuideEditPart(model);
	}
}
