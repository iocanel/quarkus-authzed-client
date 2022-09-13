package io.quarkiverse.quarkus.authzed.client.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import com.authzed.api.v1.SchemaServiceOuterClass;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.authzed.client.AuthzedClient;
import io.quarkus.test.QuarkusUnitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthzedClientTest {

    @Inject
    AuthzedClient client;

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(AuthzedClientTest.class.getResource("/test-schema"),"/test-schema"));

    

    @Test
    public void shouldCreateSchema() {
        client.v1().schemaService().writeSchema(SchemaServiceOuterClass.WriteSchemaRequest.newBuilder().setSchema(readSchema()).build());
        SchemaServiceOuterClass.ReadSchemaResponse response = client.v1().schemaService().readSchema(SchemaServiceOuterClass.ReadSchemaRequest.newBuilder().build());
        assertNotNull(response);
    }
    
    private String readSchema() {
        try {
            return Files.readString(Path.of(AuthzedClientTest.class.getResource("/test-schema").getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
