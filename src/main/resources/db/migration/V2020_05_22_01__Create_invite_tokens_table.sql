CREATE TABLE IF NOT EXISTS invite_tokens (
  token TEXT PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  user_id BIGINT REFERENCES users (id) NOT NULL
);