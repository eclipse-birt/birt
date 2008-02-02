/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.presentation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.CachedImage;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.data.dte.SingleCubeResultSet;
import org.eclipse.birt.report.engine.data.dte.SingleQueryResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.template.TemplateExecutor;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.Size;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.ReportItemPresentationInfo;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.internal.OnRenderScriptVisitor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.util.ULocale;


public class LocalizedContentVisitor extends ContentVisitorAdapter
{
	
	protected static Logger logger = Logger
			.getLogger( LocalizedContentVisitor.class.getName( ) );
	
	private ExecutionContext context;
	private Locale locale;
	private String outputFormat;
	protected HashMap templates = new HashMap( );
	private OnRenderScriptVisitor onRenderVisitor;
    final static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', 'A', 'B', 'C', 'D', 'E', 'F'}; 
    
	public LocalizedContentVisitor( ExecutionContext context )
	{
		this.context = context;
		this.locale = context.getLocale( );
		this.outputFormat = context.getOutputFormat( );
		this.onRenderVisitor = new OnRenderScriptVisitor( context );
	}
	
	IReportContent getReportContent( )
	{
		return context.getReportContent( );
	}

	ReportDesignHandle getReportDesign( )
	{
		return context.getDesign( );
	}
	
	/**
	 * Checks the background image property. If it is given as a relative path,
	 * gets its absolute path and sets it back to the style.
	 * 
	 * @param style
	 *            the style that defines background image related properties
	 */
	protected void processBackgroundImage( IStyle style )
	{
		if ( style == null )
			return;

		String image = style.getBackgroundImage( );
		if ( image == null )
			return;

		ReportDesignHandle reportDesign = context.getDesign( );
		if ( reportDesign != null )
		{
			URL url = reportDesign.findResource( image, IResourceLocator.IMAGE );
			if ( url != null )
			{
				style.setBackgroundImage( url.toExternalForm( ) );
			}
		}
	}
	
	public IContent localize(IContent content)
	{
		IStyle style = content.getInlineStyle( );
		processBackgroundImage( style );
		Object value = content.accept( this, content );
		return (IContent) value;
	}
	
	protected IContent localizeAllChildren( IContent content )
	{
		ArrayList children = (ArrayList) content.getChildren( );
		if ( children != null )
		{			
			for ( int i = 0; i < children.size( ); i++ )
			{
				IContent child = (IContent) children.get( i );
				localize( child );
				localizeAllChildren( child );
			}
		}
		return content;
	}

	public Object visitPage( IPageContent page, Object value )
	{
		boolean isExecutingMasterPage = context.isExecutingMasterPage( );
		context.setExecutingMasterPage( true );
		value = localizeAllChildren( page );
		context.setExecutingMasterPage( isExecutingMasterPage );
		return value;
	}
	
	protected TextTemplate parseTemplate( String text ) throws BirtException
	{
		SoftReference templateRef = (SoftReference) templates.get( text );
		TextTemplate template = null;
		if ( templateRef != null )
		{
			template = (TextTemplate) templateRef.get( );
			if ( template != null )
			{
				return template;
			}
		}
		try
		{
			template = new org.eclipse.birt.core.template.TemplateParser( )
					.parse( text );
			templateRef = new SoftReference( template );
			templates.put( text, templateRef );
		}
		catch ( Exception ex )
		{
			throw new EngineException( ex.getLocalizedMessage( ), ex );
		}
		return template;
	}

	String executeTemplate( TextTemplate template, HashMap values )
	{
		return new TemplateExecutor( context ).execute( template, values );
	}

	public Object visitList( IListContent list, Object value)
	{
		if ( list.getGenerateBy( ) instanceof ListItemDesign )
		{
			handleOnRender( list );
		}
		return list;
	}

	public Object visitTable( ITableContent table, Object value )
	{
		handleOnRender( table );
		
		String captionText = table.getCaption( );
		String captionKey = table.getCaptionKey( );

		captionText = localize( table, captionKey, captionText );
		table.setCaption( captionText );
		
		return table;
	}

	public Object visitRow( IRowContent row, Object value )
	{
		handleOnRender( row );
		return row;
	}

	public Object visitCell( ICellContent cell, Object value )
	{
		handleOnRender( cell );
		return cell;
	}

	/**
	 * handle the data content.
	 * 
	 * @param data
	 *            data content object
	 */
	public Object visitData( IDataContent data, Object value )
	{
		handleOnRender( data );
		processData( data );
		return data;
	}

	/**
	 * process the data content
	 * 
	 * <li> localize the help text
	 * <li> format the value
	 * <li> handle it as it is an text.
	 * 
	 * @param data
	 *            data object
	 */
	protected void processData( IDataContent data )
	{
		String helpText = localize( data, data.getHelpKey( ), data.getHelpText( ) );
		data.setHelpText( helpText );
		String text = ""; //$NON-NLS-1$
		if ( data.getLabelKey( ) != null || data.getLabelText( ) != null )
		{
			text = localize( data, data.getLabelKey( ), data.getLabelText( ) );
		}
		else
		{
			Object value = data.getValue( );

			if ( value instanceof Object[] )
			{
				Object[] values = (Object[]) value;
				if ( values.length > 0 )
				{
					value = values[0];
				}
				else
				{
					value = null;
				}
			}
			
			if ( value != null )
			{
				IStyle style = data.getComputedStyle( );
				if ( value instanceof Number )
				{
					String format = style.getNumberFormat( );
					NumberFormatter fmt = context.getNumberFormatter( format );
					text = fmt.format( (Number) value );
					CSSValue align = style
							.getProperty( IStyle.STYLE_NUMBER_ALIGN );
					if ( align != null && align != CSSValueConstants.NONE_VALUE )
					{
						data.getStyle( ).setProperty( IStyle.STYLE_TEXT_ALIGN,
								align );
					}
				}
				else if ( value instanceof String )
				{
					StringFormatter fmt = context.getStringFormatter( style
							.getStringFormat( ) );
					text = fmt.format( (String) value );

				}
				else if ( value instanceof java.util.Date )
				{
					String dateFormat = null;
					if ( value instanceof java.sql.Date )
					{
						dateFormat = style.getDateFormat( );
					}
					else if ( value instanceof java.sql.Time )
					{
						dateFormat = style.getTimeFormat( );
					}
					if ( dateFormat == null )
					{
						dateFormat = style.getDateTimeFormat( );
					}
					DateFormatter fmt = context.getDateFormatter( dateFormat );
					text = fmt.format( (java.util.Date) value );
				}
				else
				{
					if ( value instanceof byte[] )
					{
						byte[] bytes = (byte[]) value;
						int length = ( bytes.length <= 8 ? bytes.length : 8 );

						StringBuffer buffer = new StringBuffer( );
						int index = 0;
						while ( index < length )
						{
							byte byteValue = bytes[index];
							int lowValue = byteValue & 0x0F;
							int highValue = ( byteValue >> 4 ) & 0x0F;
							buffer.append( HEX[highValue] ).append(
									HEX[lowValue] ).append( ' ' );
							index++;
						}
						if ( length > 0 )
						{
							if ( length != bytes.length )
							{
								buffer.append( "..." );
							}
							else
							{
								buffer.setLength( buffer.length( ) - 1 );
							}
						}

						text = buffer.toString( );
					}
					else
					{
						text = value.toString( );
					}
				}
			}
		}
		// text can be null value after applying format
		if ( text != null )
		{
			data.setText( text );
		}
		else
		{
			data.setText( "" ); //$NON-NLS-1$
		}
	}

	/**
	 * handle the label.
	 * 
	 * @param label
	 *            label content
	 */
	public Object visitLabel( ILabelContent label, Object value )
	{
		handleOnRender( label );
		processLabel( label );
		return label;
	}

	/**
	 * process the label content
	 * 
	 * <li> localize the help text
	 * <li> localize the label content
	 * <li> handle it as it is an text
	 * 
	 * @param label
	 *            label object
	 */
	protected void processLabel( ILabelContent label )
	{
		String helpText = localize( label, label.getHelpKey( ), label.getHelpText( ) );
		label.setHelpText( helpText );
		
		if ( label.getText( ) == null )
		{
			String text = localize( label, label.getLabelKey( ), label.getLabelText( ) );
			label.setText( text );
		}
	}

	public Object visitText( ITextContent text, Object value )
	{
		handleOnRender( text );
		return value;
	}
	
	public Object visitAutoText(IAutoTextContent autoText, Object value)
	{
		int type = autoText.getType( );
		if ( type == IAutoTextContent.PAGE_NUMBER )
		{
			autoText.setText( String.valueOf( context.getPageNumber( ) ) );
		}
		else if ( type == IAutoTextContent.TOTAL_PAGE )
		{
			long totalPage = context.getTotalPage( );
			if ( totalPage <= 0 )
			{
				autoText.setText( "---" );
			}
			else
			{
				autoText.setText( String.valueOf( totalPage ) );
			}
		}
		handleOnRender( autoText );

		return value;
	}


	/**
	 * handle the foreign content object.
	 * 
	 * Foreign content can be created by following design element:
	 * <li> Text(HTML). It will create a TEMPLATE_TYPE foreign object.
	 * <li> MultiLine(HTML). It will create a HTML_TYPE forign object
	 * <li> MultiLine(PlainText).It will create a TEXT_TYPE foreign object
	 * <li> Extended item. It will create a
	 * TEXT_TYPE/HTML_TYPE/IMAGE_TYPE/VALUE_TYPE foreign object.
	 * 
	 */
	public Object visitForeign( IForeignContent foreignContent, Object value )
	{
		IReportContent reportContent = getReportContent( );
		
		String rawFormat = foreignContent.getRawType( );
		Object rawValue = foreignContent.getRawValue( );

		handleOnRender( foreignContent );
		
		if ( IForeignContent.TEMPLATE_TYPE.equals( rawFormat ) )
		{
			processTemplateContent( foreignContent );
			return foreignContent;
		}
		
		if ( IForeignContent.EXTERNAL_TYPE.equals( rawFormat ) )
		{
			return processExtendedContent( foreignContent );
		}
		
		if ( IForeignContent.IMAGE_TYPE.equals( rawFormat ) )
		{
			if ( rawValue instanceof IImageContent )
			{
				IImageContent image = (IImageContent) rawValue;
				processImage( image );
				return image;
			}
			if ( rawValue instanceof byte[] )
			{
				IImageContent imageContent = reportContent
						.createImageContent( foreignContent );
				imageContent.setImageSource( IImageContent.IMAGE_EXPRESSION );
				imageContent.setData( (byte[]) rawValue );
				processImage( imageContent );
				return imageContent;
			}
		}
		
		if ( IForeignContent.TEXT_TYPE.equals( rawFormat ) )
		{
			ITextContent textContent = reportContent
					.createDataContent( foreignContent );
			textContent.setText( rawValue == null ? "" : rawValue.toString( ) ); //$NON-NLS-1$
			return textContent;
		}
		
		if ( IForeignContent.HTML_TYPE.equals( rawFormat ) )
		{
			String key = foreignContent.getRawKey( );
			if (key != null)
			{
				String text = localize( foreignContent, key, null);
				if (text != null)
				{
					foreignContent.setRawValue(  text );
				}
			}
			return foreignContent;
		}
		
		if ( IForeignContent.VALUE_TYPE.equals( rawFormat ) )
		{
			IDataContent dataContent = reportContent
					.createDataContent( foreignContent );
			dataContent.setValue( rawValue );
			processData( dataContent );
			return dataContent;
		}
		return foreignContent;
	}

	/**
	 * localzie the text.
	 * 
	 * @param key
	 *            text key
	 * @param text
	 *            default text
	 * @return localized text.
	 */
	private String localize( IContent content, String key, String text )
	{
		assert ( content != null );
		if ( content.getGenerateBy( ) != null )
		{
			DesignElementHandle element = ( (ReportItemDesign) content
					.getGenerateBy( ) ).getHandle( );
			if ( key != null && element != null )
			{
				String t = ModuleUtil.getExternalizedValue( element, key, text,
						ULocale.forLocale( locale ) );
				if ( t != null )
				{
					return t;
				}
			}
		}
		return text;
	}

	public Object visitImage( IImageContent image, Object value )
	{
		handleOnRender( image );
		if ( image.getImageSource( ) == IImageContent.IMAGE_FILE
				|| image.getImageSource( ) == IImageContent.IMAGE_URL )
		{
			String strUri = image.getURI( );

			ReportDesignHandle reportDesign = context.getDesign( );
			URL uri = reportDesign
					.findResource( strUri, IResourceLocator.IMAGE );
			if ( uri != null )
			{
				image.setURI( uri.toExternalForm( ) );
			}
		}
		processImage( image );
		return image;
	}

	protected void processImage( IImageContent image )
	{
		String altText = localize( image, image.getAltTextKey( ), image.getAltText( ) );
		image.setAltText( altText );
		String helpText = localize( image, image.getHelpKey( ), image.getHelpText( ) );
		image.setHelpText( helpText );
	}

	/**
	 * handle the template result.
	 * 
	 * @param foreignContent
	 */

	protected void processTemplateContent( IForeignContent foreignContent )
	{
		assert IForeignContent.TEMPLATE_TYPE.equals( foreignContent
				.getRawType( ) );

		if ( foreignContent.getGenerateBy( ) instanceof TextItemDesign )
		{
			TextItemDesign design = (TextItemDesign) foreignContent
					.getGenerateBy( );

			String text = null;
			HashMap rawValues = null;
			if ( foreignContent.getRawValue( ) instanceof Object[] )
			{
				Object[] rawValue = (Object[]) foreignContent.getRawValue( );
				assert rawValue.length == 2;
				assert rawValue[0] == null || rawValue[0] instanceof String;
				if ( rawValue[0] != null )
				{
					text = (String )rawValue[0];
				}
				if ( rawValue[1] instanceof HashMap )
				{
					rawValues = (HashMap)rawValue[1];
				}
			}

			if ( text == null )
			{
				text = localize( foreignContent, design.getTextKey( ), design.getText( ) );
			}
			try
			{
				TextTemplate template = parseTemplate( text );

				String result = executeTemplate( template, rawValues );
				
				foreignContent.setRawType( IForeignContent.HTML_TYPE );
				foreignContent.setRawValue( result );
			}
			catch ( BirtException ex )
			{
				context.addException( design, ex );
			}
		}
	}

	protected String getOutputFormat()
	{
		return outputFormat;
	}
	
	/**
	 * @return whether the output format is for printing
	 */
	protected boolean isForPrinting( )
	{
		String outputFormat = getOutputFormat( );
		if ( "FO".equalsIgnoreCase( outputFormat )
				|| "PDF".equalsIgnoreCase( outputFormat )
				||"POSTSCRIPT".equalsIgnoreCase( outputFormat ))
			return true;
		return false;
	}
	
	private int getChartResolution( )
	{
		Map appContext = context.getAppContext( );
		int resolution = 0;
		if ( appContext != null )
		{
			Object tmp = appContext
					.get( EngineConstants.APPCONTEXT_CHART_RESOLUTION );
			if ( tmp != null && tmp instanceof Number )
			{
				resolution = ( (Number) tmp ).intValue( );
				if ( resolution < 96 )
				{
					resolution = 96;
				}
			}
		}
		if ( 0 == resolution )
		{
			if ( isForPrinting( ) )
			{
				resolution = 192;
			}
			else
			{
				resolution = 96;
			}
		}
		return resolution;
	}
	
	private String getChartFormats()
	{
		IRenderOption renderOption = context.getRenderOption( );
		String formats = renderOption.getSupportedImageFormats( );
		if ( formats != null )
		{
			return formats;
		}
		return "PNG;GIF;JPG;BMP;"; //$NON-NLS-1$
	}

	private String getImageCacheID( IContent content )
	{
		StringBuffer buffer = new StringBuffer( );
		buffer.append( content.getInstanceID( ).toUniqueString( ) );
		buffer.append( getChartResolution( ) );
		buffer.append( getChartFormats( ) );
		buffer.append( locale );
		return buffer.toString( );
	}
	
	private IContent processCachedImage( IForeignContent content,
			CachedImage cachedImage )
	{
		IImageContent imageObj = getReportContent( ).createImageContent(
				content );
		imageObj.setParent( content.getParent( ) );
		// Set image map
		imageObj.setImageSource( IImageContent.IMAGE_FILE );
		imageObj.setURI( cachedImage.getURL( ) );
		imageObj.setMIMEType( cachedImage.getMIMEType( ) );
		imageObj.setImageMap( cachedImage.getImageMap( ) );
		imageObj.setAltText( content.getAltText( ) );
		imageObj.setAltTextKey( content.getAltTextKey( ) );
		processImage( imageObj );
		return imageObj;
	}

	/**
	 * handle an extended item.
	 * 
	 * @param content
	 *            the object.
	 */
	protected IContent processExtendedContent( IForeignContent content )
	{
		assert IForeignContent.EXTERNAL_TYPE.equals( content.getRawType( ) );
		assert content.getGenerateBy( ) instanceof ExtendedItemDesign;

		IContent generatedContent = content;
		
		ExtendedItemDesign design = (ExtendedItemDesign) content
				.getGenerateBy( );
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle( );
		String tagName = handle.getExtensionName( );
		
		if ( "Chart".equals( tagName ) )
		{
			IHTMLImageHandler imageHandler = context.getImageHandler( );
			if ( imageHandler != null )
			{
				String imageId = getImageCacheID( content );
				CachedImage cachedImage = imageHandler.getCachedImage( imageId,
						IImage.CUSTOM_IMAGE, context.getReportContext( ) );
				if ( cachedImage != null )
				{
					return processCachedImage(content, cachedImage);
				}
			}
		}

		// call the presentation peer to create the content object
		IReportItemPresentation itemPresentation = ExtensionManager
				.getInstance( ).createPresentationItem( tagName );
		int resolution = 0;
		if ( itemPresentation != null )
		{
			IDataQueryDefinition[] queries = design.getQueries( );

			ReportItemPresentationInfo info = new ReportItemPresentationInfo( );
			info.setModelObject( handle );
			info
					.setApplicationClassLoader( context
							.getApplicationClassLoader( ) );
			info.setReportContext( context.getReportContext( ) );
			info.setReportQueries( queries );
			resolution = getChartResolution( );
			info.setResolution( resolution );
			info.setExtendedItemContent( content );
			info.setSupportedImageFormats( getChartFormats( ) );
			info.setActionHandler( context.getActionHandler( ) );
			info.setOutputFormat( getOutputFormat( ) );

			itemPresentation.init( info );

			Object rawValue = content.getRawValue( );
			if ( rawValue instanceof byte[] )
			{
				byte[] values = (byte[]) rawValue;
				itemPresentation
						.deserialize( new ByteArrayInputStream( values ) );
			}

			if ( queries == null )
			{
				DesignElementHandle elementHandle = design.getHandle( );
				if ( elementHandle instanceof ReportElementHandle )
				{
					queries = (IBaseQueryDefinition[]) context.getRunnable( )
							.getReportIR( ).getQueryByReportHandle(
									(ReportElementHandle) elementHandle );
				}
			}
			IBaseResultSet[] rsets = context.getResultSets();
			IBaseResultSet[] resultSets = null;
			if ( queries == null )
			{
				if ( rsets != null )
				{
					resultSets = new IBaseResultSet[1];
					int type = rsets[0].getType( );
					if ( IBaseResultSet.QUERY_RESULTSET == type )
					{
						resultSets[0] = new SingleQueryResultSet(
								(IQueryResultSet) rsets[0] );
					}
					else if ( IBaseResultSet.CUBE_RESULTSET == type )
					{
						resultSets[0] = new SingleCubeResultSet(
								(ICubeResultSet) rsets[0] );
					}
					else
					{
						throw new UnsupportedOperationException(
								"Unknown type of result set is found: "
										+ rsets[0].getClass( ).getName( ) );
					}
				}
			}
			else
			{
				resultSets = rsets;
			}
			
			try
			{
				Object output = itemPresentation.onRowSets( resultSets );
				if ( output != null )
				{
					int type = itemPresentation.getOutputType( );
					String imageMIMEType = itemPresentation.getImageMIMEType( );
					generatedContent = processExtendedContent( content, type, output, imageMIMEType );
					Size size = itemPresentation.getSize( );
					if ( size != null )
					{
						DimensionType height = new DimensionType( size.getHeight( ), size.getUnit( ) );
						DimensionType width = new DimensionType( size.getWidth( ), size.getUnit( ) );
						generatedContent.setHeight( height );
						generatedContent.setWidth( width );
					}
				}
				else
				{
					generatedContent = null;
				}
				itemPresentation.finish( );
			}
			catch ( BirtException ex )
			{
				context.addException( design.getHandle( ), ex );
				logger.log( Level.SEVERE, ex.getMessage( ), ex );
			}
		}
		if ( generatedContent instanceof IImageContent )
		{
			IImageContent imageContent = (IImageContent)generatedContent;
			imageContent.setResolution( resolution );
		}
		return generatedContent;
	}

	/**
	 * handle the content created by the IPresentation
	 * 
	 * @param item
	 *            extended item design
	 * @param emitter
	 *            emitter used to output the contnet
	 * @param content
	 *            ext content
	 * @param type
	 *            output type
	 * @param output
	 *            output
	 */
	protected IContent processExtendedContent( IForeignContent content, int type,
			Object output, String imageMIMEType )
	{
		assert IForeignContent.EXTERNAL_TYPE.equals( content.getRawType( ) );
		assert output != null;

		IReportContent reportContent = getReportContent( );
		
		switch ( type )
		{
			case IReportItemPresentation.OUTPUT_NONE :
				break;
			case IReportItemPresentation.OUTPUT_AS_IMAGE :
			case IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP :
				// the output object is a image, so create a image content
				// object
				Object imageMap = null;
				byte[] imageContent = new byte[0];

				Object image = output;
				if ( type == IReportItemPresentation.OUTPUT_AS_IMAGE_WITH_MAP )
				{
					// OUTPUT_AS_IMAGE_WITH_MAP
					Object[] imageWithMap = (Object[]) output;
					if ( imageWithMap.length > 0 )
					{
						image = imageWithMap[0];
					}
					if ( imageWithMap.length > 1 )
					{
						imageMap = imageWithMap[1];
					}
				}

				if ( image instanceof InputStream )
				{
					imageContent = readContent( (InputStream) image );
				}
				else if ( output instanceof byte[] )
				{
					imageContent = (byte[]) image;
				}
				else
				{
					assert false;
					logger.log( Level.WARNING,
							"unsupported image type:{0}", output ); //$NON-NLS-1$

				}

				IImageContent imageObj = reportContent.createImageContent( content );
				imageObj.setParent( content.getParent( ) );
				// Set image map
				imageObj.setImageSource( IImageContent.IMAGE_EXPRESSION );
				imageObj.setData( imageContent );
				imageObj.setImageMap( imageMap );
				imageObj.setMIMEType( imageMIMEType );
				imageObj.setAltText( content.getAltText( ) );
				imageObj.setAltTextKey( content.getAltTextKey( ) );
				
				// put the cached image into cache
				IHTMLImageHandler imageHandler = context.getImageHandler( );
				if ( imageHandler != null )
				{
					Image img = new Image( imageObj );
					img.setRenderOption( context.getRenderOption( ) );
					img.setReportRunnable( context.getRunnable( ) );
					String imageId = getImageCacheID( content );
					CachedImage cachedImage = imageHandler.addCachedImage(
							imageId, IImage.CUSTOM_IMAGE, img, context
									.getReportContext( ) );
					if ( cachedImage != null )
					{
						return processCachedImage( content, cachedImage );
					}
				}

				// don' have image cache, so handle it as a normal image
				processImage( imageObj );
				return imageObj;

			case IReportItemPresentation.OUTPUT_AS_CUSTOM :

				IDataContent dataObj = reportContent.createDataContent( content );
				dataObj.setValue( output );
				processData( dataObj );
				return dataObj;

			case IReportItemPresentation.OUTPUT_AS_HTML_TEXT :
				content.setRawType( IForeignContent.HTML_TYPE );
				content.setRawValue( output.toString( ) );
				return content;

			case IReportItemPresentation.OUTPUT_AS_TEXT :
				ITextContent textObj = reportContent.createTextContent( );
				textObj.setText( output.toString( ) );
				return textObj;
				
			default :
				assert false;
				logger.log( Level.WARNING, "unsupported output format:{0}", //$NON-NLS-1$
						new Integer( type ) );
		}
		return content;

	}

	/**
	 * read the content of input stream.
	 * 
	 * @param in
	 *            input content
	 * @return content in the stream.
	 */
	static protected byte[] readContent( InputStream in )
	{
		BufferedInputStream bin = in instanceof BufferedInputStream
				? (BufferedInputStream) in
				: new BufferedInputStream( in );
		ByteArrayOutputStream out = new ByteArrayOutputStream( 1024 );
		byte[] buffer = new byte[1024];
		int readSize = 0;
		try
		{
			readSize = bin.read( buffer );
			while ( readSize != -1 )
			{
				out.write( buffer, 0, readSize );
				readSize = bin.read( buffer );
			}
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
		}
		return out.toByteArray( );
	}
	
	protected void handleOnRender( IContent content )
	{
		if ( content.getGenerateBy( ) != null )
		{
			onRenderVisitor.onRender( content );
		}
	}
}