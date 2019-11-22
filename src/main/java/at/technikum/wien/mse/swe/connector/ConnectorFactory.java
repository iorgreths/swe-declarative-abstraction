package at.technikum.wien.mse.swe.connector;

public class ConnectorFactory<T> {

    public Connector<T> createConnectorForClass(Class<T> connectorClass){
        return new ConnectorImpl<T>(connectorClass);
    }
}
