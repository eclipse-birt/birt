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

import org.eclipse.datatools.enablement.oda.xml.util.RelationInformation;
import org.eclipse.datatools.enablement.oda.xml.util.SaxParserConsumer;
import org.eclipse.datatools.enablement.oda.xml.util.XMLDataInputStream;


/**
 * This is a helper class used by SaxParserConsumer to generate nested xml columns related
 * infomation.
 * @deprecated Please use DTP xml driver
 */
public class SaxParserComplexNestedQueryHelper extends org.eclipse.datatools.enablement.oda.xml.util.SaxParserComplexNestedQueryHelper
{

	/**
	 * 
	 * @param consumer
	 * @param rinfo
	 * @param xdis
	 * @param tName
	 */
	SaxParserComplexNestedQueryHelper( SaxParserConsumer consumer, RelationInformation rinfo, XMLDataInputStream xdis, String tName )
	{
		super( consumer, rinfo, xdis, tName );
	}
}


