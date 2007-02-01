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
 * @version $Revision: 1.2 $ $Date: 2007/01/05 07:24:55 $
 */
public final class JDBCDriverInformation
{    
    private String driverClassName = null;
    private int majorVersion = 0;
    private int minorVersion = 0;
    private String urlFormat = null;
    private String driverDisplayName = null;
    
    /**
     * Since factory methods are provided, it is recommended to make
     * construction method private.
     */
    private JDBCDriverInformation(){}
    
    public static JDBCDriverInformation newInstance( Class driverClass )
	{
		try
		{
			Driver d = (Driver) driverClass.newInstance( );
			if ( d != null )
			{
				JDBCDriverInformation info = newInstance( driverClass.getName( ) );
				info.setMajorVersion( d.getMajorVersion( ) );
				info.setMinorVersion( d.getMinorVersion( ) );
				return info;
			}
		}
		catch ( Throwable e )
		{
		}
		
		return null;
	}
    
    public static JDBCDriverInformation newInstance( String driverClassName )
	{
		JDBCDriverInformation info = new JDBCDriverInformation( );
		info.setDriverClassName( driverClassName );
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
	
    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(driverClassName);
        if ( majorVersion != 0 || minorVersion != 0 )
        {
	        buffer.append(" (");
	        buffer.append(majorVersion);
	        buffer.append(".");
	        buffer.append(minorVersion);
	        buffer.append(")");
        }
        return buffer.toString();
    }
    
    /** Gets a display-friendly string which has driver class, driver name and version */
    public String getDisplayString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(driverClassName);
        if ( majorVersion != 0 || minorVersion != 0 ||
        	driverDisplayName != null )
        {
	        buffer.append(" (");
	        if ( driverDisplayName != null )
	        	buffer.append( driverDisplayName );
	        if ( majorVersion != 0 || minorVersion != 0 )
	        {
		        if ( driverDisplayName != null )
		        	buffer.append(" ");
		        buffer.append("v");
				buffer.append(majorVersion);
		        buffer.append(".");
		        buffer.append(minorVersion);
	        }
	        buffer.append(")");
        }
        return buffer.toString();
    }
}
