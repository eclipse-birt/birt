/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.vm.ReportVM;
import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMContextData;
import org.eclipse.birt.report.debug.internal.core.vm.VMException;
import org.eclipse.birt.report.debug.internal.core.vm.VMListener;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;
import org.eclipse.birt.report.debug.internal.core.vm.js.JsLineBreakPoint;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

/**
 * Runner
 */
public class Runner implements Runnable, VMListener, VMConstants
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		ReportVM vm = new ReportVM( );

		Runner runner = new Runner( vm );

		runner.installBreakpoints( );

		Thread debugThread = new Thread( runner );

		debugThread.start( );

		while ( true )
		{
			try
			{
				synchronized ( runner.eventQueuq )
				{
					if ( runner.eventQueuq.size( ) > 0 )
					{
						Object event = runner.eventQueuq.remove( 0 );

						// if ( ( (String) event ).equals( "STARTED" ) )
						// {
						// vm.suspend( );
						// }
						// else
						if ( ( (String) event ).startsWith( "SUSPEND" ) )
						{

							System.out.println( "==== event processed: "
									+ event );

							if ( vm.isTerminated( ) )
							{
								break;
							}

							printValue( vm.evaluate( "c*20" ) );
							printValue( vm.evaluate( "b" ) );
							printValue( vm.evaluate( "this" ) );

							vm.resume( );
							// vm.suspend( );
							//vm.stepOut( );
						}
						else if ( ( (String) event ).equals( "TERMINATED" ) )
						{
							System.out.println( "==== process terminated event" );
							break;
						}
					}
				}

				Thread.sleep( 200 );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace( );
			}
		}

		System.out.println( "==== done." );

	}

	private ReportVM vm;

	List eventQueuq = new ArrayList( );

	Runner( ReportVM vm )
	{
		this.vm = vm;

		vm.addVMListener( this );
	}

	private void installBreakpoints( )
	{
		vm.addBreakPoint( new JsLineBreakPoint( "sec1", 1 ) );
	}

	public void handleEvent( int eventCode, VMContextData context )
	{
		System.out.println( "==== event received: "
				+ VMConstants.EVENT_NAMES[eventCode] );

		VMVariable[] vars = vm.getVariables( );

		for ( int i = 0; i < vars.length; i++ )
		{
			printVariable( vars[i] );
		}

		synchronized ( eventQueuq )
		{
			eventQueuq.add( VMConstants.EVENT_NAMES[eventCode] );
		}
	}

	private static void printVariable( VMVariable var )
	{
		System.out.println( "==== varable ["
				+ var.getName( )
				+ "]: "
				+ var.getValue( ).getValueString( ) );
	}

	private static void printValue( VMValue val )
	{
		System.out.println( "==== evaluated value ["
				+ val.getValueString( )
				+ "]" );
	}

	public void run( )
	{
		Context cx = Context.enter( );

		vm.attach( cx );

		Scriptable global = new ImporterTopLevel( );

		cx.evaluateString( global,
				"\r\nvar a = 2;\r\n  \r\nvar b = a*2;\r\n",
				"sec1",
				0,
				null );

		cx.evaluateString( global,
				"var a = 'ok';\r\nvar b = a;\r\n",
				"sec2",
				0,
				null );

		cx.evaluateString( global,
				"\r\n\r\nvar a = 2;\r\n\r\n\r\nvar b = a*2;\r\n",
				"sec1",
				0,
				null );

		vm.detach( cx );
	}
}
