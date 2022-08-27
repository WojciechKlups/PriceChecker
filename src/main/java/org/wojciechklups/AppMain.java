package org.wojciechklups;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.wojciechklups.service.ResponsePreparer;

import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class AppMain extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        ResponsePreparer responsePreparer = new ResponsePreparer();
        List<String> preparedResponses = responsePreparer.getPreparedResponses();

        preparedResponses.stream()
                .forEach(System.out::println);
    }
}
