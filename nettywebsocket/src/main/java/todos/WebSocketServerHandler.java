package todos;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    //客户端独享，添加Sharable注解变成共享
    //ChannelOutboundHandlerAdapter与ChannelInboundHandlerAdapter都是继承于ChannelHandler，并实现自己的ChannelXxxHandler。用于在消息管道中不同时机下处理处理消息。
    //which adds callbacks for state changes. This allows the user to hook in to state changes easily.
    WebSocketServerHandshakerFactory webSocketServerHandshakerFactory=new WebSocketServerHandshakerFactory(
            "ws://localhost:8080/websocket",null,false);
    WebSocketServerHandshaker handshaker;
    CtxMap ctxMap=CtxMap.getInstance();
    Game game=Game.getInstance();


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("readComplete!");
        ctx.flush();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext){
        System.out.println("push user");
        ctxMap.map.put(new Integer(ctxMap.i).toString(),(SocketChannel) channelHandlerContext.channel());
        game.setCtxMap(ctxMap);
        game.setKey(new Integer(ctxMap.i).toString());
        ctxMap.i++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //System.out.println("read!");

        System.out.println("InBoundHandler 线程名"+Thread.currentThread().getName());
        if (o instanceof FullHttpRequest){
            handleHttpRequest(channelHandlerContext,(FullHttpRequest)o);
        }
        if(o instanceof WebSocketFrame){
            handleWebSocketFrame(channelHandlerContext,(WebSocketFrame)o);
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if(evt instanceof IdleStateEvent){
            ctx.close();
        }

    }
    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest fullHttpRequest){
        if(!fullHttpRequest.decoderResult().isSuccess()
                ||
                (!"websocket".equals(fullHttpRequest.headers().get("Upgrade")))){
            //返回解码异常
            return;
        }
    //WS握手
    handshaker=webSocketServerHandshakerFactory.newHandshaker(fullHttpRequest);

    if(handshaker == null){
        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
    }else{
        handshaker.handshake(ctx.channel(),fullHttpRequest);
    }
    }

    private void handleWebSocketFrame(ChannelHandlerContext channelHandlerContext,WebSocketFrame webSocketFrame){
        if(webSocketFrame instanceof CloseWebSocketFrame){
            handshaker.close(channelHandlerContext.channel(),(CloseWebSocketFrame)webSocketFrame.retain());
            return;
        }
        if(webSocketFrame instanceof PingWebSocketFrame){
            channelHandlerContext.channel().write(
                    new PongWebSocketFrame(webSocketFrame.content().retain())
            );
        }
        if(!(webSocketFrame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported",webSocketFrame.getClass().getName()
            ));
        }
        String request =((TextWebSocketFrame)webSocketFrame).text();
        if(request.equals("start game")){
            if(ctxMap.i>=2){
                game.start();
            }
        }
        //System.out.println("write!");
//        channelHandlerContext.channel().write(
//                new TextWebSocketFrame(request+new Date().toString())
//        );
        //send(ctxMap);
    }
    //此函数可以自行控制消息发送。
    public void send(CtxMap ctxMap){
        ctxMap.map.get("0").write(new TextWebSocketFrame("自定义消息！"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext,Throwable throwable) throws Exception{
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
}
