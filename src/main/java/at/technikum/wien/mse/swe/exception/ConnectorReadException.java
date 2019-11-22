package at.technikum.wien.mse.swe.exception;

public class ConnectorReadException extends RuntimeException {

    public ConnectorReadException(Exception e){
        super(e);
    }

}
