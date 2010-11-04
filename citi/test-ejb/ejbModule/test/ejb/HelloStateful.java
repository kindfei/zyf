package test.ejb;

import javax.ejb.Stateful;

@Stateful(name="HelloStateful", mappedName="HelloStateful")
public class HelloStateful extends HelloEJBImpl implements HelloStatefulRemote, HelloStatefulLocal {

}
