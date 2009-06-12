/**
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package samples.jms.jbossmessaging.statelessclustered.bean;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
/**
 * @author <a href="mailto:ovidiu@jboss.org">Ovidiu Feodorov</a>
 * @version <tt>$Revision: 2742 $</tt>

 * $Id: StatelessClusteredSessionExampleHome.java 2742 2007-05-30 17:07:08Z sergeypk $
 */

public interface StatelessClusteredSessionExampleHome extends EJBHome
{
   public StatelessClusteredSessionExample create() throws RemoteException, CreateException;
}


