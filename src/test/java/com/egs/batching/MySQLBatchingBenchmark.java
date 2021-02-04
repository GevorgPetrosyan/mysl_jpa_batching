package com.egs.batching;

import com.egs.batching.jpa.generation.type.identity.IdentityGenerationType;
import com.egs.batching.jpa.generation.type.identity.IdentityGenerationTypeRepository;
import com.egs.batching.jpa.generation.type.mysql_native.MySQLNativeIdentityGenerationType;
import com.egs.batching.jpa.generation.type.mysql_native.MySQLNativeIdentityGenerationTypeRepository;
import com.egs.batching.jpa.generation.type.table.TableGenerationType;
import com.egs.batching.jpa.generation.type.table.TableGenerationTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MySQLBatchingBenchmark {

    private static final String PREPARED_STATEMENT = "insert into identity_generation_type (description) values (?)";

    public static final int BATCH_SIZE = 1000;

    public static final String BATCH_PREFIX = "batch_";

    @Autowired
    private EntityManagerFactory factory;

    @Autowired
    private IdentityGenerationTypeRepository identityGenerationTypeRepository;

    @Autowired
    private TableGenerationTypeRepository tableGenerationTypeRepository;

    @Autowired
    private MySQLNativeIdentityGenerationTypeRepository mySQLNativeIdentityGenerationTypeRepository;

    @Before
    public void warmup() {
        for (int i = 0; i < 100; i++) {
            generateBatchNames();
        }
    }

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
    }

    @Test
    public void executeSeparateBatches() {
        final SessionFactory sessionFactory = factory.unwrap(SessionFactory.class);
        final Session session = sessionFactory.openSession();
        session.doWork(connection -> {
            for (int i = 0; i < BATCH_SIZE; i++) {
                final Transaction transaction = session.beginTransaction();
                try (final PreparedStatement pstmt = connection.prepareStatement(PREPARED_STATEMENT)) {
                    pstmt.setString(1, BATCH_PREFIX + i);
                    pstmt.addBatch();
                    pstmt.executeBatch();
                }
                transaction.commit();
            }
        });
        session.close();
    }

    @Test
    public void executeSeparatelyWithIdentityGenerationType() {
        for (int i = 0; i < BATCH_SIZE; i++) {
            identityGenerationTypeRepository.save(new IdentityGenerationType(BATCH_PREFIX + i));
        }
    }

    @Test
    public void executeSeparatelyWithTableGenerationType() {
        List<TableGenerationType> tableGenerationTypes = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            tableGenerationTypes.add(new TableGenerationType(BATCH_PREFIX + i));
        }
        tableGenerationTypeRepository.saveAll(tableGenerationTypes);
    }

    @Test
    public void executeSeparatelyWithNativeIdentityGenerationType() {
        List<MySQLNativeIdentityGenerationType> batchWithTables = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            batchWithTables.add(new MySQLNativeIdentityGenerationType(BATCH_PREFIX + i));
        }
        mySQLNativeIdentityGenerationTypeRepository.saveAll(batchWithTables);
    }

    private List<String> generateBatchNames() {
        final List<String> batchNames = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            batchNames.add(BATCH_PREFIX + i);
        }
        return batchNames;
    }

}
