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

package org.eclipse.birt.core.archive;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;

public class DocArchiveLockManagerTest extends TestCase
{

    static final String LOCK_FILE_NAME = "./utest/lock.lck";
    static final int THREAD_COUNT = 20;

    @Ignore("ignore multple thread test")
	@Test
    public void testThread( )
    {
        TestTask task = new TestTask( );
        task.doTest( );
        assertTrue( task.errorThreads == 0 );
        assertTrue( !new File( LOCK_FILE_NAME ).exists( ) );
    }

    static void sleep( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( Exception ex )
        {

        }
    }

    static class TestTask
    {

        int errorThreads;
        int terminateThreads;

        public void doTest( )
        {
            LockRunnable[] runnables = new LockRunnable[THREAD_COUNT];
            for ( int i = 0; i < THREAD_COUNT; i++ )
            {
                runnables[i] = new LockRunnable( );
                new Thread( runnables[i] ).start( );
            }
            // waiting all threads terminates
            while ( terminateThreads != THREAD_COUNT )
            {
                terminateThreads = 0;
                errorThreads = 0;
                for ( int i = 0; i < THREAD_COUNT; i++ )
                {
                    if ( runnables[i].status == 1 )
                    {
                        terminateThreads++;
                        if ( runnables[i].lockTime == -1
                                || runnables[i].unlockTime == -1 )
                        {
                            errorThreads++;
                        }
                    }
                }
                if ( terminateThreads != THREAD_COUNT )
                {
                    sleep( 100 );
                }
            }
        }
    }

    static class LockRunnable implements Runnable
    {

        static long threadId = 0;
        IArchiveLockManager lockManager = ArchiveLockManager.getInstance( );
        int status;
        long lockTime = -1;
        long unlockTime = -1;
        long id = threadId++;

        public void run( )
        {
            try
            {
                Object lock = lockManager.lock( LOCK_FILE_NAME );
                lockTime = System.currentTimeMillis( );

                try
                {
                    Thread.sleep( (long) ( Math.random( ) * 1000 ) );
                }
                catch ( Exception e )
                {

                }
                lockManager.unlock( lock );
                unlockTime = System.currentTimeMillis( );
                System.out.println( id + " lock for "
                        + ( unlockTime - lockTime ) );
            }
            catch ( Throwable ex )
            {
                ex.printStackTrace( );
            }
            status = 1;
        }
    }

    /**
     * use the main to test mutiple VM
     * 
     * @param args
     */
    public static void main( String[] args ) throws Exception
    {
        TestTask task = new TestTask( );
        task.doTest( );
    }
}
