- Use Postgres PGCrypto's UUID or random bytes implementation _rather than_ the UUID extension currently used for tokens.
- Use a randomized fingerprint string for JSON Web Token extra security.
- Add indices to JOIN columns that have been forgotten about.

