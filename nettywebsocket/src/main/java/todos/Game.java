package todos;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Date;

public class Game {
    //我在此定义一个数据结构用来管理多个user的ctx。
    //像一局游戏开始一样，start之后，会给map内的人发送消息。
    public CtxMap ctxMap;
    public ArrayList<String> arr= new ArrayList<>();
    public static Game game;
    public void start(){
        for (String k :this.arr){
            this.ctxMap.map.get(k).writeAndFlush(new TextWebSocketFrame("---game start---"));
        }
    }
    public void setCtxMap(CtxMap ctxMap){
        this.ctxMap=ctxMap;
    }
    public void setKey(String key){
        this.arr.add(key);
    }
    public static Game getInstance(){
        if(game==null){
            game=new Game();
        }
        return game;
    }
}
