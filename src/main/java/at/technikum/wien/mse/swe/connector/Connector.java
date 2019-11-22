package at.technikum.wien.mse.swe.connector;

import java.nio.file.Path;

public interface Connector<T> {

    T read(Path path);

}
