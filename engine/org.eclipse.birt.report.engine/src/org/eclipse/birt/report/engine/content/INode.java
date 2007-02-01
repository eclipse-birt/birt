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
 */
public interface INode
{

	INode getParent( );

	void setParent( INode parent );

	INode getPrevious();
	
	void setPrevious(INode previous);
	
	INode getNext();
	
	void setNext(INode next);
	
	void appendChild( INode child );

	Iterator getChildren( );

	void removeChildren( );
}
