/*
 * Created on 2005-3-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.core.framework;

import java.net.URL;


/**
 *
 * @version $Revision: 1.1 $ $Date: 2005/03/25 02:33:15 $
 */
public interface IBundle
{
	 Class loadClass(String name) throws ClassNotFoundException;
	 URL getEntry(String name);
}
