package com.example.firstprototype;


import android.os.StrictMode;

import java.util.Arrays;
import java.io.IOException;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;
import com.nylas.Draft;
import com.nylas.NameEmail;

public class EmailsHelper {

    public static void send() throws RequestFailedException, IOException {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Create client object and connect it to Nylas using
            // an account's access token
            NylasClient client = new NylasClient();
            // Provide the access token for a specific account
            NylasAccount account = client.account("RkzyAwveHk84vTMR6mdn7XR2b1dTp2");

            Draft draft = new Draft();
            draft.setSubject("With Love, from szszszzsz");
            draft.setBody("XDDDDD szymixa");
            draft.setTo(Arrays.asList(new NameEmail("My Friend", "bananoweherbatniki@o2.pl")));

            account.drafts().send(draft);
        }

    }
}