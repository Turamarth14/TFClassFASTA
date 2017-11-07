package BioinformatikGoettingen.TFClassFASTA;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TFClassFASTAApplication extends Application<TFClassFASTAConfiguration> {

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
    }

    @Override
    public void run(final TFClassFASTAConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
