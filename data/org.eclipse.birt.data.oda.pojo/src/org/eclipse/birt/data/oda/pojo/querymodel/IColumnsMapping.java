/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.querymodel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A mapping that map a source to columns
 */
public interface IColumnsMapping {
	/**
	 * @return the mapping source
	 */
	IMappingSource getSource();

	/**
	 * 
	 * @param parent
	 * @return the corresponding reference node
	 */
	ReferenceNode createReferenceNode(RelayReferenceNode parent);

	Element createElement(Document doc);
}
