package BioinformatikGoettingen.TFClassFASTA;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

import org.hibernate.validator.constraints.*;

<<<<<<< HEAD
import javax.validation.Valid;
import javax.validation.constraints.*;
=======
>>>>>>> branch 'master' of https://github.com/Turamarth14/TFClassFASTA.git

public class TFClassFASTAConfiguration extends Configuration {
    @Valid
<<<<<<< HEAD
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @NotEmpty
    private String template = "test";

    @NotEmpty
    private String defaultName = "iBB";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
    
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
