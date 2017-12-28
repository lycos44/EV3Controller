package de.munro.ev3.rmi;

import de.munro.ev3.rmi.Hello;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.rmi.Naming;

public class HelloClient
{
    //private static Logger logger = LoggerFactory.getLogger(HelloClient.class);
    private static final String LOCAL_HOST = "localhost";
    private String host;
/*
    public HelloClient(String[] args) {
        if (args.length == 1) {
            setHost(args[0]);
        } else {
            setHost(LOCAL_HOST);
        }
        //logger.info("Host: "+getHost());
    }
*/
    public static void main(String arg[])
    {
        //logger.info("*** started client ***");
        // I download server's stubs ==> must set a SecurityManager
//        System.setSecurityManager(new SecurityManager());

        try
        {
            HelloClient helloClient = new HelloClient();
            //Hello look_up = (Hello) Naming.lookup("//"+helloClient.getHost()+"/HelloServer");
            Hello look_up = (Hello) Naming.lookup("//ev3dev/HelloServer");
            System.out.println(look_up.sayHello());
        }
        catch (Exception e)
        {
            System.out.println("HelloClient exception: " + e.getMessage());
            e.printStackTrace();
        }
        //logger.info("*** finished client ***");
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
  