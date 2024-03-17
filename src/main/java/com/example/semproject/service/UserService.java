package com.example.semproject.service;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    static final String projectId = "rice-comp-539-spring-2022";
    static final String instanceId = "shared-539";
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final BigtableDataClient dataClient;
    private final String tableName = "users";
    private final String columnFamily = "user_info";

    // The following test function could be uncommented to test if each method under this class works fine
    // All work well so far
//    public static void main(String[] args) throws Exception {
//        String tableId = "users";
//        String COLUMN_FAMILY = "user_info";
//
//        BigtableTableAdminSettings adminSettings = BigtableTableAdminSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
//        BigtableTableAdminClient adminClient = BigtableTableAdminClient.create(adminSettings);
//
//        System.out.println("Created adminClient");
//        if (!adminClient.exists(tableId)) {
//            System.out.println("Creating table: " + tableId);
//            CreateTableRequest createTableRequest =
//                    CreateTableRequest.of(tableId).addFamily(COLUMN_FAMILY);
//            adminClient.createTable(createTableRequest);
//            System.out.printf("Table %s created successfully%n", tableId);
//        }
//
//        UserService myUs = new UserService();
//        myUs.initDefaultUsers();
//
//        System.out.println(myUs.checkUser("testUser"));
//
//        System.out.println(myUs.checkUser("aaa"));
//
//        System.out.println(myUs.verifyUser("testUser", "testPassword"));
//
//        System.out.println(myUs.verifyUser("testUser", "123"));
//
//        System.out.println("\nDeleting table: " + tableId);
//        try {
//            adminClient.deleteTable(tableId);
//            System.out.printf("Table %s deleted successfully%n", tableId);
//        } catch (NotFoundException e) {
//            System.err.println("Failed to delete a non-existent table: " + e.getMessage());
//        }
//    }

    public UserService() {
        try {
            BigtableDataSettings settings =
                    BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
            this.dataClient = BigtableDataClient.create(settings);

        } catch (IOException e) {
            // Handle the exception, possibly rethrow as a runtime exception
            // or log and decide on a fallback or error handling strategy
            throw new RuntimeException("Failed to initialize BigtableDataClient", e);
        }
    }

    private void initDefaultUsers() throws Exception {
        insertOrUpdateUser("testUser", "testPassword", "test@example.com", true);
    }

    public void insertOrUpdateUser(String username, String password, String email, boolean isPremium) throws Exception {
        String hashedPassword = passwordEncoder.encode(password);

        RowMutation rowMutation = RowMutation.create(tableName, username)
                .setCell(columnFamily, "password", hashedPassword)
                .setCell(columnFamily, "email", email)
                .setCell(columnFamily, "is_premium", isPremium ? "true" : "false");

        dataClient.mutateRow(rowMutation);
        System.out.println("User " + username + " registered/updated successfully.");
    }

    public void insertOrUpdateUser(String username, String password, String email) throws Exception {
        String hashedPassword = passwordEncoder.encode(password);

        RowMutation rowMutation = RowMutation.create(tableName, username)
                .setCell(columnFamily, "password", hashedPassword)
                .setCell(columnFamily, "email", email)
                .setCell(columnFamily, "is_premium", "false");

        dataClient.mutateRow(rowMutation);
        System.out.println("User " + username + " registered/updated successfully.");
    }

    public boolean verifyUser(String username, String password) {
        try {
            String columnQualifier = "password";

            if (!checkUser(username)) {
                return false;
            }

            System.out.println("Reading table " + tableName + " by username " + username + "\n");
            Row row = dataClient.readRow(tableName, username);
            System.out.println("Verifying User: " + row.getKey().toStringUtf8());
            RowCell cell = row.getCells(columnFamily, columnQualifier).get(0);

            return passwordEncoder.matches(password, cell.getValue().toStringUtf8());
        } catch (NotFoundException e) {
            System.err.println("Failed to read from a non-existent table: " + e.getMessage());
            // return null;
        }
        return false;
    }

    public boolean checkUser(String username) {
        String tableName = "users";

        System.out.println("checking username: " + username);
        Row result = dataClient.readRow(tableName, username);

        return result != null;
    }

    @PreDestroy
    public void closeBigtableClient() {
        if (this.dataClient != null) {
            this.dataClient.close();
            System.out.println("BigtableDataClient is closed successfully.");
        }
    }
}
