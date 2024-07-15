/**
 * AppRealm.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.security;

import co.ipicorp.saas.core.web.security.AppRealm;

/**
 * AppRealm.
 * <<< Detail note.
 * @author hieumicro
 */
public class ConsumerRealm extends AppRealm {

    @Override
    public String getSaasAuthorizationCacheName() {
        return "PortalAuthorizationCache";
    }

    @Override
    public String getRealmName() {
        return "portalRealm";
    }

}