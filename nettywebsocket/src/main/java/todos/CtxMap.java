package todos;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CtxMap {
    //也可以用channelgroup的形式做
    public final ConcurrentHashMap<String, SocketChannel> map;
    private static CtxMap ctxMap;
    public static int i=0;
    CtxMap(){
        map=new ConcurrentHashMap<>();
    }
    //singleton
    public static CtxMap getInstance(){
        if (ctxMap==null){
            ctxMap = new CtxMap();
        }
        return ctxMap;
    }
    //不封装了先

}
