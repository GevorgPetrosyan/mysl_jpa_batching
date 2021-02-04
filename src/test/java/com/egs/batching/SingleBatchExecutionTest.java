package com.egs.batching;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.sql.PreparedStatement;
import java.util.stream.IntStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SingleBatchExecutionTest {

    private static final String PREPARED_STATEMENT = "insert into batch_with_identity (description) values (?)";

    public static final String LATEST_EXECUTED_QUERY = "select argument from mysql.general_log where argument like '%insert%' order by event_time desc limit 2";

    public static final int BATCH_SIZE = 1000;

    public static final String BATCH_PREFIX = "batch_";

    @Autowired
    private EntityManagerFactory factory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void executeSingleBatch() {
        final SessionFactory sessionFactory = factory.unwrap(SessionFactory.class);
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        session.doWork(connection -> {
            try (final PreparedStatement pstmt = connection.prepareStatement(PREPARED_STATEMENT)) {
                for (int i = 0; i < BATCH_SIZE; i++) {
                    pstmt.setString(1, BATCH_PREFIX + i);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        });

        transaction.commit();
        session.close();
        final String result = jdbcTemplate.queryForList(LATEST_EXECUTED_QUERY, String.class).get(1);
        Assert.assertTrue(IntStream.range(0, BATCH_SIZE).mapToObj(i -> BATCH_PREFIX + i).allMatch(result::contains));
    }

}