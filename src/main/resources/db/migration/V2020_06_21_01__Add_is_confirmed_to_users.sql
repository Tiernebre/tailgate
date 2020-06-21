ALTER TABLE users ADD COLUMN IF NOT EXISTS is_confirmed BOOLEAN NOT NULL DEFAULT false;

CREATE INDEX users_is_confirmed_idx ON users (is_confirmed);
