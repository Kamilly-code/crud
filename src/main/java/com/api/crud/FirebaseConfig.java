package com.api.crud;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;


@Configuration
public class FirebaseConfig  {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void init() {
        try {
            String base64 = System.getenv("FIREBASE_CONFIG_BASE64");

            if (base64 == null || base64.isEmpty()) {
                throw new IllegalStateException("Variable de entorno FIREBASE_CONFIG_BASE64 no encontrada!");
            }

            byte[] decoded = Base64.getDecoder().decode(base64);
            InputStream serviceAccount = new ByteArrayInputStream(decoded);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase inicializado con éxito (vía variable de entorno Base64).");
            }

        } catch (Exception e) {
            log.error("Error al iniciar Firebase", e);
            throw new RuntimeException("Error al iniciar Firebase: " + e.getMessage(), e);
        }
    }
}
