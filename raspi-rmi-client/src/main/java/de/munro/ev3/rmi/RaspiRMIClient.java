package de.munro.ev3.rmi;

import de.munro.ev3.controller.WatchThread;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InvalidNameException;
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

    private InstructionDetails instructionDetails = new InstructionDetails();

    public static void main(String[] args) {
        log.info("Started {}", (Object) args);

        RaspiRMIClient raspiRMIClient = new RaspiRMIClient();
//        WatchThread watchThread;
        if (args.length == 1) {
            raspiRMIClient.setHost(args[0]);
        }

        try
        {
            String service = String.format("//%s/%s", raspiRMIClient.getHost(), RemoteEV3.SERVICE_NAME);
            RemoteEV3 remoteEV3 = raspiRMIClient.waitUntilConnected(service, TIMES_TO_ATTEMPT);

            raspiRMIClient.waitUntilEV3Ready(remoteEV3, TIMES_TO_ATTEMPT);

//            watchThread = raspiRMIClient.startWatchThread(remoteEV3);

            boolean shutdown = raspiRMIClient.readInput(remoteEV3);
//            watchThread.interrupt();
            if (shutdown) {
                log.info("remoteEV3.shutdown()");
                remoteEV3.shutdown();
            }
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        log.info("Finished");
    }

    /**
     * start the thread to watch the latest state of all sensors
     * @param remoteEV3 instance to communicate with ev3
     * @return current watchThread
     */
    private WatchThread startWatchThread(RemoteEV3 remoteEV3) {
        WatchThread watchThread = new WatchThread(remoteEV3, instructionDetails);
        watchThread.start();
        return watchThread;
    }

    /**
     * Gets the host
     * @return host
     */
    private String getHost() {
        return host;
    }

    /**
     * Sets the host
     * @param host the host
     */
    private void setHost(String host) {
        this.host = host;
    }

    protected boolean readInput(RemoteEV3 remoteEV3) {
        Scanner scanner = new Scanner(System.in);
        boolean shutdown = false;
        input: while (true) {

            System.out.print("Enter something : ");
            String input = scanner.nextLine();
            synchronized (instructionDetails) {
                instructionDetails.readArguments(parseCommand(input));
            }
            log.debug(instructionDetails.toString());
            if (!validInput(instructionDetails)) {
                log.debug("Invalid input");
                continue input;
            }
            System.out.println("input : " + instructionDetails);

            try {
                switch (instructionDetails.getInstruction()) {
                    case beep:
                        System.out.println("beep");
                        remoteEV3.beep();
                        break;
                    case perform:
                        System.out.println(instructionDetails.getInstruction());
                        remoteEV3.perform(instructionDetails.getMotorType(), instructionDetails.getCommand());
                        break;
                    case set:
                        System.out.println(instructionDetails.getInstruction());
                        remoteEV3.set(instructionDetails.getMotorType(), instructionDetails.getCommand(), instructionDetails.getValue());
                        break;
                    case read:
                        System.out.println(instructionDetails.getInstruction());
                        remoteEV3.read(instructionDetails.getMotorType());
                        break;
                    case write:
                        System.out.println(instructionDetails.getInstruction());
                        remoteEV3.write(instructionDetails.getMotorType());
                        break;
                    case show:
                        System.out.println(instructionDetails.getInstruction());
                        remoteEV3.show(instructionDetails.getMotorType());
                        break;
                    case status:
                        System.out.println(instructionDetails.getInstruction());
                        System.out.println(remoteEV3.status());
                        break;
                    case quit:
                        System.out.println("quit!");
                        break input;
                    case shutdown:
                        shutdown = true;
                        System.out.println("Exit!");
                        break input;
                    default:
                        System.out.println("unknown command!");
                }
            } catch (RemoteException | InvalidNameException e) {
                e.printStackTrace();
            }

            System.out.println("-----------\n");
        }

        log.debug("input stopped");
        scanner.close();
        return shutdown;
    }

    /**
     * check whether the input holds all attributes neccessary
     * @param instructionDetails input
     * @return true, if everything is fine
     */
    private boolean validInput(InstructionDetails instructionDetails) {
        if (instructionDetails.getInstruction() == null) {
            return false;
        }
        switch (instructionDetails.getInstruction()) {
            case beep:
            case status:
            case quit:
            case shutdown:
                return true;
            case perform:
                return instructionDetails.getMotorType() != null && instructionDetails.getCommand() != null;
            case set:
                return instructionDetails.getMotorType() != null && instructionDetails.getCommand() != null && instructionDetails.getValue() != null;
            case read:
            case write:
            case show:
                return instructionDetails.getMotorType() != null;
            default:
                break;
        }
        return false;
    }

    /**
     * provides the arguments of the command line input
     * @param input command line input
     * @return arguments
     */
    protected String[] parseCommand(String input) {
        return input.split(" ");
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
                log.debug("Build connection to EV3: {}", e.getMessage());
            }
            if (null == remoteEV3) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Thread.sleep", e);
                }
            }
        }
        if (null == remoteEV3 && getHost().compareTo(LOCAL_HOST) == 0) {
            log.debug("Build connection to EV3: using dummy connection");
            remoteEV3 = new RemoteEV3Dummy();
        }
        return remoteEV3;
    }
}
