ALTER TABLE IF EXISTS refresh_tokens ALTER COLUMN token TYPE uuid USING token::uuid;
ALTER TABLE IF EXISTS refresh_tokens ALTER COLUMN token SET DEFAULT gen_random_uuid();
