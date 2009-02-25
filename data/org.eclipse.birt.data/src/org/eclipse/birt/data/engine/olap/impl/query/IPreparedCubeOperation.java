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

package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared cube operation
 */
public interface IPreparedCubeOperation
{

	/**
	 * 
	 * @return the original cube operation
	 */
	ICubeOperation getCubeOperation( );

	
	
	/**
	 * 
	 * @param scope
	 * @param cx
	 * @param manager
	 * @param basedBindings：the bindings this operation can refers to
	 * @throws DataException
	 */
	void prepare( Scriptable scope, ScriptContext cx, MeasureNameManager manager, IBinding[] basedBindings ) throws DataException;

	
	/**
	 * called after prepare() is called
	 * @return new CubeAggrDefns introduced from this operation.
	 *         an empty array is returned if no CubeAggrDefn introduced 
	 */
	CubeAggrDefn[] getNewCubeAggrDefns( ); 
	              
	/**
	 * execute the operation based on sources
	 * 
	 * @param cubeQueryDefn
	 *            cube query definition that the operation belongs to
	 * @param sources:
	 *            the data to be operated on
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	IAggregationResultSet[] execute( ICubeQueryDefinition cubeQueryDefn,
			IAggregationResultSet[] sources, Scriptable scope, ScriptContext cx, StopSign stopSign )
			throws IOException, BirtException;
}
