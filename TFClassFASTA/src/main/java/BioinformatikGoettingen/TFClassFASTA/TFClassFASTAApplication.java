package BioinformatikGoettingen.TFClassFASTA;

import com.google.common.base.Charsets;

import BioinformatikGoettingen.TFClassFASTA.core.Fasta;
import BioinformatikGoettingen.TFClassFASTA.db.FastaDAO;
import BioinformatikGoettingen.TFClassFASTA.resources.FastaResource;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TFClassFASTAApplication extends Application<TFClassFASTAConfiguration> {
	
	private final HibernateBundle<TFClassFASTAConfiguration> hibernate = new HibernateBundle<TFClassFASTAConfiguration>(Fasta.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(TFClassFASTAConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};
	
    public static void main(final String[] args) throws Exception {
        new TFClassFASTAApplication().run(args);
    }

    @Override
    public String getName() {
        return "TFClassFASTA";
    }

    @Override
    public void initialize(final Bootstrap<TFClassFASTAConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<TFClassFASTAConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(TFClassFASTAConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}        	
        });
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new TemplateConfigBundle(new TemplateConfigBundleConfiguration().charset(Charsets.US_ASCII)));
    }

    @Override
    public void run(final TFClassFASTAConfiguration configuration,
                    final Environment environment) {
        final FastaDAO fastaDAO = new FastaDAO(hibernate.getSessionFactory());
        environment.jersey().register(new FastaResource(fastaDAO,environment));
    }

}
