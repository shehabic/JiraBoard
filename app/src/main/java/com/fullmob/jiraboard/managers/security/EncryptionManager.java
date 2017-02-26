package com.fullmob.jiraboard.managers.security;

import android.content.Context;
import android.util.Base64;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;

public class EncryptionManager implements EncrypterInterface {

    private final Crypto crypto;

    public EncryptionManager(Context context) {
        SharedPrefsBackedKeyChain keyChain = new SharedPrefsBackedKeyChain(context, CryptoConfig.KEY_256);
        crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
    }

    @Override
    public String encrypt(String key, String plainText) throws Exception {
        return Base64.encodeToString(
            crypto.encrypt(
                plainText.getBytes(),
                Entity.create(key)
            ),
            Base64.NO_WRAP
        );
    }

    @Override
    public String decrypt(String key, String cipherText) throws Exception {
        return new String(
            crypto.decrypt(
                Base64.decode(
                    cipherText,
                    Base64.NO_WRAP
                ),
                Entity.create(key)
            )
        );
    }
}
