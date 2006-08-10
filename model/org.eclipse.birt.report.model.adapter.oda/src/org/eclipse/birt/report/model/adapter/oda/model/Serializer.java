/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.model.adapter.oda.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * This interface provides a means to serialize and/or de-serialize the chart
 * model instance into XML content. Serialization is internally implemented
 * using EMF tools using the DesignValues XSDs (XML schema definition(s)).
 * 
 * @author Actuate Corporation
 */
public interface Serializer
{

	// Write Methods
	/**
	 * Write the chart described by the model to the OutputStream provided.
	 * 
	 * @param cModel
	 *            The model to be serialized os The OutputStream to which the
	 *            model is to be serialized
	 */
	public void write( DesignValues cModel, OutputStream os ) throws IOException;

	public String write( DesignValues values ) throws IOException;
	
	/**
	 * Write the chart described by the model to a ByteArrayOutputStream.
	 * 
	 * @param cModel
	 *            The model to be serialized bStripHeaders Specifies whether or
	 *            not the headers are to be removed while serializing the model
	 * @return the ByteArrayOutputStream containing the serialized model
	 */
	public ByteArrayOutputStream asXml( DesignValues cModel, boolean bStripHeaders )
			throws IOException;

	// Read Methods
	/**
	 * Reads the chart model from the given InputStream
	 * 
	 * @return chart model read from the stream
	 */
	public DesignValues read( InputStream is ) throws IOException;

	/**
	 * @param values
	 * @return
	 * @throws IOException
	 */
	
	public DesignValues read( String values ) throws IOException;
	
	/**
	 * Reads the chart model from the ByteArrayInputStream.
	 * 
	 * @param byaIS
	 *            The ByteArrayInputStream holding the chart model
	 * @param bStripHeaders
	 *            Specifies whether or not the headers were removed when the
	 *            chart model was saved
	 * @return chart model read from the stream
	 * @throws IOException
	 */
	public DesignValues fromXml( ByteArrayInputStream byaIS, boolean bStripHeaders )
			throws IOException;
}