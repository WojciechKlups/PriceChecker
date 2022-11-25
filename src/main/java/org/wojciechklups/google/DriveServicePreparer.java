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
import com.google.api.services.drive.Drive;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author Author: wklups
 * @timestamp Date: 2022-09-08 16:19:47 +0200 (08 wrz 2022)
 */

@Component
@AllArgsConstructor
public class DriveServicePreparer
{
    public Drive getDriveService() throws IOException, GeneralSecurityException
    {
        Credential credential = GoogleAuthorizeUtil.authorize();

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("Drive searcher")
                .build();

    }
}


