package com.tiernebre.tailgate.user.repository;

public class UserPasswordJooqRepository implements UserPasswordRepository {
    @Override
    public void updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken) {

    }
}
