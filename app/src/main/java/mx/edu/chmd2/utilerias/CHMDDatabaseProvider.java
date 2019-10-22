package mx.edu.chmd2.utilerias;

import com.activeandroid.Configuration;
import com.activeandroid.content.ContentProvider;

import mx.edu.chmd2.modelosDB.DBCircular;

public class CHMDDatabaseProvider extends ContentProvider {
    protected Configuration getConfiguration() {
        Configuration.Builder builder = new Configuration.Builder(getContext());
        builder.addModelClass(DBCircular.class);
        return builder.create();
    }
}
