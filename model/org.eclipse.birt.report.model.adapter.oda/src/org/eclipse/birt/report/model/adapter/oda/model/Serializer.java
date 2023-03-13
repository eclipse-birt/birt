/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.model.adapter.oda.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This interface provides a means to serialize and/or de-serialize the chart
 * model instance into XML content. Serialization is internally implemented
 * using EMF tools using the DesignValues XSDs (XML schema definition(s)).
 *
 * @author Actuate Corporation
 */
public interface Serializer {

	// Write Methods
	/**
	 * Write the designer values described by the model to the OutputStream
	 * provided.
	 *
	 * @param cModel The model to be serialized os The OutputStream to which the
	 *               model is to be serialized
	 * @param os     the output stream
	 * @throws IOException
	 */

	void write(DesignValues cModel, OutputStream os) throws IOException;

	/**
	 * Write the desinger values described to the string
	 *
	 * @param values the design value
	 * @return the string value
	 * @throws IOException
	 */

	String write(DesignValues values) throws IOException;

	// Read Methods
	/**
	 * Parses the string to the design value.
	 *
	 * @param values the designer values in string
	 *
	 * @return the designer values
	 * @throws IOException
	 */

	DesignValues read(String values) throws IOException;
}
