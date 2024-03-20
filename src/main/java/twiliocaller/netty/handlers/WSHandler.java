package twiliocaller.netty.handlers;

import java.util.concurrent.ConcurrentHashMap;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private ConcurrentHashMap<String, Channel> activeChannels = new ConcurrentHashMap<String, Channel>();


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
		String message = frame.text();
		// Handle incoming messages, e.g., initiate a call based on the message
		if (message.startsWith("makeCall")) {
			initiateCall(ctx, message.substring(8));
		}

	}

	private void initiateCall(ChannelHandlerContext ctx, String recipient) {
		VoiceResponse response = new VoiceResponse.Builder()
                .dial(new Dial.Builder()
                        .number(new com.twilio.twiml.voice.Number.Builder(recipient).build())
                        .build())
                .build();
		String twiMLResponse = response.toXml();
		ctx.writeAndFlush(new TextWebSocketFrame(twiMLResponse));

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		activeChannels.put(ctx.channel().id().asLongText(), ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		activeChannels.remove(ctx.channel().id().asLongText());
	}

}
