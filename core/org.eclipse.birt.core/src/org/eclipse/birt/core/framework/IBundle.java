/*
 * Created on 2005-3-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.core.framework;


/**
 *
 * @version $Revision:$ $Date:$
 */
public interface IBundle
{
	 Class loadClass(String name) throws ClassNotFoundException;
}
