package org.zerock.b01;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;

    // MySql 데이터베이스 연결 테스트
    @Test
    public void testConnection() throws SQLException{

        @Cleanup
        Connection con = dataSource.getConnection();

        log.info(con);

        Assertions.assertNotNull(con);
    }
}
