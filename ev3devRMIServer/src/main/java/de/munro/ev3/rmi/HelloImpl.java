package de.munro.ev3.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello
{
    private static final Logger LOG = LoggerFactory.getLogger(HelloImpl.class);
    private static final String LOCAL_HOST = "localhost";
    private String host;

    public HelloImpl(String host) throws RemoteException {
        this.host = host;
    }

    public static void main(String args[])
    {
        String host;
        if (args.length == 1) {
            host = args[0];
        } else {
            host = LOCAL_HOST;
        }

        LOG.info("Started: {}", host);

        String name = String.format("//%s/%s", host, Hello.SERVICE_NAME);

        // check whether there is already an instance running
        try {
            Hello look_up = (Hello) Naming.lookup(name);
            look_up.shutdown();
            LOG.info("Waiting to shutdown...");
            Thread.sleep( 8000 );
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            LOG.info("No server running: {}", e.getMessage());
        } catch (InterruptedException e) {
            // swallow
        }

        // register new instance
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind(name, new HelloImpl(host));
            LOG.info("Server ready");
        } catch (RemoteException | MalformedURLException e) {
            LOG.error("Exception: ", e);
            System.exit(1);
        }
    }

    public String getHost() {
        return host;
    }

    @Override
    public String sayHello() {
        LOG.info("sayHello()");
        return "Hello world!";
    }

    /**
     * Unbinds the remote server.
     *
     * @exception RemoteException
     */
    @Override
    public void shutdown() {
        Thread one = new Thread(() -> {
            LOG.info( "Unbinding host=" + getHost() + ", port=" + Registry.REGISTRY_PORT + ", serviceName=" + Hello.SERVICE_NAME);
            try {
                Naming.unbind(String.format("//%s:%d/%s", getHost(), Registry.REGISTRY_PORT, Hello.SERVICE_NAME));
            }
            catch ( RemoteException | MalformedURLException ex ) {
                // impossible case.
                LOG.error( "Exception: ", ex.getMessage() + "; host=" + getHost() + ", port=" + Registry.REGISTRY_PORT
                        + ", serviceName=" + Hello.SERVICE_NAME );
            }
            catch ( NotBoundException ex ) {
                // ignore.
            }
            try {
                Thread.sleep( 2000 );
            }
            catch ( InterruptedException ex ) {
                // swallow
            }
            System.exit( 0 );
        });

        one.start();
    }

}