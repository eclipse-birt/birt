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

package org.eclipse.birt.report.model.extension.oda;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentTree;
import org.eclipse.birt.report.model.parser.treebuild.IContentHandler;

/**
 * The dummmy provider to save property values if the ODA extension cannnot be
 * found.
 * 
 */

public class OdaDummyProvider implements ODAProvider, IContentHandler {

	private ContentTree contentTree = null;

	/**
	 * The default constructor.
	 * 
	 * @param extensionID the extension id
	 */

	public OdaDummyProvider(String extensionID) {
		initializeContentTree();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#checkExtends(
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void checkExtends(DesignElement parent) throws ExtendsException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getExtDefn()
	 */
	public ExtensionElementDefn getExtDefn() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefn
	 * (java.lang.String)
	 */
	public IPropertyDefn getPropertyDefn(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefns
	 * ()
	 */
	public List<IElementPropertyDefn> getPropertyDefns() {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidExtensionID ()
	 */
	public boolean isValidExtensionID() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#convertExtensionID ()
	 */
	public String convertExtensionID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.treebuild.IContentHandler#getTree()
	 */

	public ContentTree getContentTree() {
		return this.contentTree;
	}

	/**
	 * Initializes the content tree.
	 */

	private void initializeContentTree() {
		if (contentTree == null)
			contentTree = new ContentTree();
	}
}
