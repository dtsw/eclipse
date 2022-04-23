package company.eventprocessor;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMFConfig {

    private static final PersistenceManagerFactory PERSISTENCE_MANAGER_FACTORY = JDOHelper.getPersistenceManagerFactory("events");

    public static PersistenceManagerFactory getPersistenceManagerFactory() {
        return PERSISTENCE_MANAGER_FACTORY;
    }
}
