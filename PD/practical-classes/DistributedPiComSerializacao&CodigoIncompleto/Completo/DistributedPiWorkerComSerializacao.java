import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

class ManageMyDbEntry extends Thread {

    String ipLocalAddress;
    int listeningPort;
    String dbAddress;
    String dbName;
    String user;
    String pass;    

    public ManageMyDbEntry(String ipLocalAddress, int listeningPort, String dbAddress, String dbName, String user, String pass) {
        this.ipLocalAddress = ipLocalAddress;
        this.listeningPort = listeningPort;
        this.dbAddress = dbAddress;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public void run() {
        
        String dbUrl = "jdbc:mysql://" + dbAddress + "/" + dbName;
        
        try {
            Class.forName(DistributedPiWorkerComSerializacao.JDBC_DRIVER);
        }catch (ClassNotFoundException e) {
            System.out.println("<DistributedPiWorker> Exception reported:\r\n\t..." + e);
            return;
        } 

        try(Connection conn = DriverManager.getConnection(dbUrl, user, pass);                
            Statement stmt = conn.createStatement()) {          
                        
            String createOrUpdateEntryQuery = "INSERT INTO pi_workers (address,port) VALUES ('"
                    + ipLocalAddress + "'," + listeningPort + ") ON DUPLICATE KEY UPDATE timestamp=CURRENT_TIMESTAMP;";
            
            String updateEntryQuery = "UPDATE pi_workers SET timestamp=CURRENT_TIMESTAMP WHERE address LIKE '%"
                    + ipLocalAddress + "%' AND port=" + listeningPort + ";";                
            
            String createEntryQuery = "INSERT INTO pi_workers (address,port) VALUES ('"
                    + ipLocalAddress + "'," + listeningPort + ");";
            
            if(stmt.executeUpdate(createOrUpdateEntryQuery)<1){
                System.out.println("Entry insertion or update failed");
            }            
                    
            while (true) {
                try {
                    Thread.sleep(DistributedPiWorkerComSerializacao.DB_UPDATE_DELAY);
                    
                    if(stmt.executeUpdate(updateEntryQuery)<1){

                        System.out.println("Entry update failed");
               
                        if(stmt.executeUpdate(createEntryQuery)<1){
                            System.out.println("Entry insertion failed");
                        }else{
                            System.out.println("Entry insertion succeeded");
                        }  
                    }
                } catch (InterruptedException e) {}
            }
        } catch (SQLException e) {
            System.out.println("<DistributedPiWorker> Exception reported:\r\n\t..." + e);
        } 
        
    }
}

public class DistributedPiWorkerComSerializacao implements Runnable
{
    static final int TIMEOUT = 60000; //60 seconds
    static final int DB_UPDATE_DELAY = 30000; // 30 seconds

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost/pi_distrib_db";
    //static final String USER = "piworker";
    //static final String PASS = "w-pass-123";

    private final Socket s;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public DistributedPiWorkerComSerializacao(Socket s)
    {
        this.s= s;
        in = null;
        out = null;
    }
    
    public double getMyResult(int myId, int nWorkers, long nIntervals)
    {
        long i;
        double dX, xi, myResult;
        
        if(nIntervals < 1 || nWorkers < 1 || myId <1 || myId > nWorkers){
            return 0.0;
        }
        
        dX = 1.0/nIntervals;
        myResult = 0;
        
        for (i = myId-1 ; i < nIntervals; i += nWorkers) {             
            xi = dX*(i + 0.5);
            myResult += (4.0/(1.0 + xi*xi));               
        }
        
        myResult *= dX;
        
        return myResult;
    }
    
    @Override
    public void run()
    {
        int myId;
        int nWorkers;
        long nIntervals;
        double myResult;
        RequestToWorker req;
        
        try{
            
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
                        
            try{
                
                req = (RequestToWorker)in.readObject();
                
                myId = req.getId();   
                nWorkers = req.getnWorkers();
                nIntervals = req.getnIntervals();
                
                System.out.println("<" + Thread.currentThread().getName() + 
                    "> New request received - myId: " + myId + " nWorkers: " + nWorkers + " nIntervals: " + nIntervals);
             }catch(ClassNotFoundException e){
                System.err.println("<" + Thread.currentThread().getName() + 
                    ">: " + e);             
                return;
            }  
            
            myResult = getMyResult(myId, nWorkers, nIntervals);
            
            out.writeObject(myResult);
            out.flush();
            
            System.out.format("<%s> %.10f\n", Thread.currentThread().getName(), myResult);
            
        }catch(IOException e){
            System.out.println("<" + Thread.currentThread().getName() + 
                    "> Erro ao aceder ao socket:\n\t" + e);
        }finally{
            try{
                if(s != null) s.close();
            }catch(IOException e){}            
        }
        
    }
    
    /*
    *   Lists the IP addresses of all the active network interfaces and asks
    *   the user to select one of them.
    *   Note: alternatively the user might simply specify the IP address through
    *   an additional command line argument.
    */
    static String selectLocalIpAddress() {
        int i=0;     
        ArrayList<String> ipAddresses = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int option=-1;
        
        System.out.println("Select the local address do be announced:");
        System.out.println();
        
        try{
            
            Enumeration <NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        
            while(nifs.hasMoreElements()){
                NetworkInterface nif = nifs.nextElement();
                String nifName = nif.getName();
                Enumeration <InetAddress> addresses = nif.getInetAddresses();                
                
                while (addresses.hasMoreElements()){
                    String address = addresses.nextElement().getHostAddress();
                    if(!address.contains(":")){ //Exclude IPv6 addresses from the list
                        System.out.println(++i + ": " + address + " (" + nifName + ")");
                        ipAddresses.add(address);                    
                    }
                }
            }
            
            System.out.println();
            System.out.println("0: exit");
            System.out.println();
            
            do{
                System.out.print("> ");
                try{
                    option = sc.nextInt();
                }catch(Exception ex){
                    option = -1;
                }
            }while(option<0 || option>ipAddresses.size());
            
            if(option==0)
                return null;
            
            return ipAddresses.get(option-1);
            
        }catch(SocketException ex){
            System.out.println(ex);
            return null;
        }
    }
               
    public static void main(String[] args) {
        
        int listeningPort;        
        Thread t, manageMyDbEntry;        
        int nCreatedThreads = 0;
        String ipLocalAddress;
        
        if(args.length != 5){
            System.out.println("Sintaxe: java DistributedPiWorker <listening port>"
                    + "<SGBD address> <BD name> <usename> <password>");
            return;
        }
        
        listeningPort = Integer.parseInt(args[0]);
        
        if((ipLocalAddress=selectLocalIpAddress())==null){ //Alternatively the user might specify the IP address through an additional command line argument.
            return;
        }
        
        manageMyDbEntry = new ManageMyDbEntry(ipLocalAddress, listeningPort, args[1], args[2], args[3], args[4]);
        manageMyDbEntry.setDaemon(true);
        manageMyDbEntry.start();
            
        try(ServerSocket s = new ServerSocket(listeningPort)){
            
            while(true){
                Socket toClient = s.accept();                
                toClient.setSoTimeout(TIMEOUT);
                
                nCreatedThreads++;
                t = new Thread(new DistributedPiWorkerComSerializacao(toClient), "Thread "+nCreatedThreads);
                t.start();                
            }            
            
        }catch(IOException | NumberFormatException e){
            System.out.println("<DistributedPiWorker> Exception reported:\r\n\t..." + e);
        }
                
    }
}
