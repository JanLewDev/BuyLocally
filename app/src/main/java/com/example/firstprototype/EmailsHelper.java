package com.example.firstprototype;

import services.Courier;
import services.SendService;
import models.SendEnhancedRequestBody;
import models.SendEnhancedResponseBody;
import models.SendRequestMessage;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;

public class EmailsHelper {

    public static void send() throws IOException {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            Courier.init(ConfigEmail.CourierKey);

            SendEnhancedRequestBody request = new SendEnhancedRequestBody();
            SendRequestMessage message = new SendRequestMessage();

            HashMap<String, String> to = new HashMap<String, String>();
            to.put("email", "lewandowski.jan@o2.pl");
            message.setTo(to);
            message.setTemplate("B4QNN7GN4P47C8MB4B19VJ8J1YF3");

            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("variables", "awesomeness");
            message.setData(data);

            request.setMessage(message);
            try {
                SendEnhancedResponseBody response = new SendService().sendEnhancedMessage(request);
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}