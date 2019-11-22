package at.technikum.wien.mse.swe.connector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CompoundFields {

    CompoundField[] value();

}
