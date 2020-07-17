ALTER TABLE IF EXISTS password_reset_tokens ALTER COLUMN token TYPE uuid USING token::uuid;
ALTER TABLE IF EXISTS password_reset_tokens ALTER COLUMN token SET DEFAULT gen_random_uuid();
