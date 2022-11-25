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
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.wojciechklups.enums.SheetsEnum;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This class contains various constants and one method for creating google sheets service object.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:03:05 +0200 (29 sie 2022)
 */
@Component
@AllArgsConstructor
public class SheetsServicePreparer
{
    public Sheets getSheetsService() throws IOException, GeneralSecurityException
    {
        Credential credential = GoogleAuthorizeUtil.authorize();
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName(SheetsEnum.APPLICATION_NAME.getValue())
                .build();
    }
}
