import nodemailer from "nodemailer";
import { HttpException } from "@exceptions/HttpException";
import CONFIG from "@config";
import { SupportDto } from "@dtos/support.dtos";
import { logger } from "@utils/logger";
import { isEmpty } from "@utils/util";
import { deleteFilesRecursive, deleteReports } from "@utils/fileManager";
import fs, { existsSync, mkdirSync } from "fs";
import { join } from "path";
const repDir: string = join(__dirname, "/../reports");
if (!existsSync(repDir)) {
    mkdirSync(repDir);
}

class SupportService {
    public async sendReport(
        supportData: SupportDto,
        file: Express.Multer.File
    ): Promise<void> {
        const transport = nodemailer.createTransport({
            service: 'Gmail',
            auth: {
                user: CONFIG.SUPPORT.SUPPORT_EMAIL_HOST_USER.toString(),
                pass: CONFIG.SUPPORT.SUPPORT_EMAIL_HOST_PASSWORD.toString(),
            },
        });

        transport.verify((err: Error, success: boolean) => {
            if (!success) {
                logger.error(err);
                throw new HttpException(500, "Server error");
            }
        });

        let message = {};
        if (file) {
            if (supportData.message) {
                message = {
                    from: supportData.from,
                    to: "afubillaoliva@gmail.com",
                    subject: "Alfalab - Reporte de usuario",
                    text: supportData.message,
                    attachments: [
                        {
                            filename: file.originalname,
                            path: file.path,
                        },
                    ],
                };
            } else {
                message = {
                    from: CONFIG.SUPPORT.SUPPORT_EMAIL,
                    to: "afubillaoliva@gmail.com",
                    subject: "Alfalab - Crash report",
                    attachments: [
                        {
                            filename: file.originalname,
                            path: file.path,
                        },
                    ],
                };
            }
        } else {
            message = {
                from: CONFIG.SUPPORT.SUPPORT_EMAIL,
                to: "afubillaoliva@gmail.com",
                subject: "Alfalab - Reporte de usuario",
                text: supportData.message,
            };
        }

        transport.sendMail(message, (err) => {
            if (err) {
                logger.error(err);
                throw new HttpException(500, err.message);
            } else {
                try {
                    fs.stat(repDir, function (err, stats) {
                        if (!err) deleteFilesRecursive(repDir);
                    });
                } catch (error) {
                    logger.error(error);
                    throw new HttpException(500, error.message);
                }
            }
        });
    }
}

export default SupportService;
