package todos;

import com.sun.org.apache.bcel.internal.generic.NEW;
import todos.NettyServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        NettyServer nettyServer= new NettyServer();
        System.out.println("app 线程名"+Thread.currentThread().getName());
        nettyServer.run();
    }
}











































