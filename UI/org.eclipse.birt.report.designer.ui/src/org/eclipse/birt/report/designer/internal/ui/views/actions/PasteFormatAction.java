/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ContextSelectionAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class PasteFormatAction extends ContextSelectionAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.PasteFormatAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString("PasteFormatAction.text"); //$NON-NLS-1$

	public PasteFormatAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_TEXT);
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_PASTE_FORMAT));
	}

	protected boolean calculateEnabled() {
		return getSelectedObjects().size() == 1 && CopyFormatAction.getDesignElementHandle() != null;
	}

	public void run() {
		DesignElementHandle sourceElement = CopyFormatAction.getDesignElementHandle();
		if (sourceElement != null) {
			StyleHandle sourceStyle = sourceElement.getPrivateStyle();
			DesignElementHandle element = getSelectedElement();
			if (element != null) {
				element.getModuleHandle().getCommandStack().startTrans(ACTION_TEXT);
				Field[] fields = IStyleModel.class.getFields();
				for (int i = 0; i < fields.length; i++) {
					if ((fields[i].getModifiers() & Modifier.STATIC) != 0
							&& (fields[i].getModifiers() & Modifier.FINAL) != 0) {
						try {
							String propertyName = (String) fields[i].get(IStyleModel.class);
							if (sourceStyle.getProperty(propertyName) != null) {
								element.setProperty(propertyName, sourceStyle.getProperty(propertyName));
							}
						} catch (Exception e) {
							// ignore exception
						}
					}
				}
				element.getModuleHandle().getCommandStack().commit();
				// 212982
				// CopyFormatAction.publicElementFormat.dispose( );
				// CopyFormatAction.publicElementFormat = null;
			}
		}

	}

	private DesignElementHandle getSelectedElement() {
		Object object = getSelectedObjects().get(0);
		if (object instanceof ReportElementEditPart) {
			return (DesignElementHandle) ((ReportElementEditPart) object).getModel();
		}
		return null;
	}

}
