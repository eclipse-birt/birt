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

package org.eclipse.birt.report.engine.executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.content.IPageSetupContent;
import org.eclipse.birt.report.engine.content.IReportItemContent;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.mozilla.javascript.Scriptable;

/**
 * Captures the report execution context. This class is needed for accessing
 * global information during execution as well as for for scripting. It
 * implements the <code>report</code> Javascript object, as well as other
 * objects such as <code>report.params</code>,<code>report.config</code>,
 * <code>report.design</code>, etc.
 * 
 * @version $Revision: 1.7 $ $Date: 2005/03/03 22:15:34 $
 */
public class ExecutionContext implements IFactoryContext, IPrensentationContext
{

	// for logging
	protected static Logger log = Logger.getLogger( ExecutionContext.class.getName() );

	// The scripting context
	protected ScriptContext scriptContext;

	// data engine used by this context
	protected IDataEngine dataEngine;

	// the page setup information of the report
	private IPageSetupContent pageSetup;

	// the page sequence information of the section
	protected Stack pageInfoStack = new Stack( );

	// the default master page/page sequence of the report
	private String defaultMasterPage;

	// the current master page/page sequence of the report
	private String currentMasterPage;

	// A stack of accessible reportItems, with the current one on the top
	private Stack reportItems = new Stack( );

	// A stack of content objects, with the current one on the top
	private Stack reportContents = new Stack( );

	// Global configuration variables
	private Map configs = new HashMap( );

	// Report parameters
	private Map params = new HashMap( );

	// the report design
	protected Report report;

	// the current locale
	protected Locale locale;

	// A DOM Parser for parsing HTML
	protected TextParser parser;

	/**
	 * create a new context. Call close to finish using the execution context
	 * 
	 * @see close()
	 */
	public ExecutionContext( )
	{
		parser = new TextParser( );

		locale = Locale.getDefault( );
		scriptContext = new ScriptContext( );

		//create script context used to execute the script statements

		//register the global variables in the script context
		scriptContext.registerBean( "report", new ReportObject( ) );
		scriptContext.registerBean( "params", params );
		scriptContext.registerBean( "config", configs );
		dataEngine = DataEngineFactory.getInstance( ).createDataEngine( this );
	}

	/**
	 * @return whether the report item can define its own master page
	 */
	public boolean hasNewPage( )
	{
		assert !pageInfoStack.isEmpty( );
		return ( (ReportItemContext) pageInfoStack.peek( ) ).canDefineMasterPage;
	}

	/**
	 * Clean up the execution context before finishing using it
	 */
	public void close( )
	{
		scriptContext.exit( );
	}

	/**
	 * creates new variable scope.
	 */
	public void newScope( )
	{
		scriptContext.newScope( );
	}

	/**
	 * exits a variable scope.
	 */
	public void exitScope( )
	{
		scriptContext.exitScope( );
	}

	/**
	 * declares a variable in the current scope. The variable is then accessible
	 * through JavaScript.
	 * 
	 * @param name
	 *            variable name
	 * @param value
	 *            variable value
	 */
	public void registerBean( String name, Object value )
	{
		scriptContext.registerBean( name, value );
	}

	/**
	 * Evaluate a BIRT expression
	 * 
	 * @param source
	 *            the expression to be evaluated
	 * @return the result if no error exists, otherwise null.
	 * 
	 * @see evaluate(String,String,int)
	 */
	public Object evaluate( String source )
	{
		if ( source == null )
		{
			return null;
		}
		try
		{
			return scriptContext.eval( source );
		}
		catch ( Exception e )
		{
			// TODO eval may throw RuntimeException, which may also need
			// logging. May need to log more info.
			log.log( Level.SEVERE,e.getMessage(),  e );
		}
		return null;

	}

	/**
	 * @param jsValue
	 *            a Javascript object
	 * @return A Java object
	 */
	public Object jsToJava( Object jsValue )
	{
		return scriptContext.jsToJava( jsValue );
	}

	/**
	 * Evaluate a BIRT expression
	 * 
	 * @param expr
	 *            the expression to be evaluated
	 * @param name
	 *            the file name
	 * @param lineNo
	 *            the line number
	 * 
	 * @return the result if no error exists, otherwise null.
	 */
	public Object evaluate( String expr, String name, int lineNo )
	{
		if ( expr == null )
		{
			return null;
		}
		try
		{
			return scriptContext.eval( expr, name, lineNo );
		}
		catch ( Exception e )
		{
			// TODO eval may throw RuntimeException, which may also need
			// logging. May need to log more info.
		    log.log( Level.SEVERE,e.getMessage(),  e );
		}
		return null;
	}

	/**
	 * @param expr
	 *            an expression handle used to evaluate DtE expression
	 * @return the evaluated result of the expression
	 */
	public Object evaluate( IBaseExpression expr )
	{
		return dataEngine.evaluate( expr );
	}

	/**
	 * execute the script. simply evaluate the script, and drop the return value
	 * 
	 * @param script
	 *            script to be executed
	 */
	public void execute( String script )
	{
		if ( script != null )
		{
			evaluate( script );
		}
	}

	/**
	 * execute the script. Simply evaluate the script, then drop the return
	 * value
	 * 
	 * @param script
	 *            script statement
	 * @param fileName
	 *            file name
	 * @param lineNo
	 *            line no
	 */
	public void execute( String script, String fileName, int lineNo )
	{
		if ( script != null )
		{
			evaluate( script, fileName, lineNo );
		}
	}

	/**
	 * Push the master page
	 * 
	 * @param masterPage
	 *            the master page name
	 */
	public void pushMasterPage( String masterPage )
	{
		pageInfoStack.push( masterPage );
	}

	/**
	 * Pop the master page
	 * 
	 * @return the master page name
	 */
	public String popMasterPage( )
	{
		return (String) ( pageInfoStack.pop( ) );
	}

	/**
	 * Get the current peek master page name
	 * 
	 * @return the peek master page name
	 */
	public String peekMasterPage( )
	{
		return (String) ( pageInfoStack.peek( ) );
	}

	/**
	 * 
	 * inner class for report item context
	 */
	class ReportItemContext
	{

		/** refer to the design object for a report item */
		ReportItemDesign item;

		/** Is the item allowed to choose a different master page? */
		boolean canDefineMasterPage = false;

		/**
		 * Are the children of the item allowed to choose different master
		 * pages?
		 */
		boolean canChildrenDefineMasterPage = false;

		/** The master page that this element should be rendered with */
		String masterPage;

		/**
		 * Constructor
		 * 
		 * @param item
		 *            the reference to the report item design
		 */
		ReportItemContext( ReportItemDesign item )
		{
			this.item = item;
			//the top element in the stack as the default value
			if ( item == null )
			{
				canDefineMasterPage = true;
				canChildrenDefineMasterPage = true;
				return;
			}
			if ( ( (ReportItemContext) pageInfoStack.peek( ) ).canChildrenDefineMasterPage
					&& "block".equals( item.getStyle( ).getDisplay( ) ) )
			{
				canDefineMasterPage = true;
				//				if ( item instanceof ListItemDesign )
				//				{
				//					allowChildrenPage = true;
				//				}
			}
		}
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale( )
	{
		return locale;
	}

	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale( Locale locale )
	{
		this.locale = locale;
	}

	/**
	 * @return Returns the report.
	 */
	public Report getReport( )
	{
		return report;
	}

	/**
	 * @param report
	 *            The report to set.
	 */
	public void setReport( Report report )
	{
		this.report = report;
	}

	/**
	 * @return Returns the defaultMasterPage.
	 */
	public String getDefaultMasterPage( )
	{
		return defaultMasterPage;
	}

	/**
	 * @param defaultMasterPage
	 *            The defaultMasterPage to set.
	 */
	public void setDefaultMasterPage( String defaultMasterPage )
	{
		this.defaultMasterPage = defaultMasterPage;
	}

	/**
	 * @return Returns the pageSetup.
	 */
	public IPageSetupContent getPageSetup( )
	{
		return pageSetup;
	}

	/**
	 * @param pageSetup
	 *            The pageSetup to set.
	 */
	public void setPageSetup( IPageSetupContent pageSetup )
	{
		this.pageSetup = pageSetup;
	}

	/**
	 * @return Returns the currentMasterPage.
	 */
	public String getCurrentMasterPage( )
	{
		return currentMasterPage;
	}

	/**
	 * @param currentMasterPage
	 *            The currentMasterPage to set.
	 */
	public void setCurrentMasterPage( String currentMasterPage )
	{
		this.currentMasterPage = currentMasterPage;
	}

	/**
	 * @return Returns the parser.
	 */
	public TextParser getParser( )
	{
		return parser;
	}

	/**
	 * @return Returns the dataEngine.
	 */
	public IDataEngine getDataEngine( )
	{
		return dataEngine;
	}

	/**
	 * @param name
	 * @param value
	 */
	public void setParamter( String name, Object value )
	{
		params.put( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IFactoryContext#getConfigs()
	 */
	public Map getConfigs( )
	{
		return configs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IFactoryContext#getItemDesign()
	 */
	public ReportItemHandle getItemDesign( )
	{
		assert !reportItems.isEmpty( );
		return (ReportItemHandle) reportItems.peek( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IFactoryContext#getReportDesign()
	 */
	public ReportDesignHandle getDesign( )
	{
		return report.getReportDesign( ).handle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IPrensentationContext#getItemState()
	 */
	public IReportItemContent getItemState( )
	{
		return (IReportItemContent) reportContents.peek( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IPrensentationContext#getParams()
	 */
	public Map getParams( )
	{
		return params;
	}

	/**
	 * pushes a report item design handle onto report items stack, and set it as
	 * current item (the <code>this</code> object).
	 * 
	 * @param handle
	 *            the report item design handle
	 */
	public void pushReportItem( ReportItemHandle handle )
	{
		reportItems.push( handle );
		scriptContext.registerBean( "itemDesign", handle );
	}

	/**
	 * removes a report item design handle from the report items stack, and sets
	 * the next item on stack as the current item (the <code>this</code>
	 * object).
	 */
	public void popReportItem( )
	{
		reportItems.pop( );
		if ( !reportItems.isEmpty( ) )
		{
			scriptContext.registerBean( "itemDesign", reportItems.peek( ) );
		}
	}

	/** loaded script file names */
	protected HashSet loadedScripts = new HashSet( );

	/**
	 * Loads scripts that are stored in an external file. Used to support
	 * include-script. Each script file should be load only once. and the script
	 * in the file must be encoded in UTF-8.
	 * 
	 * @param fileName
	 *            script file name
	 */
	public void loadScript( String fileName )
	{
		//check if the script has been loaded.
		if ( loadedScripts.contains( fileName ) )
			return;

		//read the script in the file, and execution.
		try
		{
			File script = new File( report.getBasePath( ), fileName );
			FileInputStream in = new FileInputStream( script );
			byte[] buffer = new byte[in.available( )];
			in.read( buffer );
			execute( new String( buffer, "UTF-8" ), fileName, 1 );
			in.close( );
			loadedScripts.add( fileName );
		}
		catch ( IOException ex )
		{
		    log.log( Level.SEVERE, "loading external script file " + fileName + " failed.",
					ex );
			//TODO This is a fatal error. Should throw an exception.
		}
	}

	/**
	 * get current scope
	 * 
	 * @return scope object
	 */
	public Scriptable getScope( )
	{
		return scriptContext.getCurrentScope( );
	}

	/**
	 * report object is the script object used in the script context.
	 * 
	 * All infos can get from this object.
	 * 
	 *  
	 */
	private class ReportObject
	{

		/**
		 * get the report design handle
		 * 
		 * @return report design object.
		 */
		public ReportDesignHandle getDesign( )
		{
			return report.getReportDesign( ).handle( );
		}

		/**
		 * get the report document.
		 * 
		 * @return report document.
		 */
		public Object getDocument( )
		{
			return null;
		}

		/**
		 * @return a map of name/value pairs for all the parameters and their
		 *         values
		 */
		public Map getParmas( )
		{
			return params;
		}

		//TODO DTE should return the datasets and datasources.
		/**
		 * @return a set of data sets
		 */
		public SlotHandle getDataSets( )
		{
			return report.getReportDesign( ).handle( ).getDataSets( );
		}

		/**
		 * @return a set of data sources
		 */
		public SlotHandle getDataSources( )
		{
			return report.getReportDesign( ).handle( ).getDataSets( );
		}

		//TODO return the set of configuration files
		/**
		 * @return a map of name/value pairs for all the configuration variables
		 */
		public Map getConfig( )
		{
			return configs;
		}
	}

}