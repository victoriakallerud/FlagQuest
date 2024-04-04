
## Description

The FlagQuest backend is using the Nest.js TypeScript framework.

## Requirements

- Node.js

## Installation

Make sure you are in the `FlagQuest/backend` directory.
Run: 
```bash
$ npm install
```

## Running the app

### Create your own environment file for the database connection:

Duplicate the included `default_env.md` file and rename it to `development.env`.

Change the path for 

```env
GOOGLE_APPLICATION_CREDENTIALS="yourpathhere"
```
to the absolute path of the `backend/src/keys/flagquest-d4502-firebase-adminsdk-a20ch-9e9693fbee.json` file.

Make sure to leave the `"..."` characters!

### Start the backend

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

## Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```

- Twitter - [@nestframework](https://twitter.com/nestframework)

## License

Nest is [MIT licensed](LICENSE).
