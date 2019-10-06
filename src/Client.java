import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client implements Runnable{
    SocketChannel socketChannel;
    int port;
    String host;
    GUI gui;


    public Client(String host, int port){
        this.host = host;
        this.port = port;
        gui = new GUI(this);


    }

    public void connectServer(){
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            System.out.println("client has connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            if (socketChannel.isOpen()) {
                receiveMessage();
            } else {
                wait();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            String msg = gui.nick.getText() + ": " + message;
            byte [] bytes = msg.getBytes();
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            socketChannel.write(buf);
            buf.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    synchronized void receiveMessage(){
        while(socketChannel.isOpen()){
           ByteBuffer buffer = ByteBuffer.allocate(256);
            try {

                int read = socketChannel.read(buffer);
                if(read == -1 ){
                    socketChannel.close();
                }

            } catch (IOException e) {

            }

            String msg = new String(buffer.array());
            System.out.println("received message " + msg);
            gui.messageList.append(msg);

        }
    }

    public void quit(){
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
