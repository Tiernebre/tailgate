ALTER TABLE IF EXISTS users RENAME TO user;
ALTER TABLE IF EXISTS password_reset_tokens RENAME TO password_reset_token;
ALTER TABLE IF EXISTS refresh_tokens RENAME TO refresh_token;
ALTER TABLE IF EXISTS security_questions RENAME TO security_question;
ALTER TABLE IF EXISTS user_confirmation_tokens RENAME TO user_confirmation_token;
ALTER TABLE IF EXISTS user_security_questions RENAME TO user_security_question;
