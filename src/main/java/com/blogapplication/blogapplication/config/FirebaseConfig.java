package com.blogapplication.blogapplication.config;
import com.google.auth.oauth2.GoogleCredentials;
;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

//@Configuration
public class FirebaseConfig {

//    @Bean
    public Storage storage() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("D:/My work/Projects/blogapllication/src/main/resources/config/blog-application-ec7cc-firebase-adminsdk-3ilfb-f3a52359f8.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
        return options.getService();
    }
}


