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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;

public class Image extends ReportItem implements IImage
{

	public Image( ImageHandle image )
	{
		super( image );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getScale()
	 */

	public double getScale( )
	{
		return ( ( ImageHandle ) handle ).getScale( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSize()
	 */

	public String getSize( )
	{
		return ( ( ImageHandle ) handle ).getSize( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltText()
	 */

	public String getAltText( )
	{
		return ( ( ImageHandle ) handle ).getAltText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltTextKey()
	 */

	public String getAltTextKey( )
	{
		return ( ( ImageHandle ) handle ).getAltTextKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSource()
	 */

	public String getSource( )
	{
		return ( ( ImageHandle ) handle ).getSource( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setSource(java.lang.String)
	 */

	public void setSource( String source ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setSource( source );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURI()
	 */

	public String getURI( )
	{
		return ( ( ImageHandle ) handle ).getURI( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getTypeExpression()
	 */

	public String getTypeExpression( )
	{
		return ( ( ImageHandle ) handle ).getTypeExpression( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getValueExpression()
	 */

	public String getValueExpression( )
	{
		return ( ( ImageHandle ) handle ).getValueExpression( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getImageName()
	 */

	public String getImageName( )
	{
		return ( ( ImageHandle ) handle ).getImageName( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setImageName(java.lang.String)
	 */

	public void setImageName( String name ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setImageName( name );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * @deprecated
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURI(java.lang.String)
	 */

	public void setURI( String uri ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setURI( uri );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setScale(double)
	 */

	public void setScale( double scale ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setScale( scale );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setSize(java.lang.String)
	 */

	public void setSize( String size ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setSize( size );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setTypeExpression(java.lang.String)
	 */

	public void setTypeExpression( String value ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setTypeExpression( value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setValueExpression(java.lang.String)
	 */

	public void setValueExpression( String value ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setValueExpression( value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getActionHandle()
	 */

	public ActionHandle getActionHandle( )
	{
		return ( ( ImageHandle ) handle ).getActionHandle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setAction(org.eclipse.birt.report.model.api.elements.structures.Action)
	 */

	public ActionHandle setAction( Action action ) throws ScriptException
	{
		try
		{
			return ( ( ImageHandle ) handle ).setAction( action );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getHelpText()
	 */

	public String getHelpText( )
	{
		return ( ( ImageHandle ) handle ).getHelpText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setHelpText(java.lang.String)
	 */

	public void setHelpText( String helpText ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setHelpText( helpText );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getHelpTextKey()
	 */

	public String getHelpTextKey( )
	{
		return ( ( ImageHandle ) handle ).getHelpTextKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setHelpTextKey(java.lang.String)
	 */

	public void setHelpTextKey( String helpTextKey ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setHelpTextKey( helpTextKey );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public IAction getAction( )
	{
		return new ActionImpl( ( ( ImageHandle ) handle ).getActionHandle( ),
				( ImageHandle ) handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setFile()
	 */
	public void setFile( String file ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setFile( file );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getFile()
	 */
	public String getFile( )
	{
		return ( ( ImageHandle ) handle ).getFile( );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURL()
	 */
	public void setURL( String url ) throws ScriptException
	{
		try
		{
			( ( ImageHandle ) handle ).setURL( url );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURL()
	 */
	public String getURL( )
	{
		return ( ( ImageHandle ) handle ).getURL( );
	}

}
