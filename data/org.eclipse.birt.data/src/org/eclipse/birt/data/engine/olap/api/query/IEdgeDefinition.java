
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
package org.eclipse.birt.data.engine.olap.api.query;

import java.util.List;

/**
 * 
 */

public interface IEdgeDefinition extends INamedObject
{
	public IDimensionDefinition createDimension( String name );
	public List getDimensions( );
	public IEdgeDrillingDownDefinition createDrillingDownDefinition( String name );
	public List getDrillingDownDefinition();
	public IEdgeDrillingUpDefinition createDrillingUpDefinition( String name );
	public List getDrillingUpDefinition();
}
