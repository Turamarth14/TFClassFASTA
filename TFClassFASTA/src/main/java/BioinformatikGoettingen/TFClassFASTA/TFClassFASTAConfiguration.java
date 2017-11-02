package BioinformatikGoettingen.TFClassFASTA;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;


public class TFClassFASTAConfiguration extends Configuration {
    @Valid
    @NotEmpty
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory(){
        return database;
    }
}
