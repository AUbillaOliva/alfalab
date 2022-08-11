import { NextFunction, Request, Response } from "express";
import { SupportDto } from "@dtos/support.dtos";
import SupportService from "@services/support.service";
import SMTPTransport from "nodemailer/lib/smtp-transport";

class SupportController {
  public supportService = new SupportService();

  public sendReport = async (
    req: Request,
    res: Response,
    next: NextFunction
  ): Promise<void> => {
    const createEmailData: SupportDto = req.body;
    try {
      await this.supportService.sendReport(createEmailData, req.file);
      res.status(200).json({ message: "OK" });
    } catch (error) {
      next(error);
    }
  };
}

export default SupportController;
