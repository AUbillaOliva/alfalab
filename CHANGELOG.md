# Changelog

All notable changes to this project will be documented in this file.

---

## [2.1.0] - 15-06-22

### [CHANGED]

- Procfile script refactored.
- Updated packages versions in `package.json`.
- Updated `tsconfig.json` types paths`.
- Code refactored in `app.ts`.
- Session types refactored.
- Services refactoreg, using mongoose object ids.
- Server file refactored.
- Orders and Token model refactored, using mongoose object ids.
- Order dto refactored, using mongoose object ids.
- Database connection options removed due to package update.
- Services refactored, using mongoose object ids.
- RequestWithUser interface refactored, added CustomSessionFields interface.
- Client interface refactored.
- Order interface refactored, using mongoose object id, extended interfaces with `Mongoose.Document`.
- Users interface refactored, using mongoose object id, extended interfaces with `Mongoose.Document`.

### [FIXED]

- Using `RequestWithUser` in Tokens service instead of `Request` from express.

### [ADDED]

- Continuous Integration with CircleCi.
- Changelog file added.

## [2.0.0] - 11-06-22

### [CHANGED]

- Project structure using `typescript-express-starter`.
