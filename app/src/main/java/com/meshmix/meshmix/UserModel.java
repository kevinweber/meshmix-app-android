package com.meshmix.meshmix;

/**
 * Created by kevinweber on 16/11/15.
 */
public class UserModel {

    String getUserData() {
        String userData = "{'grant_type':'password',"
                + "'username':'k-iwi@web.de',"
                + "'password':'pw-des-users',"
                + "'client_id':'testclient',"
                + "'client_secret':'secret'}";

        return userData;
    }

}
