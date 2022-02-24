/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * add comment here
 * 
 */
public class EditorRulerEditPartFactory implements EditPartFactory {

	protected GraphicalViewer diagramViewer;

	/**
	 * @param primaryViewer
	 */
	public EditorRulerEditPartFactory(GraphicalViewer primaryViewer) {
		diagramViewer = primaryViewer;
	}

	protected EditPart createRulerEditPart(EditPart parentEditPart, Object model) {
		return new EditorRulerEditPart(model);
	}

	protected EditPart createGuideEditPart(EditPart parentEditPart, Object model) {
		if (model instanceof DragEditorGuide) {
			return new DragEditorGuideEditPart(model);
		}
		return new EditorGuideEditPart(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart parentEditPart, Object model) {
		// the model can be null when the contents of the root edit part are set
		// to null
		EditPart part = null;
		if (isRuler(model)) {
			part = createRulerEditPart(parentEditPart, model);
		} else if (model != null) {
			part = createGuideEditPart(parentEditPart, model);
		}
		return part;
	}

	/**
	 * @return
	 */
	protected Object getHorizontalRuler() {
		Object ruler = null;
		RulerProvider provider = (RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER);
		if (provider != null) {
			ruler = provider.getRuler();
		}
		return ruler;
	}

	/**
	 * @return
	 */
	protected Object getVerticalRuler() {
		Object ruler = null;
		RulerProvider provider = (RulerProvider) diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER);
		if (provider != null) {
			ruler = provider.getRuler();
		}
		return ruler;
	}

	/**
	 * @param model
	 * @return
	 */
	protected boolean isRuler(Object model) {
		boolean result = false;
		if (model != null) {
			result = model == getHorizontalRuler() || model == getVerticalRuler();
		}
		return result;
	}
}
