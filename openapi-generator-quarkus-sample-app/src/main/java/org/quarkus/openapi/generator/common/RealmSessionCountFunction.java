package org.quarkus.openapi.generator.common;

import java.util.Map;
import java.util.function.ToDoubleFunction;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.resources.KeycloakApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealmSessionCountFunction implements ToDoubleFunction<String> {

    private static final Logger log = LoggerFactory.getLogger(RealmSessionCountFunction.class);

    @Override
    public double applyAsDouble(String realmName) {
        double sessionCount = 0.0;
        KeycloakSession session = null;

        try {
            session = KeycloakApplication.getSessionFactory().create();
            RealmModel realm = session.realms().getRealmByName(realmName);

            Map<String, Long> activeClientSessionStats = session.sessions()
                .getActiveClientSessionStats(realm, false);

            sessionCount = activeClientSessionStats.values()
                .stream()
                .mapToDouble(Long::doubleValue)
                .sum();

        } catch (IllegalStateException ex) {
            log.warn("Can't calculate total sessions count for realm: {}", realmName);
            // Found this can occur when metrics are collected before any sessions are established, presumably as
            // There are no active transactions nothing works.  Once one person has logged in it doesn't happen.
            // Recommend just squelching this as will return 0 sessions anyway.
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return sessionCount;
    }
}
