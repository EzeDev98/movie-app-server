package com.movie.app.utility.impl;

import com.movie.app.utility.UniqueValueGeneratorService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.movie.app.constants.GeneralConstants.RANDOM_STRING_LENGTH;


@Service
public class UniqueValueGeneratorServiceImpl implements UniqueValueGeneratorService {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    @Override
    public String generateWalletId() {
        UUID uuid = UUID.randomUUID();
        String walletId = "W" + uuid.toString().replace("-", "").substring(0, 6);
        return walletId;
    }

    @Override
    public String generateLoginId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String generateSyncId() {
        String timeComponent = Long.toString(System.currentTimeMillis(), 36).toUpperCase();
        String randomString = generateRandomString(RANDOM_STRING_LENGTH);
        return timeComponent + "_" + randomString;
    }

    @Override
    public String generateRandomString(int length) {
        byte[] randomBytes = new byte[length];
        SECURE_RANDOM.nextBytes(randomBytes);
        String randomString = toBase36String(randomBytes);
        return randomString.substring(0, length).toUpperCase();
    }

    private String toBase36String(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xFF), 36));
        }
        return sb.toString();
    }

    @Override
    public String generateSyncTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter nameFormatter = DateTimeFormatter.ofPattern("EEEE");
        String dayName = now.format(nameFormatter);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = now.format(dateTimeFormatter);
        return dayName + ", " + formattedTime;
    }

    @Override
    public String generateTransactionRef() {
        long timeStamp = System.currentTimeMillis();
        String id = UUID.randomUUID().toString();
        String uniqueId = id.split("-")[0];
        return "TXN-" + timeStamp + "-" + uniqueId;
    }
}
