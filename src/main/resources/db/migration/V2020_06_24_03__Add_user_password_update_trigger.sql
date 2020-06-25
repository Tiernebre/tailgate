CREATE OR REPLACE FUNCTION audit_user_password_update()
  RETURNS trigger AS
$BODY$
BEGIN
	IF NEW.password <> OLD.password THEN
		 INSERT INTO audit.user_password_updates(user_id)
		 VALUES(OLD.id);
	END IF;

	RETURN NEW;
END;
$BODY$

LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER user_password_changes
  BEFORE UPDATE
  ON public.users
  FOR EACH ROW
  EXECUTE PROCEDURE audit_user_password_update();
