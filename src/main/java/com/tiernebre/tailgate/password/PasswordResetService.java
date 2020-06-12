package com.tiernebre.tailgate.password;

public interface PasswordResetService {
    /**
     * Creates a password reset for a provided request.
     *
     * If the password request is linked to a legitimate user, a delivery of a temporary
     * password reset token is sent to the user using a previously confirmed form of
     * communication.
     *
     * If the password request is illegitimate, then no delivery will occur. But to avoid
     * individuals from using this a means to audit / determine emails, no failure _or_
     * success response is returned from this method.
     *
     * @param passwordResetRequest The request to reset a password.
     */
    void createOne(PasswordResetRequest passwordResetRequest);
}
