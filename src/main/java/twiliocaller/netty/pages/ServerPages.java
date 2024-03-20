package twiliocaller.netty.pages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ServerPages {
	
	
	public static ByteBuf getContent() {
		return Unpooled.copiedBuffer("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "  <head>\r\n"
				+ "    <title>Twilio Voice</title>\r\n"
				+ "    <link rel=\"stylesheet\" href=\"js/site.css\" />\r\n"
				+ "  </head>\r\n"
				+ "  <body>\r\n"
				+ "    <header>\r\n"
				+ "      <h1>Twilio Voice</h1>\r\n"
				+ "      <button id=\"startup-button\">Start up the Device</button>\r\n"
				+ "    </header>\r\n"
				+ "    <main id=\"controls\">\r\n"
				+ "      <section class=\"left-column\" id=\"info\">\r\n"
				+ "        <h2>Your Device Info</h2>\r\n"
				+ "        <div id=\"client-name\"></div>\r\n"
				+ "        <div id=\"output-selection\" class=\"hide\">\r\n"
				+ "          <label>Ringtone Devices</label>\r\n"
				+ "          <select id=\"ringtone-devices\" multiple></select>\r\n"
				+ "          <label>Speaker Devices</label>\r\n"
				+ "          <select id=\"speaker-devices\" multiple></select\r\n"
				+ "          ><br />\r\n"
				+ "          <button id=\"get-devices\">Seeing \"Unknown\" devices?</button>\r\n"
				+ "        </div>\r\n"
				+ "      </section>\r\n"
				+ "      <section class=\"center-column\">\r\n"
				+ "        <h2 class=\"instructions\">Make a Call</h2>\r\n"
				+ "        <div id=\"call-controls\" class=\"hide\">\r\n"
				+ "          <form>\r\n"
				+ "            <label for=\"phone-number\"\r\n"
				+ "              >Enter a phone number or client name</label\r\n"
				+ "            >\r\n"
				+ "            <input id=\"phone-number\" type=\"text\" placeholder=\"+15552221234\" />\r\n"
				+ "            <button id=\"button-call\" type=\"submit\">Call</button>\r\n"
				+ "          </form>\r\n"
				+ "          <button id=\"button-hangup-outgoing\" class=\"hide\">Hang Up</button>\r\n"
				+ "          <div id=\"incoming-call\" class=\"hide\">\r\n"
				+ "            <h2>Incoming Call Controls</h2>\r\n"
				+ "            <p class=\"instructions\">\r\n"
				+ "              Incoming Call from <span id=\"incoming-number\"></span>\r\n"
				+ "            </p>\r\n"
				+ "            <button id=\"button-accept-incoming\">Accept</button>\r\n"
				+ "            <button id=\"button-reject-incoming\">Reject</button>\r\n"
				+ "            <button id=\"button-hangup-incoming\" class=\"hide\">Hangup</button>\r\n"
				+ "          </div>\r\n"
				+ "          <div id=\"volume-indicators\" class=\"hide\">\r\n"
				+ "            <label>Mic Volume</label>\r\n"
				+ "            <div id=\"input-volume\"></div>\r\n"
				+ "            <br /><br />\r\n"
				+ "            <label>Speaker Volume</label>\r\n"
				+ "            <div id=\"output-volume\"></div>\r\n"
				+ "          </div>\r\n"
				+ "        </div>\r\n"
				+ "      </section>\r\n"
				+ "      <section class=\"right-column\">\r\n"
				+ "        <h2>Event Log</h2>\r\n"
				+ "        <div class=\"hide\" id=\"log\"></div>\r\n"
				+ "      </section>\r\n"
				+ "    </main>\r\n"
				+ "\r\n"
				+ "    <script  type=\"text/javascript\" src=\"//ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\r\n"
				+ "    <script type=\"text/javascript\" src=\"js/twilio.min.js\"></script>\r\n"
				+ "    <script  type=\"text/javascript\" src=\"js/quickstart.js\"></script>\r\n"
				+ "  </body>\r\n"
				+ "</html>\r\n"
				+ "", CharsetUtil.US_ASCII);
		
	}

}
