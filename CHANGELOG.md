# Changelog

All notable changes to this project will be documented in this file.

---

## [2.3.0] - 11-08-22

### [CHANGED]

- Updated packages versions in `package.json`.
- Added config for support email.
- Search Filters for Orders.
- Order status.
- Order interface refactored.
- Token middleware for auth token.
- Reset route in `auth.route`.
- Reset password route in `users.route`.
- Reset token services in `auth.services`.
- Code format refactored.
- Reset password services in `users.service`.

### [ADDED]

- Added HTML pages for mail responses.
- Dtos classes for auth and support.
- Interface for api response.
- File manager for mail responses.
- Handlebars, nodemailer, multer packages added.
- Added reset fun in Auth controller.
- Reset password fun in Orders controller.
- OrderQueryOptions and OrderQuery interfaces added.
- Support routes in server.

### [REMOVED]

- LoginUserDto removed from `users.dto`.

---

## [2.2.0] - 10-07-22

### [CHANGED]

- OrderItem Level type refactored to `Number`.
- Order Schema Updated due to top change.
- Token expiration error handling. 

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
