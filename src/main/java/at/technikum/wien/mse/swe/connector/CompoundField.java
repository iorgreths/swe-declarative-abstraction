package at.technikum.wien.mse.swe.connector;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(CompoundFields.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompoundField {

    String name();
    int begin();
    int length();
    String padding();
    FieldAlignment alignment();

}
