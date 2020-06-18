
package gkd.simple.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("gkd.simple.mall.dao")
@SpringBootApplication
public class SimpleMallAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleMallAPIApplication.class, args);
    }

}
