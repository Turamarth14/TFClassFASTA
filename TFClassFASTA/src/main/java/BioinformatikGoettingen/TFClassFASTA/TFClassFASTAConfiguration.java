package BioinformatikGoettingen.TFClassFASTA;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class TFClassFASTAConfiguration extends Configuration {
    @Valid
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
    	Map<String,String> s = new HashMap<String,String>();
    	s.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
    	database.setProperties(s);
        return database;
    }
}
