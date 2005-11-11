/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

import java.util.Iterator;

/**
 * Tree interface.
 * 
 * implement memory management policy. The node may exist in disk or keep in the
 * memory.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/10/21 04:51:43 $
 */
public interface INode
{

	INode getParent( );

	void setParent( INode parent );

	void appendChild( INode child );

	Iterator getChildren( );

	void removeChildren( );
}
