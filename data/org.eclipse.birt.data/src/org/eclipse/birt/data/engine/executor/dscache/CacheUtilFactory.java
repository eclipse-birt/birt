
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.dscache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DiskDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.IDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.MemoryDataSetCacheObject;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

class CacheUtilFactory
{
	/**
	 * 
	 * @param cacheObject
	 * @param rs
	 * @return
	 */
	public static ISaveUtil createSaveUtil( IDataSetCacheObject cacheObject , IResultClass rs)
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			return new DiskSaveUtil( ( (DiskDataSetCacheObject) cacheObject ).getDataFile( ),
					( (DiskDataSetCacheObject) cacheObject ).getMetaFile( ),
					rs );
		} else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return new MemorySaveUtil( (MemoryDataSetCacheObject)cacheObject, rs );
		}	
		return null;
	}
	
	/**
	 * 
	 * @param cacheObject
	 * @return
	 */
	public static ILoadUtil createLoadUtil( IDataSetCacheObject cacheObject )
	{
		if ( cacheObject instanceof DiskDataSetCacheObject )
		{
			return new DiskLoadUtil( ( (DiskDataSetCacheObject) cacheObject ).getDataFile( ),
					( (DiskDataSetCacheObject) cacheObject ).getMetaFile( ) );
		}
		else if ( cacheObject instanceof MemoryDataSetCacheObject )
		{
			return new MemoryLoadUtil( (MemoryDataSetCacheObject) cacheObject );
		}
		return null;
	}
	
	
	/**
	 * Util class to save the original data retrieved from ODA driver into cache
	 * file.
	 */
	private static class DiskSaveUtil implements ISaveUtil
	{
		private File file;
		private File metaFile;
		private FileOutputStream fos;
		private BufferedOutputStream bos;
		
		private IResultClass rsClass;
		private ResultObjectUtil roUtil;
		
		private int rowCount;
		
		/**
		 * @param file
		 * @param rsClass
		 */
		public DiskSaveUtil( File file, File metaFile, IResultClass rsClass )
		{
			assert file != null;
			assert metaFile != null;
			assert rsClass != null;

			this.file = file;
			this.file.deleteOnExit( );
			this.metaFile = metaFile;
			this.metaFile.deleteOnExit( );
			this.rsClass = rsClass;
			this.rowCount = 0;
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public void saveObject( IResultObject resultObject ) throws DataException
		{
			assert resultObject!=null;
			
			if ( roUtil == null )
			{				
				roUtil = ResultObjectUtil.newInstance( rsClass );
				try
				{
					fos = new FileOutputStream(file);
					bos = new BufferedOutputStream( fos );
				}
				catch ( FileNotFoundException e )
				{
					throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
							e );
				}
			}
			
			try
			{
				rowCount ++;
				roUtil.writeData( bos, resultObject );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{			
			try
			{
				if ( bos != null )
				{
					bos.close( );
					fos.close( );
				}
				
				FileOutputStream fos1 = new FileOutputStream( metaFile );
				BufferedOutputStream bos1 = new BufferedOutputStream( fos1 );

				// save the count of data
				IOUtil.writeInt( bos1, this.rowCount );
				// save the meta data of result
				Map metaMap = new HashMap( );
				populateDataSetRowMapping( metaMap );
				( (ResultClass) rsClass ).doSave( bos1, metaMap );

				bos1.close( );
				fos1.close( );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
						e );
			}			
		}

		/**
		 * 
		 * @param metaMap
		 * @throws DataException
		 */
		private void populateDataSetRowMapping( Map metaMap )
				throws DataException
		{
			for ( int i = 0; i < rsClass.getFieldCount( ); i++ )
				metaMap.put( rsClass.getFieldName( i + 1 ),
						new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( rsClass.getFieldName( i + 1 ) ) ) );
		}
	}
	
	/**
	 *
	 */
	private static class MemorySaveUtil implements ISaveUtil
	{
		private MemoryDataSetCacheObject cacheObject;
		
				/**
		 * @param file
		 * @param rsClass
		 */
		public MemorySaveUtil( MemoryDataSetCacheObject cacheObject, IResultClass rs )
		{
			assert cacheObject != null;

			this.cacheObject = cacheObject;
			this.cacheObject.setResultClass( rs );
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public void saveObject( IResultObject resultObject ) throws DataException
		{
			assert resultObject!=null;
			
			this.cacheObject.populateResult( resultObject );
			
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{			
		}
	}
	
	/**
	 * Util class to retrieve data from stored cache file.
	 */
	private static class DiskLoadUtil implements ILoadUtil
	{
		private File file;
		private File metaFile;
		
		private FileInputStream fis;
		private BufferedInputStream bis;
		
		private ResultObjectUtil roUtil;
		private IResultClass rsClass;
		private int rowCount;
		private int currIndex;
		
		/**
		 * @param file
		 * @param metaFile
		 */
		public DiskLoadUtil( File file, File metaFile )
		{
			assert file != null;
			assert metaFile != null;
			 
			this.file = file;
			this.metaFile = metaFile;
			this.rowCount = 0;
			this.currIndex = -1;
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public IResultObject loadObject( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			try
			{
				if ( currIndex == rowCount - 1 )
					return null;
				
				currIndex++;
				return roUtil.readData( bis, 1 )[0];
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public IResultClass loadResultClass( ) throws DataException
		{			
			if ( roUtil == null )
				init( );
			
			return this.rsClass;
		}
		
		/**
		 * @throws DataException
		 */
		private void init( ) throws DataException
		{
			try
			{
				FileInputStream fis1 = new FileInputStream( metaFile );
				BufferedInputStream bis1 = new BufferedInputStream( fis1 );

				rowCount = IOUtil.readInt( bis1 );
				rsClass = new ResultClass( bis1 );

				bis1.close( );
				fis1.close( );

				if ( rowCount > 0 )
				{
					roUtil = ResultObjectUtil.newInstance( rsClass );
					fis = new FileInputStream( file );
					bis = new BufferedInputStream( fis );
				}
			}
			catch ( FileNotFoundException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.DATASETCACHE_LOAD_ERROR,
						e );
			}
		}
		
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{
			if ( bis != null )
			{
				try
				{
					bis.close( );
					fis.close( );
				}
				catch ( IOException e )
				{
					// ignore;
				}
			}
		}
	}
	
	/**
	 * Util class to retrieve data from stored cache file.
	 */
	private static class MemoryLoadUtil implements ILoadUtil
	{
		private int currIndex;
		
		private MemoryDataSetCacheObject cacheObject;
		
		/**
		 * @param file
		 * @param metaFile
		 */
		public MemoryLoadUtil( MemoryDataSetCacheObject cacheObject )
		{
			assert cacheObject != null;
			this.cacheObject = cacheObject;
			this.currIndex = -1;
		}
		
		/**
		 * @param resultObject
		 * @throws DataException
		 */
		public IResultObject loadObject( ) throws DataException
		{			
			currIndex++;
			if ( currIndex >= this.cacheObject.getSize( ) )
				return null;
			return this.cacheObject.getResultObject( currIndex );
		}
		
		/**
		 * @return
		 * @throws DataException
		 */
		public IResultClass loadResultClass( ) throws DataException
		{			
			return this.cacheObject.getResultClass( );
		}
	
		/**
		 * @throws DataException
		 */
		public void close( ) throws DataException
		{
		}
	}

}
