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

package org.eclipse.birt.data.engine.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.PropertySecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Define in which context Data Engine is running. The context can be divided
 * into three types: generation, presentation and direct presentation. 
 */
public class DataEngineContext
{
	/** three defined mode */
	public final static int MODE_GENERATION = 1;
	public final static int MODE_PRESENTATION = 2;
	public final static int DIRECT_PRESENTATION  = 3;
	
	/**
	 * flow mode is required to check if the request received is from regular
	 * report execution,interactive viewing session or for data extraction
	 */
	public static enum DataEngineFlowMode{NORMAL,IV,DATA_EXTRACTION}
	private DataEngineFlowMode flowMode = DataEngineFlowMode.NORMAL;
	
	/**
	 * this is a special mode, that a query which is running on a report
	 * document also needs to be stored into the same report document. This is a
	 * update operation, and it is a combination mode of presentation and
	 * generation. One note is in running in this mode, the report document must
	 * be same for reading and writing.
	 */
	public final static int MODE_UPDATE = 4;
	
	/**
	 * AppContext and Data Set cache count setting decide whether cache is used,
	 * which is default value for data engine context.
	 */
	public final static int CACHE_USE_DEFAULT = 1;
	
	/**
	 * Do not use cache, regardless of data set cache setting
	 */
	public final static int CACHE_USE_DISABLE = 2;
	
	/**
	 * Always use cached data if available, disregard data set cache setting and
	 * AppContext. cachRowCount parameter decides cache count.
	 */
	public final static int CACHE_USE_ALWAYS = 3;
	
	public final static int CACHE_MODE_IN_MEMORY = 1;
	
	public final static int CACHE_MODE_IN_DISK = 2;
	
	/** some fields */
	private int mode;
	
	/**
	 * The generationOption is used for some specific doc generation case.
	 */
	private int generationOption;
	private Scriptable scope;	
	private IDocArchiveReader reader;
	private IDocArchiveWriter writer;
	private ULocale currentLocale;
	
	private boolean enableDashBoardMode = false;

	/** cacheCount field */
	private int cacheOption;
	private int cacheCount;

	private String tmpDir = PropertySecurity.getSystemProperty( "java.io.tmpdir" ); //$NON-NLS-1$
	private ClassLoader classLoader;
	
	/** stream id for internal use, don't use it externally */
	public final static int VERSION_INFO_STREAM = 11;
	
	public final static int DATASET_DATA_STREAM = 21;
	
	public final static int DATASET_META_STREAM = 22;
	
	public final static int DATASET_DATA_LEN_STREAM = 23;
	
	public final static int EXPR_VALUE_STREAM = 31;
	
	public final static int EXPR_META_STREAM = 32;
	
	public final static int EXPR_ROWLEN_STREAM = 33;
	
	public final static int GROUP_INFO_STREAM = 41;
	
	public final static int SUBQUERY_INFO_STREAM = 42;
	
	// current query definition
	public final static int QUERY_DEFN_STREAM = 43;
	
	// original query defintion
	public final static int ORIGINAL_QUERY_DEFN_STREAM = 44;
	
	// row index to the base rd
	public final static int ROW_INDEX_STREAM = 51;
	
	// manage query running on based rd
	public final static int QUERYID_INFO_STREAM = 61;
	
	// parent index to the base subquery rd
	public final static int SUBQUERY_PARENTINDEX_STREAM = 71;
	
	public final static int META_STREAM = 99;
	
	public final static int META_INDEX_STREAM = 100;
	
	public final static int NAMING_RELATION_STREAM = 101;
	
	public final static int PLS_GROUPLEVEL_STREAM = 102;
	
	public final static int AGGR_INDEX_STREAM = 103;
	
	public final static int AGGR_VALUE_STREAM = 104;
	
	public final static int COMBINED_AGGR_INDEX_STREAM = 105;
	
	public final static int COMBINED_AGGR_VALUE_STREAM = 106;
	
	public final static int META_SECURITY_STREAM = 109;
	
	@Deprecated
	public final static int ROW_SECURITY_STREAM = 110;
	
	public final static int ACL_COLLECTION_STREAM = 111;
	
	public final static int CUBE_META_SECURITY_STREAM = 112;
	
	public final static int DIMENSION_META_SECURITY_STREAM = 114;
	
	@Deprecated
	public final static int ROW_SECURITY_STREAM_INDEX = 115;
	
	public final static int EMPTY_NESTED_QUERY_ID = 116;
	
	public final static String QUERY_STARTING_ID = "/dataEngine/queryStartingID";
	
	public final static int PROGRESSIVE_VIEWING_GROUP_STREAM = 120;
	
	public final static int QUERY_ID_BASED_VERSIONING_STREAM = 130;
	
	public final static int DATAMODEL_TABULAR_CURSOR_DATA_STREAM = 131;
	
	public final static int DATAMODEL_TABULAR_CURSOR_INDEX_STREAM = 132;
	
	public final static int DATAMODEL_TABULAR_CURSOR_GROUP_INFO_STREAM = 133;
	
	public final static int DATAMODEL_IV_STREAM = 134;
	
	public final static int DATAMODEL_IV_TABULAR_CURSOR_DATA_STREAM = 135;

	public final static int DATAMODEL_IV_TABULAR_CURSOR_INDEX_STREAM = 136;

	public final static int DATAMODEL_IV_TABULAR_CURSOR_GROUP_INFO_STREAM = 137;
	
	private static Logger logger = Logger.getLogger( DataEngineContext.class.getName( ) );
	
	private ScriptContext scriptContext;
	private TimeZone currentTimeZone;
	private String bundleVersion; // the bundle version of report engine
	
	/**
	 * When mode is MODE_GENERATION, the writer stream of archive will be used.
	 * When mode is MODE_PRESENTATION, the reader stream of archive will be used.
	 * When mode is DIRECT_PRESENTATION, the archive will not be used.
	 * When mode is PRESENTATION_AND_GENERATION, both the write stream and the read 
	 * steram of archive will be used. 
	 * @deprecated
	 * @param mode
	 * @param scope
	 * @param reader
	 * @param writer
	 * @param the ClassLoader used for this data engine.
	 * @return an instance of DataEngineContext
	 */
	public static DataEngineContext newInstance( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer, ClassLoader classLoader )
			throws BirtException
	{
		return new DataEngineContext( mode, scope, reader, writer, classLoader, null );
	}
	
	/**
	 * @deprecated
	 * @param mode
	 * @param scope
	 * @param reader
	 * @param writer
	 * @return
	 * @throws BirtException
	 * @deprecated
	 */
	public static DataEngineContext newInstance( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer )
			throws BirtException
	{
		ScriptContext context = new ScriptContext().newContext( scope );
		DataEngineContext result = newInstance( mode, context, reader, writer, null );
		return result;
	}
	
	public static DataEngineContext newInstance( int mode,
			ScriptContext context, IDocArchiveReader reader,
			IDocArchiveWriter writer, ClassLoader classLoader ) throws BirtException
	{
		IDataScriptEngine dse = (IDataScriptEngine) context.getScriptEngine( IDataScriptEngine.ENGINE_NAME );
		DataEngineContext result = new DataEngineContext( mode,
				dse.getJSScope( context ),
				reader,
				writer,
				classLoader, context );
		return result;	
	}
	
	/**	
	 * @param mode
	 * @param scope
	 * @param reader
	 * @param writer
	 * @throws BirtException
	 */
	public DataEngineContext( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer, ClassLoader classLoader, ScriptContext context )
			throws BirtException
	{
		Object[] params = {
				Integer.valueOf( mode ), scope, reader, writer, classLoader
		};
		logger.entering( DataEngineContext.class.getName( ),
				"DataEngineContext", //$NON-NLS-1$
				params );
		
		if ( !( mode == MODE_GENERATION
				|| mode == MODE_PRESENTATION || mode == DIRECT_PRESENTATION || mode == MODE_UPDATE ) )
			throw new DataException( ResourceConstants.RD_INVALID_MODE );

		if ( writer == null && mode == MODE_GENERATION )
			throw new DataException( ResourceConstants.RD_INVALID_ARCHIVE );

		if ( reader == null && mode == MODE_PRESENTATION )
			throw new DataException( ResourceConstants.RD_INVALID_ARCHIVE );

		if ( reader == null && mode == MODE_UPDATE )
			throw new DataException( ResourceConstants.RD_INVALID_ARCHIVE );

		this.classLoader = classLoader;
		this.mode = mode;
		this.scope = scope;
		this.reader = reader;
		this.writer = writer;
		this.cacheOption = CACHE_USE_DEFAULT;
		this.scriptContext = context;
		this.currentLocale = ULocale.getDefault( );
		this.currentTimeZone = TimeZone.getDefault( );
		logger.exiting( DataEngineContext.class.getName( ), "DataEngineContext" ); //$NON-NLS-1$
	}

	/** 
	 * @return current context mode
	 */
	public int getMode( )
	{
		return mode;
	}
	
	/**
	 * 
	 * @param mode
	 */
	public void setMode( int mode )
	{
		this.mode = mode;
	}

	/**
	 * @return current top scope
	 */
	public Scriptable getJavaScriptScope( )
	{
		return scope;
	}

	/**
	 * @return cacheCount
	 */
	public int getCacheOption( )
	{
		return this.cacheOption;
	}
	
	/**
	 * @return cacheCount
	 */
	public int getCacheCount( )
	{
		return this.cacheCount;
	}

	/**
	 * This method is used to set the cache option for current data engine
	 * instance. These option values will override the values defined in
	 * individual data set and its application context. The option value has
	 * three posible values, CACHE_USE_DEFAULT, CACHE_USE_DISABLE,
	 * CACHE_USE_ALWAYS. The cacheCount values can be larger than 0, which
	 * indicates the count of how many rows will be ccached, equal to 0, which
	 * indicates cache will not be used, less than 0, which indicates the entire
	 * data set will be cached.
	 * 
	 * Please notice, this cache function only available for
	 * DIRECT_PRESENTATION. In other cases, exception will be thrown.
	 * 
	 * @param option
	 * @param cacheCount
	 */
	public void setCacheOption( int option, int cacheCount )
			throws BirtException
	{
		if ( this.mode != DIRECT_PRESENTATION )
			throw new DataException( ResourceConstants.CACHE_FUNCTION_WRONG_MODE );

		this.cacheOption = option;
		this.cacheCount = cacheCount;
	}
	
	/**
	 * According to the paramters of streamID, subStreamID and streamType, an
	 * output stream will be created for it. To make stream close simply, the
	 * stream needs to be closed by caller, and then caller requires to add
	 * buffer stream layer when needed.
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return output stream for specified streamID, subStreamID and streamType
	 */
	public RAOutputStream getOutputStream( String streamID, String subStreamID,
			int streamType ) throws DataException
	{
		assert writer != null;
		
		String relativePath = getPath( streamID, subStreamID, streamType );
		
		return openOutputStream( relativePath );
	}
	
	private RAOutputStream openOutputStream( String relativePath ) throws DataException
	{
		try
		{
			RAOutputStream outputStream = writer.openRandomAccessStream( relativePath );

			if ( outputStream == null )
				throw new DataException( ResourceConstants.RD_SAVE_STREAM_ERROR );

			return outputStream;
		}
		catch (IOException e)
		{
			throw new DataException( ResourceConstants.RD_SAVE_STREAM_ERROR, e );
		}
	}
	
	public RAOutputStream getOutputStream( String streamID, String subStreamID,
			int streamType, String subName ) throws DataException
	{
		assert writer != null;
		
		String relativePath = getPath( streamID, subStreamID, streamType, subName );
		
		return openOutputStream( relativePath );
	}

	/**
	 * Determins whether one particular stream exists
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return boolean value
	 */
	public boolean hasOutStream( String streamID, String subStreamID,
			int streamType )
	{
		String relativePath = getPath( streamID, subStreamID, streamType );

		if ( writer != null )
			return writer.exists( relativePath );
		else
			return false;
	}
	
	/**
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return
	 */
	public boolean hasInStream( String streamID, String subStreamID,
			int streamType )
	{
		String relativePath = getPath( streamID, subStreamID, streamType );

		if ( reader != null && reader.exists( relativePath ) )
			return true;
		else if ( writer!= null && writer.exists( relativePath ))
			return true;
		return false;
	}
	
	public boolean hasInStream( String streamID, String subStreamID, int streamType, String subname )
	{
		String relativePath = getPath( streamID, subStreamID, streamType, subname );

		if ( reader != null && reader.exists( relativePath ) )
			return true;
		else if ( writer!= null && writer.exists( relativePath ))
			return true;
		return false;
	}
	
	/**
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 */
	public void dropStream( String streamID, String subStreamID, int streamType )
	{
		String relativePath = getPath( streamID, subStreamID, streamType );
		
		this.dropStream( relativePath );
	}
	
	public void dropStream( String streamID, String subStreamID, int streamType, String subName )
	{
		String relativePath = getPath( streamID, subStreamID, streamType, subName );
		
		this.dropStream( relativePath );
	}
	
	/**
	 * Directly drop stream
	 * 
	 * @param streamPath
	 */
	public void dropStream( String streamPath )
	{
		if ( writer != null )
		{
			try
			{
				if( !writer.exists( streamPath ))
					return;
				//If a stream exists in ArchiveFile but not exists in ArchiveView
				//the call to IDocArchiveWriter.dropStream() would not remove the stream
				//from ArchiveFile. So we've to first create a stream of same path in
				//ArchiveView (so that it can automatically replace the on in ArchiveFile) 
				OutputStream stream = writer.createRandomAccessStream( streamPath );
				stream.close( );
				
				//We should not try to drop stream here, because this may expose the real stream in 
				//archive file (read only), in case the stream in archive view is dropped. 
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * According to the paramters of streamID, subStreamID and streamType, an
	 * input stream will be created for it. To make stream close simply, the
	 * stream needs to be closed by caller, and then caller requires to add
	 * buffer stream layer when needed.
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return input stream for specified streamID, subStreamID and streamType
	 */
	public RAInputStream getInputStream( String streamID, String subStreamID,
			int streamType ) throws DataException
	{
		//assert reader != null;

		String relativePath = getPath( streamID, subStreamID, streamType );
		
		return getInStream( relativePath );
	}
	
	private RAInputStream getInStream( String relativePath ) throws DataException
	{
		try
		{
			RAInputStream inputStream = null;
			
			if ( reader != null && reader.exists( relativePath ))
			{	
			  inputStream = reader.getStream( relativePath );
			}
			else if ( writer != null && writer.exists( relativePath ) )
			{
				inputStream = writer.getInputStream( relativePath );
			}
			
			if ( inputStream == null )
			{
				throw new DataException( ResourceConstants.RD_LOAD_STREAM_ERROR );
			}
			
			return inputStream;
		}
		catch (IOException e)
		{
			throw new DataException( ResourceConstants.RD_LOAD_STREAM_ERROR, e );
		}
	}
	
	public RAInputStream getInputStream( String streamID, String subStreamID,
			int streamType, String subname ) throws DataException
	{
		String relativePath = getPath( streamID, subStreamID, streamType, subname );
		
		return getInStream( relativePath );
	}
	
	/**
	 * @param locale
	 *            The current task's locale
	 */
	public void setLocale( Locale locale )
	{
		currentLocale = ULocale.forLocale( locale );
		DataException.setLocale( currentLocale );

	}

	/**
	 * set time zone
	 * @param zone
	 */
	public void setTimeZone( TimeZone zone )
	{
		currentTimeZone = zone;
	}
	
	/**
	 * @return time zone
	 */
	public TimeZone getTimeZone( )
	{
		return this.currentTimeZone;
	}
	
	/**
	 * @return The current locale
	 */
	public ULocale getLocale( )
	{
		return currentLocale;
	}
	
	/**
	 * get Dte temporary dir.
	 * @return
	 */
	public String getTmpdir( )
	{
		if( !tmpDir.endsWith( File.separator ))
		{
			return tmpDir + File.separator;
		}
		return tmpDir;
	}

	/**
	 * set Dte temporary dir.
	 * @param tmpdir
	 */
	public void setTmpdir( String tmpdir )
	{
		this.tmpDir = tmpdir;
		DataEngineThreadLocal.getInstance( ).getPathManager( ).setTempPath( getTmpdir( ) );
	}

	public void setDataEngineOption( int option )
	{
		this.generationOption = option;
	}
	
	public int getDataEngineOption( )
	{
		return this.generationOption;
	}
	
	/**
	 * Set the classloader.
	 * 
	 * @param classLoader
	 */
	public void setClassLoader ( ClassLoader classLoader )
	{
		this.classLoader = classLoader;
	}
	
	/**
	 * 
	 * @return
	 */
	public IDocArchiveReader getDocReader()
	{
		return this.reader;
	}
	
	/**
	 * 
	 * @return
	 */
	public IDocArchiveWriter getDocWriter()
	{
		return this.writer;
	}
	
	public void setDocReader( IDocArchiveReader reader )
	{
		this.reader = reader;
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void enableDashboardMode( boolean enabled )
	{
		this.enableDashBoardMode = enabled;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDashBoardEnabled( )
	{
		return this.enableDashBoardMode;
	}
	
	/**
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader( )
	{
		return this.classLoader;
	}
	
	/**
	 * @param streamType
	 * @return relative path, notice in reading data from file, directory can
	 *         not be created.
	 */
	public static String getPath( String streamID, String subStreamID, int streamType )
	{
		String relativePath = null;
		switch ( streamType )
		{
			case VERSION_INFO_STREAM :
				return "/DataEngine/VesionInfo"; //$NON-NLS-1$
			case NAMING_RELATION_STREAM :
				return "/DataEngine/NamingRelation"; //$NON-NLS-1$
				
			case DATASET_DATA_STREAM :
				relativePath = "DataSetData"; //$NON-NLS-1$
				break;
			case DATASET_META_STREAM :
				relativePath = "ResultClass"; //$NON-NLS-1$
				break;
			case DATASET_DATA_LEN_STREAM :
				relativePath = "DataSetLens"; //$NON-NLS-1$
				break;
			case EXPR_VALUE_STREAM :
				relativePath = "ExprValue"; //$NON-NLS-1$
				break;
			case EXPR_ROWLEN_STREAM :
				relativePath = "ExprRowLen"; //$NON-NLS-1$
				break;
			case EXPR_META_STREAM :
				relativePath = "ExprMetaInfo"; //$NON-NLS-1$
				break;
			case GROUP_INFO_STREAM :
				relativePath = "GroupInfo"; //$NON-NLS-1$
				break;
			case SUBQUERY_INFO_STREAM :
				relativePath = "SubQueryInfo"; //$NON-NLS-1$
				break;
			case QUERY_DEFN_STREAM :
				relativePath = "QueryDefn"; //$NON-NLS-1$
				break;
			case ORIGINAL_QUERY_DEFN_STREAM:
				relativePath = "OriginalQueryDefn"; //$NON-NLS-1$
				break;
			case ROW_INDEX_STREAM:
				relativePath = "RowIndex"; //$NON-NLS-1$
				break;
			case QUERYID_INFO_STREAM :
				relativePath = "QueryIDInfo"; //$NON-NLS-1$
				break;
			case SUBQUERY_PARENTINDEX_STREAM :
				relativePath = "ParentIndex"; //$NON-NLS-1$
				break;
			case META_STREAM :
				relativePath = "Meta"; //$NON-NLS-1$
				break;
			case META_INDEX_STREAM :
				relativePath = "MetaIndex"; //$NON-NLS-1$
				break;
			case PLS_GROUPLEVEL_STREAM:
				relativePath = "PlsGroupLevel";
				break;
			case AGGR_INDEX_STREAM:
				relativePath = "AggrIndex";
				break;
			case AGGR_VALUE_STREAM:
				relativePath = "AggrValue";
				break;
			case COMBINED_AGGR_INDEX_STREAM:
				relativePath = "CombinedAggrIndex";
				break;
			case COMBINED_AGGR_VALUE_STREAM:
				relativePath = "CombinedAggrValue";
				break;
			case META_SECURITY_STREAM:
				relativePath = "MetaSecurity";
				break;
			case ROW_SECURITY_STREAM:
				relativePath = "RowSecurity";
				break;
			case ROW_SECURITY_STREAM_INDEX:
				relativePath = "RowSecurityIndex";
				break;
			case CUBE_META_SECURITY_STREAM:
				relativePath ="CubeMetaSecurity";
				break;
			case ACL_COLLECTION_STREAM:
				relativePath = "ACLCollection";
				break;
			case DIMENSION_META_SECURITY_STREAM:
				relativePath = "DimensionSecurity";
				break;
			case PROGRESSIVE_VIEWING_GROUP_STREAM:
				relativePath = "ProgressiveViewingGroupInfo";
				break;
			case EMPTY_NESTED_QUERY_ID:
				relativePath = "EmptyNestQueryResultIDs";
				break;
			case QUERY_ID_BASED_VERSIONING_STREAM:
				return "/DataEngine/QueryIdBasedVersioningStream";
			case DATAMODEL_TABULAR_CURSOR_DATA_STREAM:	
				relativePath = "TabularCursorData";
				break;
			case DATAMODEL_TABULAR_CURSOR_INDEX_STREAM:
				relativePath = "TabularCursorDataIndex";
				break;
			case DATAMODEL_TABULAR_CURSOR_GROUP_INFO_STREAM:
				relativePath = "TabularCursorDataRowId2BreakGroupLevel";
				break;
			case DATAMODEL_IV_STREAM:
				relativePath = "IV";
				break;
			case DATAMODEL_IV_TABULAR_CURSOR_DATA_STREAM:
				relativePath = "IV/TabularCursorData";
				break;
				
			case DATAMODEL_IV_TABULAR_CURSOR_INDEX_STREAM:
				relativePath = "IV/TabularCursorDataIndex";
				break;
				
			case DATAMODEL_IV_TABULAR_CURSOR_GROUP_INFO_STREAM:
				relativePath = "IV/TabularCursorDataRowId2BreakGroupLevel";
				break;
			default :
				assert false; // impossible
		}
		
		String streamRoot = "/" + streamID + "/"; //$NON-NLS-1$ //$NON-NLS-2$
		if ( subStreamID != null )
			streamRoot += subStreamID + "/"; //$NON-NLS-1$
		return streamRoot + relativePath;
	}	
	
	public static String getPath( String streamID, String subStreamID, int streamType, String subName )
	{
		String path = getPath( streamID, subStreamID, streamType );
		if ( subName != null && subName.length( ) > 0 )
			path += "/" + subName; //$NON-NLS-1$
		return path;
	}

	/**
	 * 
	 * @return
	 */
	public ScriptContext getScriptContext( )
	{
		if ( this.scriptContext == null )
			this.scriptContext = new ScriptContext( );
		return this.scriptContext;
	}
	
	public void setBundleVersion( String bundleVersion )
	{
		this.bundleVersion = bundleVersion;
	}
	
	public String getBundleVersion( )
	{
		return this.bundleVersion;
	}	
	
	public void setFlowMode(DataEngineFlowMode flowMode){
		this.flowMode = flowMode;
	}
	
	public DataEngineFlowMode getFlowMode(){
		return flowMode;
	}
}
