package test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;

import ejb.resources.DummyResources;
import tech.lapsa.lapsa.arquillian.archive.ArchiveBuilderFactory;
import tech.lapsa.lapsa.jmsRPC.VoidResult;

@RunWith(Arquillian.class)
public abstract class ArquillianBaseTestCase {

    private static final Archive<?> DEPLOYMENT = ArchiveBuilderFactory.newEarBuilder() //
	    .withModule(ArchiveBuilderFactory.newEjbBuilder() //
		    .withPackageOf(VoidResult.class, DummyResources.class) //
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
