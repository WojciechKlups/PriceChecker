/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This class contains various constants and one method for creating google sheets service object.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:03:05 +0200 (29 sie 2022)
 */
@Component
public class SheetsServicePreparer
{
    public static final String CREDENTIALS_FILE_PATH = "/google-sheets-client-secret.json";
    public static final String SPREADSHEET_ID = "1iNzLpyVqGmnHeyOU-VkjsqhUca2g7RPdtMFJNLUPQaU";//"1lPzVIsAs_hNmO-2AvKmovfOHSIE_06fyOpK7LTvqWLA";
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String APPLICATION_NAME = "Price Checker Sheet";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException
    {
        Credential credential = GoogleAuthorizeUtil.authorize();
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
