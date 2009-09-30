/**
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package demo.jms.jbossmessaging.statelessclustered.bean;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * @author <a href="mailto:ovidiu@jboss.org">Ovidiu Feodorov</a>
 * @version <tt>$Revision: 2742 $</tt>

 * $Id: StatelessClusteredSessionExample.java 2742 2007-05-30 17:07:08Z sergeypk $
 */

public interface StatelessClusteredSessionExample extends EJBObject
{
   public void drain(String queueName) throws RemoteException, Exception;
   
   public void send(String txt, String queueName) throws RemoteException, Exception;
   
   public int browse(String queueName) throws RemoteException, Exception;
   
   public String receive(String queueName) throws RemoteException, Exception;
}