import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server implements Runnable{
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    ByteBuffer buffer;
    final int serverport;


    public Server(int serverport){
        this.serverport = serverport;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", serverport));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            buffer = ByteBuffer.allocate(256);
            System.out.println("server started");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        SelectionKey selectionKey;
        while(true){
            try {
                selector.select();
                Set<SelectionKey> key = selector.selectedKeys();
                Iterator<SelectionKey> iterator = key.iterator();
                while(iterator.hasNext()){
                    selectionKey = (SelectionKey) iterator.next();

                    if(selectionKey.isAcceptable()){
                        registerUser(selectionKey);
                    }
                    if(selectionKey.isReadable()){
                        receiveMessage(selectionKey);
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerUser(SelectionKey key){
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("Client from " + socketChannel.getLocalAddress() + " is connected" );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveMessage(SelectionKey key){
        SocketChannel socket = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        buffer.clear();
        try {
            int read = 0;
            while((read = socket.read(buffer)) > 0){
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                sb.append(new String(bytes));
                buffer.clear();
            }

            System.out.println(read);
            String msg;
            if(read<0){
                msg = "the user is leaving";
                key.cancel();
                sendBroadcast(msg);
                socket.close();
            } else {
                msg = sb.toString();
                sendBroadcast(msg + "\n");
            }
        System.out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcast(String message){
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());
        for( SelectionKey skey : selector.keys()){
            if(skey.isValid() && skey.channel() instanceof  SocketChannel){
                byteBuffer.clear();
                SocketChannel sc = (SocketChannel) skey.channel();
                try {
                    sc.write(byteBuffer);
                    byteBuffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

    }



}
