/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.xml.util;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.enablement.oda.xml.util.IXMLSource;
import org.eclipse.datatools.enablement.oda.xml.util.RelationInformation;
import org.eclipse.datatools.enablement.oda.xml.util.SaxParserConsumer;

/**
 * This is a helper class used by SaxParserConsumer to generate nested xml
 * columns related infomation.
 * 
 * @deprecated Please use DTP xml driver
 */
public class SaxParserComplexNestedQueryHelper
		extends org.eclipse.datatools.enablement.oda.xml.util.SaxParserNestedQueryHelper {

	/**
	 * 
	 * @param consumer
	 * @param rinfo
	 * @param xdis
	 * @param tName
	 */
	SaxParserComplexNestedQueryHelper(SaxParserConsumer consumer, RelationInformation rinfo, IXMLSource xmlSource,
			String tName) throws OdaException {
		super(consumer, rinfo, xmlSource, tName);
	}
}
