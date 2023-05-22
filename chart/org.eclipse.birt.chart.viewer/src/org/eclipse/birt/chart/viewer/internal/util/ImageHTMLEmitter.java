/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.util;

/**
 * HTML emitter for image object
 */

public class ImageHTMLEmitter
{
	/** image property: id */
	public String id = ""; //$NON-NLS-1$
	/** image property: image type */
	public String ext = "PNG"; //$NON-NLS-1$
	/** image property: source */
	public String src = ""; //$NON-NLS-1$
	/** image property: alt */
	public String alt = ""; //$NON-NLS-1$
	/** image property: image map */
	public String imageMap;
	/** image property: width */
	public int width = 0;
	/** image property: height */
	public int height = 0;

	private StringBuffer html = null;

	/**
	 * Constructor
	 */
	public ImageHTMLEmitter( )
	{
		html = new StringBuffer( );
	}

	/**
	 * Generate HTML
	 *
	 * @return Return generated HTML
	 */
	public String generateHTML( )
	{
		if ( isSVG( ) )
		{
			addSVG( );
		}
		else if ( isPDF( ) )
		{
			addPDF( );
		}
		else
		{
			addImage( );
		}
		return html.toString( );
	}

	private void addSVG( )
	{
		 addSVGEmbed( );
	}

	private void addSVGEmbed( )
	{
		html.append( "<embed id=\"" //$NON-NLS-1$
				+ id + "\" type=\"image/svg+xml\" src=\"" + src + "\" alt=\"" //$NON-NLS-1$ //$NON-NLS-2$
				+ alt + "\" style=\" width: " + width + "px; height: " + height //$NON-NLS-1$ //$NON-NLS-2$
				+ "px;\">" ); //$NON-NLS-1$
		html.append("\n</embed>");
	}

	private void addPDF( )
	{
		addIFrame( );
	}

	private void addImage( )
	{
		addImageDiv( );
		// addIFrame( );
	}

	private void addImageDiv( )
	{
		html.append( "<div>\n " ); //$NON-NLS-1$
		if ( imageMap != null )
		{
			html.append( "<map name=\"" + id + "\">" ); //$NON-NLS-1$ //$NON-NLS-2$
			html.append( imageMap );
			html.append( "</map>" ); //$NON-NLS-1$
		}
		html.append( "<img id=\"" //$NON-NLS-1$
				+ id + "\" src=\"" + src + "\" alt=\"" + alt //$NON-NLS-1$ //$NON-NLS-2$
				+ "\" style=\" width: " + width + "; height: " + height //$NON-NLS-1$ //$NON-NLS-2$
				+ ";\" border=0" ); //$NON-NLS-1$
		if ( imageMap != null )
		{
			html.append( " usemap=\"#" + id + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		html.append( ">" ); //$NON-NLS-1$
		html.append( "\n</div>" ); //$NON-NLS-1$
	}

	private void addIFrame( )
	{
		html.append( "<iframe id=\"" //$NON-NLS-1$
				+ id + "\" src=\"" + src + "\" width=" + width + " height=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ height + " scrolling=\"no\" frameborder=0 ></iframe>\n" ); //$NON-NLS-1$
	}

	private void addAutoFresh( )
	{
		int timeInterval = 5000;
		// Print js code for auto-refresh
		final String strObj = "window.frames['" + id + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		final String strFunc = "refreshIFrame_" + id + "( )"; //$NON-NLS-1$ //$NON-NLS-2$
		html.append( "<script language=\"javascript\">\n" ); //$NON-NLS-1$
		html.append( strObj + ".onload = " + strFunc + ";\n" ); //$NON-NLS-1$ //$NON-NLS-2$
		html.append( "function " + strFunc + "\n{\n  " ); //$NON-NLS-1$ //$NON-NLS-2$
		html.append( "window.setInterval(\"" //$NON-NLS-1$
				+ strObj + ".location.reload()\"," + timeInterval //$NON-NLS-1$
				+ ");\n}\n" ); //$NON-NLS-1$
		html.append( "</script>" ); //$NON-NLS-1$
	}

	/**
	 * Check is SVG
	 *
	 * @return Return the check result of SVG
	 */
	public boolean isSVG( )
	{
		return "SVG".equalsIgnoreCase( ext ); //$NON-NLS-1$
	}

	/**
	 * Check is PDF
	 *
	 * @return Return the check result of PDF
	 */
	public boolean isPDF( )
	{
		return "PDF".equalsIgnoreCase( ext ); //$NON-NLS-1$
	}

}
