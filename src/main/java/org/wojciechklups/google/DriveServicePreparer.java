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
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.wojciechklups.google.SheetsServicePreparer.JSON_FACTORY;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-09-08 16:19:47 +0200 (08 wrz 2022)
 */
public class DriveServicePreparer
{
    public static Drive getDriveService() throws IOException, GeneralSecurityException
    {
        Credential credential = GoogleAuthorizeUtil.authorize();

         return new Drive.Builder(
                 GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, credential)
                .setApplicationName("Drive searcher")
                .build();

    }
}
