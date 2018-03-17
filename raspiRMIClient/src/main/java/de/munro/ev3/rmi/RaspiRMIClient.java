package de.munro.ev3.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;

public class RaspiRMIClient {
    private static final Logger LOG = LoggerFactory.getLogger(RaspiRMIClient.class);
    private static final String LOCAL_HOST = "localhost";

    private String host = LOCAL_HOST;

    public static void main(String args[]) {
        LOG.info("Started {}", (Object[])args);

        RaspiRMIClient RaspiRMIClient = new RaspiRMIClient();
        if (args.length == 1) {
            RaspiRMIClient.setHost(args[0]);
        }

        try
        {
            String service = String.format("//%s/%s", RaspiRMIClient.getHost(), RemoteEV3.SERVICE_NAME);
            LOG.info(service);
            RemoteEV3 remoteEV3 = (RemoteEV3) Naming.lookup(service);

            LOG.info("remoteEV3.forward()");
            remoteEV3.forward();
            LOG.info("remoteEV3.forward()");
            remoteEV3.forward();
            Thread.sleep(2000);
            LOG.info("remoteEV3.stop()");
            remoteEV3.stop();
            Thread.sleep(2000);
            LOG.info("remoteEV3.backward()");
            remoteEV3.backward();
            Thread.sleep(2000);
            LOG.info("remoteEV3.stop()");
            remoteEV3.stop();
        }
        catch (Exception e)
        {
            LOG.error("Exception: ", e);
        }
        LOG.info("Finished");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
