/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;



/**
 * This class maintains information about a specific JDBC driver
 * such as its name, version, class etc.
 * It cannot be instantiated directly.
 * 
 * call the {@link #getInstance(java.sql.Driver) getInstance} method to create an instance
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/11 02:51:08 $
 */
public final class JDBCDriverInformation
{
    
    private String driverClassName = null;
    private int majorVersion = 0;
    private int minorVersion = 0;
    private String urlFormat = null;

    /**
     * 
     */
    private JDBCDriverInformation()
    {
        super();
    }
    
    static JDBCDriverInformation getInstance(Class driverClass, URL[] classPaths) throws InstantiationException, IllegalAccessException
    {
        URLClassLoader ucl = new URLClassLoader( classPaths );
        Driver d = (Driver) driverClass.newInstance( );
        
        JDBCDriverInformation info = new JDBCDriverInformation();
        info.setDriverClassName(driverClass.getName());
        info.setMajorVersion(d.getMajorVersion());
        info.setMinorVersion(d.getMinorVersion());
        return info;
    }

    static JDBCDriverInformation getInstance(String driverClassName, URL[] classPaths) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        URLClassLoader ucl = new URLClassLoader( classPaths );
        return getInstance(Class.forName( driverClassName, true, ucl ), classPaths);
    }
    
    

    /**
     * @return Returns the driverClassName.
     */
    public String getDriverClassName()
    {
        return driverClassName;
    }
    /**
     * @param driverClassName The driverClassName to set.
     */
    protected void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }
    /**
     * @return Returns the majorVersion.
     */
    public int getMajorVersion()
    {
        return majorVersion;
    }
    /**
     * @param majorVersion The majorVersion to set.
     */
    protected void setMajorVersion(int majorVersion)
    {
        this.majorVersion = majorVersion;
    }
    /**
     * @return Returns the minorVersion.
     */
    public int getMinorVersion()
    {
        return minorVersion;
    }
    /**
     * @param minorVersion The minorVersion to set.
     */
    protected void setMinorVersion(int minorVersion)
    {
        this.minorVersion = minorVersion;
    }
   
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(driverClassName);
        buffer.append(" (");
        buffer.append(majorVersion);
        buffer.append(".");
        buffer.append(minorVersion);
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @return Returns the urlFormat.
     */
    public String getUrlFormat()
    {
        return urlFormat;
    }
    /**
     * @param urlFormat The urlFormat to set.
     */
    protected void setUrlFormat(String urlFormat)
    {
        this.urlFormat = urlFormat;
    }
}
