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

package org.eclipse.birt.report.engine.javascript;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Enumeration;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.SecurityController;

public class ScriptUtil
{

	public static SecurityController createSecurityController( )
	{
		return new SecurityController( ) {

			public GeneratedClassLoader createClassLoader(
					final ClassLoader parentLoader, Object securityDomain )
			{
				final ProtectionDomain domain = (ProtectionDomain) securityDomain;
				return AccessController
						.doPrivileged( new PrivilegedAction<GeneratedClassLoader>( ) {

							public GeneratedClassLoader run( )
							{
								return new Loader( parentLoader, domain );
							}
						} );
			}
			
			public Object getDynamicSecurityDomain( Object securityDomain )
			{
				ProtectionDomain staticDomain = (ProtectionDomain) securityDomain;
				return getDynamicDomain( staticDomain );
			}

			public Object execWithDomain( Context cx, Scriptable scope,
					Script script, Object securityDomain )
			{
		        return script.exec( cx, scope );
			}

			private ProtectionDomain getDynamicDomain(
					ProtectionDomain staticDomain )
			{
				ContextPermissions permission = new ContextPermissions(
						staticDomain );
				ProtectionDomain contextDomain = new ProtectionDomain(
						null, permission );
				return contextDomain;
			}
		};
	}

	public static class Loader extends ClassLoader
			implements
				GeneratedClassLoader
	{

		private ProtectionDomain domain;
		private ClassLoader parent;

		Loader( ClassLoader parent, ProtectionDomain domain )
		{
			super( );
			this.domain = domain;
			this.parent = parent;
		}

		public Class loadClass( String name, boolean resolve )
				throws ClassNotFoundException
		{
			Class cl = findLoadedClass( name );
			if ( cl == null )
			{
				if ( parent != null )
				{
					cl = parent.loadClass( name );
				}
				else
				{
					cl = findSystemClass( name );
				}
			}
			if ( resolve )
			{
				resolveClass( cl );
			}
			return cl;
		}

		public Class defineClass( String name, byte[] data )
		{
			return super.defineClass( name, data, 0, data.length, domain );
		}

		public void linkClass( Class cl )
		{
			resolveClass( cl );
		}
	}

	// Represents permissions that permits an action only if it is permitted by
	// staticDomain and by security context of Java stack on the moment of
	// constructor invocation
	public static class ContextPermissions extends PermissionCollection
	{
		private AccessControlContext context;
		private PermissionCollection statisPermissions;
		private static final long serialVersionUID = -1721494496320750721L;

		public ContextPermissions( ProtectionDomain staticDomain )
		{
			context = AccessController.getContext( );
			if ( staticDomain != null )
			{
				statisPermissions = staticDomain.getPermissions( );
			}
			setReadOnly( );
		}

		public void add( Permission permission )
		{
			throw new RuntimeException( "NOT IMPLEMENTED" );
		}

		public boolean implies( Permission permission )
		{
			if ( statisPermissions != null )
			{
				if ( !statisPermissions.implies( permission ) )
				{
					return false;
				}
			}
			try
			{
				context.checkPermission( permission );
				return true;
			}
			catch ( AccessControlException ex )
			{
				return false;
			}
		}

		public Enumeration elements( )
		{
			return new Enumeration( ) {

				public boolean hasMoreElements( )
				{
					return false;
				}

				public Object nextElement( )
				{
					return null;
				}
			};
		}
	}
}
