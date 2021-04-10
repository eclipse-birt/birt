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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerStateContent;

/**
 * An adapter class that converts between ROM OdaDesignerStateHandle and ODA
 * DesignerState.
 * 
 * @see OdaDesignerStateHandle
 * @see DesignerState
 */

class DesignerStateAdapter {

	/**
	 * Creates a ROM DesignerState object with the given ODA DataSet design.
	 * 
	 * @param designerState    the ODA designer state.
	 * @param romDesignerState the ROM designer state.
	 * @throws SemanticException if ROM Designer state value is locked.
	 */

	static void updateROMDesignerState(DesignerState designerState, ReportElementHandle reportElement)
			throws SemanticException {
		if (designerState == null || reportElement == null)
			return;

		CommandStack cmdStack = reportElement.getModuleHandle().getCommandStack();
		cmdStack.startTrans(null);

		OdaDesignerStateHandle romDesignerState = null;

		if (reportElement instanceof OdaDataSourceHandle) {
			romDesignerState = ((OdaDataSourceHandle) reportElement).getDesignerState();
		} else if (reportElement instanceof OdaDataSetHandle) {
			romDesignerState = ((OdaDataSetHandle) reportElement).getDesignerState();
		} else
			return;

		if (romDesignerState == null) {
			OdaDesignerState tmpDesignerState = StructureFactory.createOdaDesignerState();

			if (reportElement instanceof OdaDataSourceHandle) {
				romDesignerState = ((OdaDataSourceHandle) reportElement).setDesignerState(tmpDesignerState);
			} else if (reportElement instanceof OdaDataSetHandle) {
				romDesignerState = ((OdaDataSetHandle) reportElement).setDesignerState(tmpDesignerState);
			} else {
				assert false;
				return;
			}
		}

		romDesignerState.setVersion(designerState.getVersion());

		DesignerStateContent stateContent = designerState.getStateContent();
		if (stateContent == null)
			return;

		romDesignerState.setContentAsString(stateContent.getStateContentAsString());
		romDesignerState.setContentAsBlob(stateContent.getStateContentAsBlob());

		cmdStack.commit();
	}

	/**
	 * Creates a ODA DesignerState object with the given ROM designer state.
	 * 
	 * @param designerState the ROM designer state.
	 * @return the oda DesignerState object.
	 */

	static DesignerState createOdaDesignState(OdaDesignerStateHandle designerState) {
		if (designerState == null)
			return null;

		DesignerState odaState = ODADesignFactory.getFactory().createDesignerState();
		odaState.setVersion(designerState.getVersion());

		byte[] blobContent = designerState.getContentAsBlob();
		String stringContent = designerState.getContentAsString();
		if (blobContent == null && stringContent == null)
			return odaState;

		DesignerStateContent stateContent = ODADesignFactory.getFactory().createDesignerStateContent();
		if (blobContent != null)
			stateContent.setStateContentAsBlob(blobContent);
		if (stringContent != null)
			stateContent.setStateContentAsString(stringContent);
		odaState.setStateContent(stateContent);

		return odaState;

	}
}
