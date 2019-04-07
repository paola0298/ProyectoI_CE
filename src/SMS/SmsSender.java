package SMS;

// Install the Java helper library from twilio.com/docs/libraries/java
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "AC49c2f86ea29541a25bae6a8d02128982";
    public static final String AUTH_TOKEN =
            "fc3df5871ac07519f21a4391691b6f68";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+50683216963"), // to
                        new PhoneNumber("+16503790702"), // from
                        "Where's Wallace?")
                .create();

        System.out.println(message.getSid());
    }
}