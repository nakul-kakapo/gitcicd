package twiliocaller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;

public class VoiceCaller {

	public static void main(String[] args) {
		
		
		Twilio.init(
	            "ACf385b106208583c1057e9f7d5f05a618",
	            "fa21deece7cc62c12b7ae70b4f3903c0");

	        String helloTwiml = new VoiceResponse.Builder()
	            .say(new Say.Builder("Hello from Twilio")
	                .voice(Say.Voice.POLLY_MATTHEW).build())
	            .build().toXml();

	        Call call = Call.creator(
	                new PhoneNumber("+919809355419"),
	                new PhoneNumber("+15412348138"),
	                new Twiml(helloTwiml))
	            .create();
	        System.out.println(call.getSid());
	        

	}

}
