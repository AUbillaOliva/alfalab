// TODO: Refactor this
import { Router } from "express";
import { Routes } from "@interfaces/routes.interface";
import SupportController from "@controllers/support.controller";
import { validationMiddleware } from "@middlewares/validation.middleware";
import { SupportDto } from "@dtos/support.dtos";
import multer from "multer";
import { join } from "path";
import { existsSync, mkdirSync } from "fs";

const repDir: string = join(__dirname, "/../reports");
if (!existsSync(repDir)) {
    mkdirSync(repDir);
}
const supportStorage = multer({ dest: repDir });

class SupportRoute implements Routes {
    public path = "/support";
    public router = Router();
    public supportController = new SupportController();

    constructor() {
        this.initializeRoutes();
    }

    private initializeRoutes() {
        this.router.post(
            `${this.path}`,
            [
                validationMiddleware(SupportDto, "body", true),
                supportStorage.single("file"),
            ],
            this.supportController.sendReport
        );
    }
}

export default SupportRoute;
