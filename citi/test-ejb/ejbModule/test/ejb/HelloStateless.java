package test.ejb;

import javax.ejb.Stateless;

@Stateless(name="HelloStateless", mappedName="HelloStateless")
public class HelloStateless extends HelloEJBImpl implements HelloStatelessRemote, HelloStatelessLocal {

}
