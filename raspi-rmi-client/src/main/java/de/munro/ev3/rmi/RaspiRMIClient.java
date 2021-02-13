package de.munro.ev3.rmi;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

@Slf4j
public class RaspiRMIClient {
    
    private static final String LOCAL_HOST = "localhost";
    private static final int TIMES_TO_ATTEMPT = 2;

    private String host = LOCAL_HOST;

    public static void main(String[] args) {
        log.info("Started {}", (Object) args);

        RaspiRMIClient raspiRMIClient = new RaspiRMIClient();
        if (args.length == 1) {
            raspiRMIClient.setHost(args[0]);
        }

        try
        {
            String service = String.format("//%s/%s", raspiRMIClient.getHost(), RemoteEV3.SERVICE_NAME);
            RemoteEV3 remoteEV3 = raspiRMIClient.waitUntilConnected(service, TIMES_TO_ATTEMPT);

            raspiRMIClient.waitUntilEV3Ready(remoteEV3, TIMES_TO_ATTEMPT);

            raspiRMIClient.readInput(remoteEV3);
            log.info("remoteEV3.shutdown()");
            remoteEV3.shutdown();
        }
        catch (Exception e)
        {
            log.error("Exception: ", e);
        }
        log.info("Finished");
    }

    protected void readInput(RemoteEV3 remoteEV3) {
        Scanner scanner = new Scanner(System.in);

        input: while (true) {

            System.out.print("Enter something : ");
            String input = scanner.nextLine();
            System.out.println("input : " + input);

            try {
                switch (input) {
                    case "q":
                        System.out.println("quit!");
                        break input;
                    case "beep":
                        System.out.println("beep");
                        remoteEV3.beep();
                        break;
                    case "forward":
                        System.out.println("forward");
                        remoteEV3.forward();
                        break;
                    case "backward":
                        System.out.println("backward");
                        remoteEV3.backward();
                        break;
                    case "stop":
                        System.out.println("stop");
                        remoteEV3.stop();
                        break;
                    case "left":
                        System.out.println("left");
                        remoteEV3.left();
                        break;
                    case "right":
                        System.out.println("right");
                        remoteEV3.right();
                        break;
                    case "straight":
                        System.out.println("straight");
                        remoteEV3.straight();
                        break;
                    case "frontup":
                        System.out.println("frontup");
                        remoteEV3.frontUp();
                        break;
                    case "frontdown":
                        System.out.println("frontdown");
                        remoteEV3.frontDown();
                        break;
                    case "backup":
                        System.out.println("backup");
                        remoteEV3.backUp();
                        break;
                    case "backdown":
                        System.out.println("backdown");
                        remoteEV3.backDown();
                        break;
                    case "reset":
                        System.out.println("reset");
                        remoteEV3.reset();
                        break;
                    case "test":
                        System.out.println("test");
                        remoteEV3.test();
                        break;
                    case "shutdown":
                        System.out.println("Exit!");
                        break input;
                    default:
                        System.out.println("unknown command!");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            System.out.println("-----------\n");
        }

        scanner.close();
    }

    private void waitUntilEV3Ready(RemoteEV3 remoteEV3, int wait4Connection) {
        int attempted = 0;
        while (true) {
            try {
                if (remoteEV3.isInitialized() || attempted++ > wait4Connection) break;
                Thread.sleep(1000);
            } catch (RemoteException | InterruptedException e) {
                log.error("Initialization", e);
            }
        }
    }

    private RemoteEV3 waitUntilConnected(String service, int wait4Connection) throws NotBoundException, MalformedURLException, RemoteException {
        log.debug("waitUntilConnected({}, {})", service, wait4Connection);
        RemoteEV3 remoteEV3 = null;
        int attempted = 0;
        while (null == remoteEV3 && attempted++ < wait4Connection) {
            try {
                log.debug("Build connection to EV3: {}", service);
                remoteEV3 = (RemoteEV3) Naming.lookup(service);
            } catch (java.rmi.ConnectException e) {
                log.debug("Build connection to EV3: ", e);
            }
            if (null == remoteEV3) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Thread.sleep", e);
                }
            }
        }
        return remoteEV3;
    }

    private String getHost() {
        return host;
    }

    private void setHost(String host) {
        this.host = host;
    }
}
