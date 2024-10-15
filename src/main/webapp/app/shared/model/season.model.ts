import dayjs from 'dayjs';
import { ICropType } from 'app/shared/model/crop-type.model';

export interface ISeason {
  id?: number;
  seasonName?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  isActive?: boolean | null;
  cropType?: ICropType | null;
}

export const defaultValue: Readonly<ISeason> = {
  isActive: false,
};
