package todos;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.SocketAddress;

public class WebSocketServerOutBoundHandler extends ChannelOutboundHandlerAdapter {
    //get notified for IO-outbound-operations
    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        System.out.println("bind!");
        super.bind(channelHandlerContext,socketAddress,channelPromise);
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) throws Exception {
        System.out.println("connect!");
        super.connect(channelHandlerContext,socketAddress,socketAddress1,channelPromise);
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        System.out.println("close!");
        super.close(channelHandlerContext,channelPromise);
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.read(channelHandlerContext);
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof TextWebSocketFrame){
            System.out.println("write!"+((TextWebSocketFrame)msg).text());
        }
        super.write(ctx, msg, promise);
    }

}
