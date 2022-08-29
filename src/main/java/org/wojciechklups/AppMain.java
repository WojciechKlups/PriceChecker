package org.wojciechklups;

import com.google.api.services.sheets.v4.Sheets;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.wojciechklups.google.SheetsService;
import org.wojciechklups.service.ResponsePreparer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class AppMain extends SpringBootServletInitializer
{
    public static void main(String[] args) throws GeneralSecurityException, IOException
    {
        ResponsePreparer responsePreparer = new ResponsePreparer();
        List<String> preparedResponses = responsePreparer.getPreparedResponses();

        preparedResponses.stream()
                .forEach(System.out::println);

        SheetsService.setup();
        SheetsService.writeSomething();
    }
}
