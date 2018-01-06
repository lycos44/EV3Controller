package de.munro.ev3.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.rmi.Naming;

public class HelloClient
{
    private static final Logger LOG = LoggerFactory.getLogger(HelloClient.class);
    private String host = "localhost";

    public static void main(String args[])
    {
        LOG.info("Started {}", (Object[])args);
        // I download server's stubs ==> must set a SecurityManager
//        System.setSecurityManager(new SecurityManager());
        HelloClient helloClient = new HelloClient();
        if (args.length == 1) {
            helloClient.setHost(args[0]);
        }

        try
        {
            Hello look_up = (Hello) Naming.lookup(String.format("//%s/%s", helloClient.getHost(), Hello.SERVICE_NAME));
            String message = look_up.sayHello();
            LOG.info("look_up.sayHello(): {}", message);
            look_up.shutdown();
            LOG.info("shutdown()");
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
  