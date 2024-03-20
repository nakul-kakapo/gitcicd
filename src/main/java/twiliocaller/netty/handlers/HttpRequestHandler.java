package twiliocaller.netty.handlers;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Client;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Number;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final String TWILIO_ACCOUNT_SID = "ACf385b106208583c1057e9f7d5f05a618";
	private static final String TWILIO_TWIML_APP_SID = "AP2dc0863f136e4b1b4cf02cce3e3e0cf8";
	private static final String TWILIO_CALLER_ID = "+15412348138";
	private static final String IDENTITY1 = "NakulK";

	private static final String API_KEY = "SKe1e3ddb3210e3e3c94f1a3fb40e546c3";
	private static final String API_SECRET = "TmJsUkc5J1ybQdL1j4037VFHwD3T4ETX";

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		if ("/login".equals(req.uri()) && req.method().equals(HttpMethod.POST)) {
			System.out.println("/index");
//			ByteBuf content = ServerPages.getContent();
			String username = "", password = "";
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
			try {
				decoder.offer(req);
				List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
				for (InterfaceHttpData data : postData) {
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						Attribute attribute = (Attribute) data;
						if (attribute.getName().equals("username")) {
							username = attribute.getValue();
						} else if (attribute.getName().equals("password")) {
							password = attribute.getValue();
						}
					}
				}
			} finally {
				decoder.destroy();
			}

			System.out.println("u:" + username);
			System.out.println("p:" + password);

			Gson gson = new Gson();
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
			HashMap<String, String> json = new HashMap<>();
			json.put("username", username);
			json.put("password", password);
			response.content().writeBytes(gson.toJson(json).getBytes());
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
			setCorsHeaders(response.headers());
			ctx.writeAndFlush(response).addListener(future -> ctx.close());

		} else if ("/make-call".equals(req.uri())) {
			Twilio.init("ACf385b106208583c1057e9f7d5f05a618", "fa21deece7cc62c12b7ae70b4f3903c0");

			String helloTwiml = new VoiceResponse.Builder()
					.say(new Say.Builder("Hello from Twilio").voice(Say.Voice.POLLY_MATTHEW).build()).build().toXml();

			Call call = Call
					.creator(new PhoneNumber("+919809355419"), new PhoneNumber("+15412348138"), new Twiml(helloTwiml))
					.create();
			System.out.println(call.getSid());

			final FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK,
					Unpooled.copiedBuffer("Initiating call".getBytes()));

			res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
			ctx.writeAndFlush(res).addListener(future -> ctx.close());

		} else if ("/incoming-call".equals(req.uri())) {

			Say say = new Say.Builder("Hello world!").build();
			VoiceResponse twiml = new VoiceResponse.Builder().say(say).build();

			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			response.content().writeBytes(twiml.toXml().getBytes());
			ctx.writeAndFlush(response).addListener(future -> ctx.close());

		} else if ("/".equals(req.uri())) {
			System.out.println("/voice");
			String to = "";
			String phone = "";
			String from = "";
			boolean isIncoming = false;
			if (req.method().equals(HttpMethod.POST)) {
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
				try {
					decoder.offer(req);
					List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

					// Iterate through the post data
					for (InterfaceHttpData data : postData) {
						if (data.getHttpDataType() == HttpDataType.Attribute) {
							Attribute attribute = (Attribute) data;
							// Process the attribute (post parameter)
							// You can store it in a map or perform other operations
							System.out.println("Attribute: " + attribute.getName() + " = " + attribute.getValue());
							if (attribute.getName().equals("To")) {
								to = attribute.getValue();
							}
							if (attribute.getName().equals("to")) {
								to = attribute.getValue();
							}
							if (attribute.getName().equals("phone")) {
								phone = attribute.getValue();
							}
							if (attribute.getName().equals("From")) {
								from = attribute.getValue();
							}

							if (attribute.getName().equals("Caller")) {
								if (attribute.getValue().contains("client")) {
									isIncoming = true;
								}
							}
						}
					}
				} finally {
					decoder.destroy();
				}
			}
			if (to == null || to.isEmpty()) {
				to = phone;
			}

			if (isIncoming) {
				generateDialTwiML(ctx, req, TWILIO_CALLER_ID, to);
			} else {
				System.out.println("Client:" + to);
				System.out.println("From:" + from);
				String greeting = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Response><Dial callerId=\"" + from
						+ "\"> <Client>NakulK</Client></Dial> </Response>";
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.copiedBuffer(greeting.getBytes()));
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/xml");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
				ctx.writeAndFlush(response);
			}

		} else if ("/token".equals(req.uri())) {
			System.out.println("/token");

			VoiceGrant grant = new VoiceGrant();
			grant.setOutgoingApplicationSid(TWILIO_TWIML_APP_SID);

			// Optional: add to allow incoming calls
			grant.setIncomingAllow(true);

			AccessToken accessToken = new AccessToken.Builder(TWILIO_ACCOUNT_SID, API_KEY, API_SECRET)
					.identity(IDENTITY1).grant(grant).build();

			String token = accessToken.toJwt();
			System.out.println("token:" + token);

			HashMap<String, String> json = new HashMap<>();
			json.put("identity", IDENTITY1);
			json.put("token", token);

			Gson gson = new Gson();

			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
			setCorsHeaders(response.headers());
			response.content().writeBytes(gson.toJson(json).getBytes());

			System.out.println("json:" + gson.toJson(json));
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			ctx.writeAndFlush(response).addListener(future -> ctx.close());

		} else {
			System.out.println("content" + req.content().toString());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("exception:" + cause);
		super.exceptionCaught(ctx, cause);
	}

	private static void generateDialTwiML(ChannelHandlerContext ctx, FullHttpRequest req, String from, String to) {
		// Use <Dial> or <Number> verb to specify the destination number
		String dial = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Response><Dial callerId=\"" + from
				+ "\"><Number>" + to + "</Number></Dial> </Response>";
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(dial.getBytes()));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/xml");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		ctx.writeAndFlush(response);
	}

	public static String createVoiceResponse(String to, String identity) {
		VoiceResponse voiceTwimlResponse;
		if (to != null && to.equals(TWILIO_CALLER_ID)) {
			System.out.println("In first condition");
			Client client = new Client.Builder(identity).build();
			Dial dial = new Dial.Builder().client(client).build();
			voiceTwimlResponse = new VoiceResponse.Builder().dial(dial).build();
		} else if (to != null && !to.isEmpty()) {
			System.out.println("In condition where to != null");
			System.out.println(to);
			System.out.println(identity);
			Dial.Builder dialBuilder = new Dial.Builder().callerId(TWILIO_CALLER_ID);

			Dial.Builder dialBuilderWithReceiver = addChildReceiver(dialBuilder, to);

			voiceTwimlResponse = new VoiceResponse.Builder().dial(dialBuilderWithReceiver.build()).build();
		} else {
			voiceTwimlResponse = new VoiceResponse.Builder().say(new Say.Builder("Thanks for calling!").build())
					.build();
		}

		return voiceTwimlResponse.toXml();
	}

	private static boolean isPhoneNumber(String to) {
		return to.matches("^[\\d\\+\\-\\(\\) ]+$");
	}

	private static Dial.Builder addChildReceiver(Dial.Builder builder, String to) {
		// wrap the phone number or client name in the appropriate TwiML verb
		// by checking if the number given has only digits and format symbols
		if (isPhoneNumber(to)) {
			return builder.number(new Number.Builder(to).build());
		}
		return builder.client(new Client.Builder(to).build());
	}

	private void setCorsHeaders(HttpHeaders headers) {
		headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
		headers.set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, "3600");
		headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization, X-Requested-With");
	}

	private Attribute getAttributes(FullHttpRequest req) {

		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
		Attribute attribute = null;
		try {
			decoder.offer(req);
			List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : postData) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					attribute = (Attribute) data;
				}
			}
		} finally {
			decoder.destroy();
		}
		return attribute;

	}

}
