/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.service.sheetsservices;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-11-25 20:06:46 +0200 (25 lis 2022)
 */
public interface SheetService
{
    void setup() throws GeneralSecurityException, IOException;

    void writePrices(List<Double> prices) throws IOException;
}
