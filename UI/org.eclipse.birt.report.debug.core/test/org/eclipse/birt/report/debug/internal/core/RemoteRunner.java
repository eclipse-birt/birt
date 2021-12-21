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

import org.eclipse.birt.report.debug.internal.core.vm.ReportVMClient;
import org.eclipse.birt.report.debug.internal.core.vm.ReportVMServer;
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
 * RemoteRunner
 */
public class RemoteRunner implements Runnable, VMConstants, VMListener
{

	private ReportVMServer server;
	private ReportVMClient client;

	public static void main( String[] args )
	{
		RemoteRunner runner = new RemoteRunner( );

		Thread debugthread = new Thread( runner, "Debug Thread" );

		debugthread.start( );

		System.out.println( "start client" );

		ReportVMClient client = new ReportVMClient( );

		runner.client = client;

		client.addVMListener( runner );

		runner.installBreakpoints( );

		try
		{
			// Thread.sleep( 2000 );

			client.connect( 10000 );

			System.out.println( "client connected" );

		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	private void installBreakpoints( )
	{
		try
		{
			client.addBreakPoint( new JsLineBreakPoint( "sec1", 1 ) );
		}
		catch ( VMException e )
		{
			e.printStackTrace( );
		}
	}

	private static void printVariable( VMVariable var )
	{
		System.out.println( "==== varable ["
				+ var.getName( )
				+ "]: "
				+ var.getValue( ).getValueString( ) );

		VMVariable[] ms = var.getValue( ).getMembers( );

		for ( int i = 0; i < ms.length; i++ )
		{
			System.out.println( "==== member ["
					+ var.getName( )
					+ "."
					+ ms[i].getName( )
					+ "]: "
					+ ms[i].getValue( ).getValueString( ) );
		}
	}

	private static void printValue( String name, VMValue val )
	{
		System.out.println( "==== evaluate ["
				+ name
				+ "]: "
				+ val.getValueString( ) );

		VMVariable[] ms = val.getMembers( );

		for ( int i = 0; i < ms.length; i++ )
		{
			System.out.println( "==== member ["
					+ name
					+ "."
					+ ms[i].getName( )
					+ "]: "
					+ ms[i].getValue( ).getValueString( ) );
		}
	}

	public void handleEvent( int eventCode, VMContextData context )
	{
		System.out.println( "client event processed: "
				+ VMConstants.EVENT_NAMES[eventCode] );

		try
		{
			if ( eventCode == VM_STARTED )
			{
				client.resume( );
			}
			else if ( eventCode == VM_TERMINATED )
			{
				client.disconnect( );
			}
			else if ( eventCode == VM_SUSPENDED_BREAKPOINT
					|| eventCode == VM_SUSPENDED_CLIENT )
			{
				VMVariable[] vars = client.getVariables( );

				for ( int i = 0; i < vars.length; i++ )
				{
					printVariable( vars[i] );
				}

				printValue( "this", client.evaluate( "this" ) );
				printValue( "this.a", client.evaluate( "this.a" ) );

				client.resume( );
			}
			else
			{
				// client.resume( );
			}
		}
		catch ( VMException e )
		{
			e.printStackTrace( );
		}

	}

	public void run( )
	{
		try
		{
			System.out.println( "start server" );

			server = new ReportVMServer( );

			Context cx = Context.enter( );

			server.start( 10000, cx );

			System.out.println( "server started" );

			Scriptable global = new ImporterTopLevel( );

			cx.evaluateString( global,
					"var a = 2;\r\nvar b = a*2;\r\n",
					"sec1",
					0,
					null );

			cx.evaluateString( global,
					"var a = 'ok';\r\nvar b = a;\r\n",
					"sec2",
					0,
					null );

			cx.evaluateString( global,
					"\r\nvar a = 2;\r\nvar b = a*2;\r\n",
					"sec1",
					0,
					null );

			server.shutdown( cx );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

}
