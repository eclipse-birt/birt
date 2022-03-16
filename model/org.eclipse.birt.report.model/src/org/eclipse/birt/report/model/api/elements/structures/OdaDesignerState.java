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

package org.eclipse.birt.report.model.api.elements.structures;

import java.io.UnsupportedEncodingException;

import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * The structure of the Oda designer state.
 */

public class OdaDesignerState extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "OdaDesignerState"; //$NON-NLS-1$

	/**
	 * Name of the "version" member. Version of the designer state's data format.
	 */

	public static final String VERSION_MEMBER = "version"; //$NON-NLS-1$

	/**
	 * Name of the "stateContentAsString" member. The data of the content as a
	 * string.
	 */

	public static final String CONTENT_AS_STRING_MEMBER = "stateContentAsString"; //$NON-NLS-1$

	/**
	 * Name of the "stateContentAsString" member. The data of the content as a byte
	 * array.
	 */

	public static final String CONTENT_AS_BLOB_MEMBER = "stateContentAsBlob"; //$NON-NLS-1$

	/**
	 * Encoding mode for the data member.
	 */

	public static final String CHARSET = "8859_1"; //$NON-NLS-1$

	/**
	 * Value of the "version" member.
	 */

	private String version = null;

	/**
	 * Value of the "contentAsString" member.
	 */

	private String contentAsString = null;

	/**
	 * Value of the "contentAsBLOB" member.
	 */

	private String contentAsBlob = null;

	/**
	 * Constructs the sort key with the key to sort and the direction.
	 *
	 * @param version
	 * @param contentAsString
	 * @param contentAsBlob
	 */

	public OdaDesignerState(String version, String contentAsString, String contentAsBlob) {
		this.version = version;
		this.contentAsBlob = contentAsBlob;
		this.contentAsString = contentAsString;
	}

	/**
	 * Default constructor.
	 *
	 */

	public OdaDesignerState() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (VERSION_MEMBER.equals(propName)) {
			return version;
		} else if (CONTENT_AS_BLOB_MEMBER.equals(propName)) {
			return contentAsBlob;
		} else if (CONTENT_AS_STRING_MEMBER.equals(propName)) {
			return contentAsString;
		} else {
			assert false;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (VERSION_MEMBER.equals(propName)) {
			version = (String) value;
		} else if (CONTENT_AS_BLOB_MEMBER.equals(propName)) {
			contentAsBlob = (String) value;
		} else if (CONTENT_AS_STRING_MEMBER.equals(propName)) {
			contentAsString = (String) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the version of the designer state's data.
	 *
	 * @return the version of the designer state's data.
	 */

	public String getVersion() {
		return (String) getProperty(null, VERSION_MEMBER);
	}

	/**
	 * Sets the version of the designer state's data.
	 *
	 * @param version the version of the designer state's data.
	 */

	public void setVersion(String version) {
		setProperty(VERSION_MEMBER, version);
	}

	/**
	 * Returns the content data as byte arrays.
	 *
	 * @return the content data as byte arrays.
	 */

	public byte[] getContentAsBlob() {
		if (contentAsBlob == null) {
			return null;
		}

		try {
			return contentAsBlob.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			assert false;
			return null;
		}
	}

	/**
	 * Sets the content data as byte arrays.
	 *
	 * @param contentBlob the content data as byte arrays.
	 */

	public void setContentAsBlob(byte[] contentBlob) {
		if (contentBlob != null) {
			try {
				this.contentAsBlob = new String(contentBlob, CHARSET);
			} catch (UnsupportedEncodingException e) {
				this.contentAsBlob = null;
				assert false;
			}
		} else {
			this.contentAsBlob = null;
		}
	}

	/**
	 * Returns the content data as string.
	 *
	 * @return the content data as string.
	 */

	public String getContentAsString() {
		return (String) getProperty(null, CONTENT_AS_STRING_MEMBER);

	}

	/**
	 * Sets the content data as string.
	 *
	 * @param contentString the content data as string.
	 */

	public void setContentAsString(String contentString) {
		setProperty(CONTENT_AS_STRING_MEMBER, contentString);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	/**
	 * Return an <code>FormatValueHandle</code> to deal with the string format.
	 *
	 * @param valueHandle the property or member handle
	 * @return the structure handle
	 *
	 */

	@Override
	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new OdaDesignerStateHandle(valueHandle.getElementHandle(), getContext());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	@Override
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		assert false;

		return null;
	}

}
