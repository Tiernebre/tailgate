ALTER TABLE IF EXISTS user_confirmation_tokens ALTER COLUMN token TYPE uuid USING token::uuid;
ALTER TABLE IF EXISTS user_confirmation_tokens ALTER COLUMN token SET DEFAULT gen_random_uuid();
