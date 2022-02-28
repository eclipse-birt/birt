/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import java.io.UnsupportedEncodingException;

import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * The structure handle of the Oda designer state.
 */

public class OdaDesignerStateHandle extends StructureHandle {

	/**
	 * Constructs the handle of Oda Designer state.
	 *
	 * @param elementHandle the oda data set element.
	 * @param context       context to the structure.
	 */

	public OdaDesignerStateHandle(DesignElementHandle elementHandle, StructureContext context) {
		super(elementHandle, context);
	}

	/**
	 * Constructs the handle of Oda Designer state.
	 *
	 * @param elementHandle the oda data set element.
	 * @param context       context to the structure
	 * @deprecated
	 */

	@Deprecated
	public OdaDesignerStateHandle(DesignElementHandle elementHandle, MemberRef context) {
		super(elementHandle, context);
	}

	/**
	 * Returns the version of the designer state's data.
	 *
	 * @return the version of the designer state's data.
	 */

	public String getVersion() {
		return getStringProperty(OdaDesignerState.VERSION_MEMBER);
	}

	/**
	 * Sets the version of the designer state's data.
	 *
	 * @param version the version of the designer state's data.
	 */

	public void setVersion(String version) {
		setPropertySilently(OdaDesignerState.VERSION_MEMBER, version);
	}

	/**
	 * Returns the content data as byte arrays.
	 *
	 * @return the content data as byte arrays.
	 */

	public byte[] getContentAsBlob() {
		OdaDesignerState state = (OdaDesignerState) getStructure();
		return state.getContentAsBlob();
	}

	/**
	 * Sets the content data as byte arrays.
	 *
	 * @param contentBlob the content data as byte arrays.
	 */

	public void setContentAsBlob(byte[] contentBlob) {
		if (contentBlob == null) {
			setPropertySilently(OdaDesignerState.CONTENT_AS_BLOB_MEMBER, null);
			return;
		}

		try {
			setPropertySilently(OdaDesignerState.CONTENT_AS_BLOB_MEMBER,
					new String(contentBlob, OdaDesignerState.CHARSET));
		} catch (UnsupportedEncodingException e) {
			// Should not fail

			assert false;
		}
	}

	/**
	 * Returns the content data as string.
	 *
	 * @return the content data as string.
	 */

	public String getContentAsString() {
		return getStringProperty(OdaDesignerState.CONTENT_AS_STRING_MEMBER);

	}

	/**
	 * Sets the content data as string.
	 *
	 * @param contentString the content data as string.
	 */

	public void setContentAsString(String contentString) {
		setPropertySilently(OdaDesignerState.CONTENT_AS_STRING_MEMBER, contentString);
	}
}
