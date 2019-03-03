package com.swe.duckware.megalexa.alexa;

public class Connector {

    private String cnName;

    private String cnURL;

    public Connector(String connectorName, String connectorURL) {
        this.cnName = connectorName;
        this.cnURL = connectorURL;
    }

    @Override
    public String toString() {
        return cnName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Connector))
            return false;

        Connector cn = (Connector) obj;
        return cn.cnName.equals(cnName) && cn.cnURL.equals(cnURL);
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + cnName.hashCode();
        res = 31 * res + cnURL.hashCode();
        return res;
    }

}
