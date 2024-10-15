import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBusiness } from 'app/shared/model/business.model';

export interface IFarmer {
  id?: number;
  firstname?: string;
  lastname?: string;
  email?: string;
  position?: string;
  phone?: string | null;
  address?: string | null;
  city?: string | null;
  province?: string | null;
  country?: string | null;
  postalCode?: string | null;
  registrationDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  business?: IBusiness | null;
}

export const defaultValue: Readonly<IFarmer> = {};
