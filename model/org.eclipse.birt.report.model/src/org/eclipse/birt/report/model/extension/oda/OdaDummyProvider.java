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
	@Override
	public void checkExtends(DesignElement parent) throws ExtendsException {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getExtDefn()
	 */
	@Override
	public ExtensionElementDefn getExtDefn() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefn
	 * (java.lang.String)
	 */
	@Override
	public IPropertyDefn getPropertyDefn(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefns
	 * ()
	 */
	@Override
	public List<IElementPropertyDefn> getPropertyDefns() {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidExtensionID ()
	 */
	@Override
	public boolean isValidExtensionID() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#convertExtensionID ()
	 */
	@Override
	public String convertExtensionID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.treebuild.IContentHandler#getTree()
	 */

	@Override
	public ContentTree getContentTree() {
		return this.contentTree;
	}

	/**
	 * Initializes the content tree.
	 */

	private void initializeContentTree() {
		if (contentTree == null) {
			contentTree = new ContentTree();
		}
	}
}
