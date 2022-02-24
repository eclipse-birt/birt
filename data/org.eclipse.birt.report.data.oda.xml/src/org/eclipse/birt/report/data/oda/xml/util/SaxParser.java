/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.xml.util;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.enablement.oda.xml.util.ISaxParserConsumer;
import org.eclipse.datatools.enablement.oda.xml.util.IXMLSource;

/**
 * This instance interacts with a SaxParserConsumer instance to populate the
 * ResultSet data.
 * 
 * @deprecated Please use DTP xml driver
 */
public class SaxParser extends org.eclipse.datatools.enablement.oda.xml.util.SaxParser {

	/**
	 * 
	 * @param stream
	 * @param consumer
	 */
	public SaxParser(IXMLSource xmlSource, ISaxParserConsumer consumer, boolean useNamespace) throws OdaException {
		super(xmlSource, consumer, useNamespace);
	}
}
