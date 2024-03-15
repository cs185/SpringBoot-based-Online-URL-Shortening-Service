package com.example.semproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;

import java.io.IOException;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final BigtableDataClient dataClient;

    public UserService() {
        try {
            this.dataClient = BigtableDataClient.create("rice-comp-539-spring-2022", "shared-539");
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
        String tableName = "users";
        String columnFamily = "user_info";

        RowMutation rowMutation = RowMutation.create(tableName, username)
                .setCell(columnFamily, "password", hashedPassword)
                .setCell(columnFamily, "email", email)
                .setCell(columnFamily, "is_premium", isPremium ? "true" : "false");

        dataClient.mutateRow(rowMutation);
        System.out.println("User " + username + " registered/updated successfully.");

    }

    public boolean verifyUser(String username, String password) {
        // Placeholder for your implementation
        return false;
    }

    // Ensure to close your BigtableDataClient resource properly
    // Consider using @PreDestroy for cleanup
}
