/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.swt.graphics.Image;

public interface IDBNode {
	/**
	 * @return display name of node in the DB available items tree
	 */
	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	String getDisplayName(String metadataBidiFormatStr);

	/**
	 * @param useIdentifierQuoteString: whether use identifier quote string when
	 *                                  populating qualified name
	 * @return the full qualified name in a SQL text.
	 *         <p>
	 *         null if it can't be a part of SQL text.
	 */
	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	String getQualifiedNameInSQL(boolean useIdentifierQuoteString, boolean includeSchema, String metadataBidiFormatStr);

	/**
	 * @return image of node in the DB available items tree
	 */
	Image getImage();
}
