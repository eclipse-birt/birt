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

package org.eclipse.birt.report.engine.ir;

/**
 * Image Item definition.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class ImageItemDesign extends ReportItemDesign
{

	/**
	 * the image source is defined by a URI
	 */
	public final static int IMAGE_URI = 0;
	/**
	 * the image source is defined by name
	 */
	public final static int IMAGE_NAME = 1;
	/**
	 * the image source is defined by expression.
	 */
	public final static int IMAGE_EXPRESSION = 2;
	
	/**
	 * the image source is defined by a FILE
	 */
	public final static int IMAGE_FILE = 3;

	/**
	 * image source type
	 */
	protected int imageSource;

	/**
	 * image uri, used if source type URI
	 */
	protected String imageUri;
	/**
	 * image name, used if source type NAME
	 */
	protected String imageName;
	/**
	 * image expression, used if source type EXPR
	 */
	protected Expression imageExpression;
	/**
	 * image type, used if source type EXPR.
	 */
	protected Expression imageFormat;

	/**
	 * Action associated with this item.
	 */
	protected ActionDesign action;
	/**
	 * Text associated with this image, used for default locale.
	 */
	protected String altText;
	/**
	 * Text Resource Key used for altText localization.
	 */
	protected String altTextKey;
	
	/**
	 * help text
	 */
	protected String helpText;
	/**
	 * help text resource key
	 */
	protected String helpTextKey;

	public ImageItemDesign( )
	{
		this.imageSource = IMAGE_URI;
	}

	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitImageItem( this );
	}

	/**
	 * get the type of the image source.
	 * 
	 * Image can be defined by expression, uri and name.
	 * 
	 * @return type of the image source.
	 */
	public int getImageSource( )
	{
		return this.imageSource;
	}

	/**
	 * @param imageExpr
	 *            The imageExpr to set.
	 * @param imageType
	 *            the image type.
	 */
	public void setImageExpression( Expression imageExpr, Expression imageType )
	{
		this.imageSource = IMAGE_EXPRESSION;
		this.imageExpression = imageExpr;
		this.imageFormat = imageType;
	}

	/**
	 * @return Returns the imageExpr.
	 */
	public Expression getImageExpression( )
	{
		assert this.imageSource == IMAGE_EXPRESSION;
		return imageExpression;
	}

	/**
	 * @return Returns the imageType.
	 */
	public Expression getImageFormat( )
	{
		assert this.imageSource == IMAGE_EXPRESSION;
		return imageFormat;
	}

	/**
	 * @param imageName
	 *            The imageName to set.
	 */
	public void setImageName( String imageName )
	{
		this.imageSource = IMAGE_NAME;
		this.imageName = imageName;
	}

	/**
	 * @return Returns the imageName.
	 */
	public String getImageName( )
	{
		assert imageSource == IMAGE_NAME;
		return imageName;
	}

	/**
	 * @param imageUri
	 *            The imageUri to set.
	 */
	public void setImageUri( String imageUri )
	{
		this.imageSource = IMAGE_URI;
		this.imageUri = imageUri;
	}

	/**
	 * @return Returns the imageUri.
	 */
	public String getImageUri( )
	{
		assert imageSource == IMAGE_URI;
		return imageUri;
	}
	
	public void setImageFile(String file)
	{
		imageSource = IMAGE_FILE;
		imageUri = file;
	}
	
	public String getImageFile()
	{
		assert imageSource == IMAGE_FILE;
		return imageUri;
	}
	
	

	/**
	 * @return Returns the action.
	 */
	public ActionDesign getAction( )
	{
		return action;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction( ActionDesign action )
	{
		this.action = action;
	}

	/**
	 * @param altText
	 *            The altText to set.
	 */
	public void setAltText( String altTextKey, String altText )
	{
		this.altTextKey = altTextKey;
		this.altText = altText;
	}

	/**
	 * @return Returns the altTextKey.
	 */
	public String getAltTextKey( )
	{
		return altTextKey;
	}

	/**
	 * @return Returns the altText.
	 */
	public String getAltText( )
	{
		return altText;
	}
	/**
	 * set the help info.
	 * 
	 * @param key
	 *            resource key
	 * @param text
	 *            text content
	 */
	public void setHelpText( String key, String text )
	{
		this.helpTextKey = key;
		this.helpText = text;
	}

	/**
	 * get the help text property.
	 * @return help text
	 */
	public String getHelpText()
	{
		return this.helpText;
	}
	
	/**
	 * get the help text resource key property.
	 * @return resource key of the help text
	 */
	public String getHelpTextKey()
	{
		return this.helpTextKey;
	}
}
