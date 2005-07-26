/*
 * Created on 2005-3-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.core.framework;

import java.net.URL;
import java.util.Enumeration;


/**
 *
 * @version $Revision: 1.2 $ $Date: 2005/04/12 03:17:34 $
 */
public interface IBundle
{
	 Class loadClass(String name) throws ClassNotFoundException;
	 URL getEntry(String name);
	 Enumeration getEntryPaths(String path);
}
