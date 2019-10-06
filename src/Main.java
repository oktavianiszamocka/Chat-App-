public class Main {

    public static void main(String[] args)
    {
        Server server = new Server(9999);
        new Thread(server).start();
       Client client1 = new Client("localhost", 9999);
       Client client2 =  new Client("localhost", 9999);
    }
}
