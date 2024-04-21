
## Description

The FlagQuest backend is using the Nest.js TypeScript framework. It contains a List of REST Enpoints which can bee seen here http://flagquest.leotm.de:3000/docs and the WebSocket event listeners and emitters can be seen in the WebSocket Gateway.

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

## Advanced

### Running the backend on Debian VPS

#### Requirements

- `node` and `git` installed

#### Steps

1. Generate deployment SSH key on the VPS
2. Add key to GitHub repo
3. `git clone` the repo on the VPS
4. `cd FlagQuest` and `cd backend`
5. Run `npm install` to install dependencies
6. Create your `development.env` file according to the tutorial above
7. Install `pm2` globally using `npm install pm2 -g`
8. Run `npm run build` to build the backend to the `dist` folder
9. Run `export NODE_ENV=production && pm2 start dist/main.js` to set the node environment to `production` and start the backend service with `pm2`

#### Additional commands

```bash
# Show all running pm2 processes
pm2 list

# Stop backend process
pm2 stop main

# Show logs of backend process
pm2 logs main

# Start backend with live output enabled
export NODE_ENV=production && pm2 start dist/main.js --no-daemon
```
