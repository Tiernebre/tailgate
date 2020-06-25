CREATE TABLE IF NOT EXISTS audit.user_password_updates (
    user_id BIGINT REFERENCES public.users (id),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
):