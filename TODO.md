# To-Do

## Features

### Coaches

- Create, Update, Delete, and View Coaches.

### Schools

- Get Schools

### Players

- Automatically create and generate Recruits.
- Create, Update, Delete, and View Positions for Football Players.

## Security

- Implement ReCaptcha Mechanism to Prevent Automation with Logins and Registrations.

## Tech Debt

- User Creation via the Repository owns and knows about the Join table with Security Questions and creates the
  security questions. The Security Question Repository should own this.
- Transaction Management in `UserJooqRepository` should use Spring's Transactional API instead. See https://www.jooq.org/doc/3.1/manual/getting-started/tutorials/jooq-with-spring/
  for specific details.
