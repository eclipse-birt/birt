package org.eclipse.birt.report.engine.presentation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.eclipse.birt.report.engine.api.CachedImage;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
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
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.template.TemplateExecutor;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.RowSet;
import org.eclipse.birt.report.engine.extension.internal.SingleRowSet;
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
				IContent localChild = localize( child );
				if ( localChild != child )
				{
					// replace the child with the licallized child.
					children.set( i, localChild );

					// set the locallized child's parent as orient child's
					// parent.
					localChild.setParent( content );

					// copy all children of this child to its localized child,
					// also change all children's parent to this localized
					// child.
					Collection childrenOfLocalChild = localChild.getChildren( );
					Iterator iter = child.getChildren( ).iterator( );
					while ( iter.hasNext( ) )
					{
						IContent childOfChild = (IContent) iter.next( );
						if ( !childrenOfLocalChild.contains( childOfChild ) )
						{
							childOfChild.setParent( localChild );
							childrenOfLocalChild.add( childOfChild );
						}
					}
				}
				localizeAllChildren( localChild );
			}
		}
		return content;
	}

	public Object visitPage( IPageContent page, Object value )
	{
		return page;
	}
	
	protected TextTemplate parseTemplate( String text )
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
			context
					.addException( new EngineException( ex
							.getLocalizedMessage( ) ) );
			logger.log( Level.WARNING, ex.getMessage( ), ex );
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
				else if ( value instanceof Date )
				{
					DateFormatter fmt = context.getDateFormatter( style
							.getDateFormat( ) );
					text = fmt.format( (Date) value );
				}
				else
				{
					text = value.toString( );
				}
			}
		}
		//text can be null value after applying format
		if(text!=null)
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

		if ( IForeignContent.TEMPLATE_TYPE.equals( rawFormat ) )
		{
			handleOnRender( foreignContent );
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
			handleOnRender( foreignContent );
			ITextContent textContent = reportContent
					.createDataContent( foreignContent );
			textContent.setText( rawValue == null ? "" : rawValue.toString( ) ); //$NON-NLS-1$
			return textContent;
		}
		
		if ( IForeignContent.HTML_TYPE.equals( rawFormat ) )
		{
			handleOnRender( foreignContent );
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
			handleOnRender( foreignContent );
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
		
		handleOnRender( image );
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
			
			TextTemplate template = parseTemplate( text );

			String result = executeTemplate( template, rawValues );
			
			foreignContent.setRawType( IForeignContent.HTML_TYPE );
			foreignContent.setRawValue( result );
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
				String imageId = content.getInstanceID( ).toUniqueString( );
				CachedImage cachedImage = imageHandler.getCachedImage( imageId,
						IImage.CUSTOM_IMAGE, context.getReportContext( ) );
				if ( cachedImage != null )
				{
					IImageContent imageObj = getReportContent( )
							.createImageContent( content );
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
			}
		}

		// call the presentation peer to create the content object
		IReportItemPresentation itemPresentation = ExtensionManager
				.getInstance( ).createPresentationItem( tagName );
		if ( itemPresentation != null )
		{
			itemPresentation.setModelObject( handle );
			itemPresentation.setApplicationClassLoader( context
					.getApplicationClassLoader( ) );
			itemPresentation.setScriptContext( context.getReportContext( ) );
			IBaseQueryDefinition[] queries = (IBaseQueryDefinition[])design.getQueries( );
			itemPresentation.setReportQueries( queries );
			itemPresentation.setDynamicStyle( content.getComputedStyle( ) );
			Map appContext = context.getAppContext( );
			int resolution = 0;
			if ( appContext != null )
			{
				Object tmp = appContext.get( EngineConstants.APPCONTEXT_CHART_RESOLUTION );
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

			itemPresentation.setResolution( resolution );
			itemPresentation.setLocale( locale );

			String supportedImageFormats = "PNG;GIF;JPG;BMP;"; //$NON-NLS-1$
			IRenderOption renderOption = context.getRenderOption( );
			String formats = renderOption.getSupportedImageFormats( );
			if ( formats != null )
			{
				supportedImageFormats = formats;
			}
			itemPresentation.setSupportedImageFormats( supportedImageFormats ); // Default

			itemPresentation.setActionHandler( context.getActionHandler( ) );
			// value
			String outputFormat = getOutputFormat( );
			itemPresentation.setOutputFormat( outputFormat );

			Object rawValue = content.getRawValue( );
			if ( rawValue instanceof byte[] )
			{
				byte[] values = (byte[]) rawValue;
				itemPresentation
						.deserialize( new ByteArrayInputStream( values ) );
			}

			IRowSet[] rowSets = null;
			IBaseResultSet[] rsets = context.getResultSets();	
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
			if ( queries != null )
			{
				if ( rsets != null )
				{
					rowSets = new IRowSet[rsets.length];
					for ( int i = 0; i < rowSets.length; i++ )
					{
						rowSets[i] = new RowSet( context,
								(IQueryResultSet) rsets[i] );
					}
				}
			}
			else
			{
				if ( rsets != null )
				{
					rowSets = new IRowSet[1];
					rowSets[0] = new SingleRowSet( context,
							(IQueryResultSet) rsets[0] );
				}
			}

			try
			{
				Object output = itemPresentation.onRowSets( rowSets );
				if ( output != null )
				{
					int type = itemPresentation.getOutputType( );
					String imageMIMEType = itemPresentation.getImageMIMEType( );
					generatedContent = processExtendedContent( content, type, output, imageMIMEType );
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