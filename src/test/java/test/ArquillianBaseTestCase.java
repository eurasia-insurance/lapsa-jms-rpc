package test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;

import tech.lapsa.javax.jms.Constants;
import tech.lapsa.lapsa.arquillian.archive.ArchiveBuilderFactory;
import test.ejb.resources.DummyResources;

@RunWith(Arquillian.class)
public abstract class ArquillianBaseTestCase {

    private static final Archive<?> DEPLOYMENT = ArchiveBuilderFactory.newEarBuilder() //
	    .withModule(ArchiveBuilderFactory.newEjbBuilder() //
		    .withPackageOf(Constants.class, DummyResources.class) //
		    .withManifestFolder() //
		    .withTestManifestFolder() //
		    .build() //
		    .dumpingTo(System.out::println) //
	    )
	    .build() //
	    .dumpingTo(System.out::println) //
	    .asEnterpriseArchive();

    @Deployment(testable = true)
    public static Archive<?> createDeploymentEAR() {
	return DEPLOYMENT;
    }
}
