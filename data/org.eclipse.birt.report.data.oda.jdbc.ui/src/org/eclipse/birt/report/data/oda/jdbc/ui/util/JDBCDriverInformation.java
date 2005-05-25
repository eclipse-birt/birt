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

import java.sql.Driver;


/**
 * This class maintains information about a specific JDBC driver
 * such as its name, version, class etc.
 * It cannot be instantiated directly.
 * 
 * call the {@link #getInstance(java.sql.Driver) getInstance} method to create an instance
 * 
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/24 06:57:27 $
 */
public final class JDBCDriverInformation
{    
    private String driverClassName = null;
    private int majorVersion = 0;
    private int minorVersion = 0;
    private boolean displayVersion = true;
    private String urlFormat = null;
    private String driverDisplayName = null;

    static JDBCDriverInformation newInstance( Class driverClass )
	{
		Driver d = null;
		try
		{
			d = (Driver) driverClass.newInstance( );
		}
		catch ( Throwable e )
		{
		}
		
		JDBCDriverInformation info = null;
		if ( d != null )
		{
			info = new JDBCDriverInformation( );
			info.setDriverClassName( driverClass.getName( ) );
			info.setMajorVersion( d.getMajorVersion( ) );
			info.setMinorVersion( d.getMinorVersion( ) );
		}
		return info;
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
    public void setDriverClassName(String driverClassName)
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
    
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName(){
	   return driverDisplayName;	
	}
	
	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName){
	    this.driverDisplayName = displayName;
	}
	
	/**
	 * @return Returns the displayVersion.
	 */
	public boolean isDisplayVersion() {
		return displayVersion;
	}
	
	/**
	 * @param displayVersion The displayVersion to set.
	 */
	public void setDisplayVersion(boolean displayVersion) {
		this.displayVersion = displayVersion;
	}
	
    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(driverClassName);
        if ( displayVersion )
        {
	        buffer.append(" (");
	        buffer.append(majorVersion);
	        buffer.append(".");
	        buffer.append(minorVersion);
	        buffer.append(")");
        }
        return buffer.toString();
    }
    
}
