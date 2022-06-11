import { NextFunction, Request, Response } from 'express';
import FormatsService from '@services/format.service';
import { FormatDto } from '@dtos/formats.dto';

class FormatController {
  public formatsService = new FormatsService();

  public getAllFormats = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const findAllFormatData: FormatDto[] = await this.formatsService.findAllFormats();

      res.status(200).json({ data: findAllFormatData, message: 'findAll' });
    } catch (error) {
      next(error);
    }
  };

  public getFormatById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const formatId: string = req.params.id;
      const findOneFormatData: FormatDto = await this.formatsService.findFormatById(formatId);

      res.status(200).json({ data: findOneFormatData, message: 'findOne' });
    } catch (error) {
      next(error);
    }
  };

  public createFormat = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const formatData: FormatDto = req.body;
      const createFormatData: FormatDto = await this.formatsService.createFormat(formatData);

      res.status(201).json({ data: createFormatData, message: 'created' });
    } catch (error) {
      next(error);
    }
  };

  public updateFormat = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const formatId: string = req.params.id;
      const formatData: FormatDto = req.body;
      const updateFormatData: FormatDto = await this.formatsService.updateFormat(formatId, formatData);

      res.status(200).json({ data: updateFormatData, message: 'updated' });
    } catch (error) {
      next(error);
    }
  };

  public deleteFormat = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const formatId: string = req.params.id;
      const deleteFormatData: FormatDto = await this.formatsService.deleteFormatById(formatId);

      res.status(200).json({ data: deleteFormatData, message: 'deleted' });
    } catch (error) {
      next(error);
    }
  };
}

export default FormatController;
