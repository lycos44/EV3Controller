package de.munro.ev3.rmi;

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
//            log.error("Exception: ", e);
        }
        log.info("Finished");
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

    protected void readInput(RemoteEV3 remoteEV3) {
        Scanner scanner = new Scanner(System.in);

        input: while (true) {

            System.out.print("Enter something : ");
            String input = scanner.nextLine();
            InstructionDetails instructionDetails = parseCommand(input);
            if (instructionDetails.getInstruction() == null) {
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
                    case quit:
                        System.out.println("quit!");
                        break input;
                    case shutdown:
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

        scanner.close();
    }

    /**
     * provides the arguments of the command line input
     * @param input command line input
     * @return arguments
     */
    protected InstructionDetails parseCommand(String input) {
        String[] arguments = input.split(" ");

        return new InstructionDetails(arguments);
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
