package com.example.semproject;

import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.*;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;

import java.util.ArrayList;
import java.util.List;

public class BigTableExample {
    // Reference -> https://cloud.google.com/bigtable/docs/samples-java-hello-world

    public static void main(String[] args) throws Exception {
        // Add below Env variable in configuration of Intellij
        // GOOGLE_APPLICATION_CREDENTIALS=/Users/michaek05130505gmail.com/Downloads/rice-comp-539-spring-2022-8a812ce96ddd.json
        String projectId = "rice-comp-539-spring-2022";
        String instanceId = "shared-539";
        String tableId = "kaushal123";
        String COLUMN_FAMILY = "cf1";
        String COLUMN_QUALIFIER_GREETING = "greeting";
        String COLUMN_QUALIFIER_NAME = "name";
        String ROW_KEY_PREFIX = "rowKey";

        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        BigtableDataClient dataClient = BigtableDataClient.create(settings);

        BigtableTableAdminSettings adminSettings = BigtableTableAdminSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        BigtableTableAdminClient adminClient = BigtableTableAdminClient.create(adminSettings);

        if (!adminClient.exists(tableId)) {
            System.out.println("Creating table: " + tableId);
            CreateTableRequest createTableRequest =
                    CreateTableRequest.of(tableId).addFamily(COLUMN_FAMILY);
            adminClient.createTable(createTableRequest);
            System.out.printf("Table %s created successfully%n", tableId);
        }

        try {
            System.out.println("\nWriting some greetings to the table");
            String[] names = {"World", "Bigtable", "Java"};
            for (int i = 0; i < names.length; i++) {
                String greeting = "Hello " + names[i] + "!";
                RowMutation rowMutation =
                        RowMutation.create(tableId, ROW_KEY_PREFIX + i)
                                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME, names[i])
                                .setCell(COLUMN_FAMILY, COLUMN_QUALIFIER_GREETING, greeting);
                dataClient.mutateRow(rowMutation);
                System.out.println(greeting);
            }
        } catch (NotFoundException e) {
            System.err.println("Failed to write to non-existent table: " + e.getMessage());
        }


        try {
            System.out.println("\nReading a single row by row key");
            Row row = dataClient.readRow(tableId, ROW_KEY_PREFIX + 0);
            System.out.println("Row: " + row.getKey().toStringUtf8());
            for (RowCell cell : row.getCells()) {
                System.out.printf(
                        "Family: %s    Qualifier: %s    Value: %s%n",
                        cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
            }
            // return row;
        } catch (NotFoundException e) {
            System.err.println("Failed to read from a non-existent table: " + e.getMessage());
            // return null;
        }
        try {
            System.out.println("\nReading specific cells by family and qualifier");
            Row row = dataClient.readRow(tableId, ROW_KEY_PREFIX + 0);
            System.out.println("Row: " + row.getKey().toStringUtf8());
            List<RowCell> cells = row.getCells(COLUMN_FAMILY, COLUMN_QUALIFIER_NAME);
            for (RowCell cell : cells) {
                System.out.printf(
                        "Family: %s    Qualifier: %s    Value: %s%n",
                        cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
            }
            // return cells;
        } catch (NotFoundException e) {
            System.err.println("Failed to read from a non-existent table: " + e.getMessage());
            // return null;
        }


        try {
            System.out.println("\nReading the entire table");
            Query query = Query.create(tableId);
            ServerStream<Row> rowStream = dataClient.readRows(query);
            List<Row> tableRows = new ArrayList<>();
            for (Row r : rowStream) {
                System.out.println("Row Key: " + r.getKey().toStringUtf8());
                tableRows.add(r);
                for (RowCell cell : r.getCells()) {
                    System.out.printf(
                            "Family: %s    Qualifier: %s    Value: %s%n",
                            cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
                }
            }
            // return tableRows;
        } catch (NotFoundException e) {
            System.err.println("Failed to read a non-existent table: " + e.getMessage());
            // return null;
        }

        System.out.println("\nDeleting table: " + tableId);
        try {
            adminClient.deleteTable(tableId);
            System.out.printf("Table %s deleted successfully%n", tableId);
        } catch (NotFoundException e) {
            System.err.println("Failed to delete a non-existent table: " + e.getMessage());
        }
    }
}

