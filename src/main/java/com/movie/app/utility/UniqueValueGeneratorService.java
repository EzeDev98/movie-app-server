package com.movie.app.utility;

public interface UniqueValueGeneratorService {
    String generateWalletId();
    String generateLoginId();
    String generateSyncId();
    String generateRandomString(int length);
    String generateSyncTime();
    String generateTransactionRef();

}
