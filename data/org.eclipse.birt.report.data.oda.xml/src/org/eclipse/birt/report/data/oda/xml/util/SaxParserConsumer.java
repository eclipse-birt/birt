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

/**
 * This class is an implementation of ISaxParserConsumer. The instance of this
 * class deligate the communication between ResultSet and SaxParser, and does
 * the majority of result-set population job.
 * 
 * @deprecated Please use DTP xml driver
 */
public class SaxParserConsumer extends org.eclipse.datatools.enablement.oda.xml.util.SaxParserConsumer {
	/**
	 * 
	 * @param rs
	 * @param rinfo
	 * @param is
	 * @param tName
	 * @throws OdaException
	 */
	public SaxParserConsumer(RelationInformation rinfo, IXMLSource xmlSource, String tName) throws OdaException {
		super(rinfo, xmlSource, tName);
	}
}
