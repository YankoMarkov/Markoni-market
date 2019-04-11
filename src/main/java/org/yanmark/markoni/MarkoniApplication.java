package org.yanmark.markoni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarkoniApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkoniApplication.class, args);
    }

}
