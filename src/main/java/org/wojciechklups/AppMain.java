package org.wojciechklups;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.wojciechklups.enums.ProductPageEnum;
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
        SpringApplication.run(AppMain.class, args);
        ResponsePreparer responsePreparer = new ResponsePreparer();
        List<Double> preparedResponses = responsePreparer.getPreparedResponses();

//        preparedResponses.stream()
//                .forEach(System.out::println);

        SheetsService.setup();
        System.out.println(SheetsService.readLastPrice(ProductPageEnum.PSU.getColumn()));
//        SheetsService.writePrices(preparedResponses);
    }
}
