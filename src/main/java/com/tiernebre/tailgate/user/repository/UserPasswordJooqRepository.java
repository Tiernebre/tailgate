package com.tiernebre.tailgate.user.repository;

public class UserPasswordJooqRepository implements UserPasswordRepository {
    @Override
    public void updatePasswordForOneWithEmailAndNonExpiredPasswordResetToken(String password, String email, String passwordResetToken) {

    }
}
