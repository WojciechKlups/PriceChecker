/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.wojciechklups.enums.SheetsEnum;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles authorization with google services.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 13:57:29 +0200 (29 sie 2022)
 */
public class GoogleAuthorizeUtil
{
    private static final List<String> SCOPES = Arrays.asList(
            SheetsScopes.SPREADSHEETS,
            SheetsScopes.DRIVE,
            SheetsScopes.DRIVE_FILE,
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE);

    public static Credential authorize() throws IOException, GeneralSecurityException
    {
        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream(SheetsEnum.CREDENTIALS_FILE_PATH.getValue());

        if (in == null)
        {
            throw new FileNotFoundException("Resource not found: " + SheetsEnum.CREDENTIALS_FILE_PATH.getValue());
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(SheetsEnum.TOKENS_DIRECTORY_PATH.getValue())))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
