import dayjs from 'dayjs';
import { IBusinessType } from 'app/shared/model/business-type.model';

export interface IBusiness {
  id?: number;
  registeredName?: string;
  organisationName?: string | null;
  vatNumber?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  businessType?: IBusinessType | null;
}

export const defaultValue: Readonly<IBusiness> = {};
