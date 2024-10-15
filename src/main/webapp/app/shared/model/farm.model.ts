import { ICropType } from 'app/shared/model/crop-type.model';
import { IFarmer } from 'app/shared/model/farmer.model';

export interface IFarm {
  id?: number;
  latitude?: number;
  longitude?: number;
  cellIdentifier?: string;
  cropType?: ICropType | null;
  farmer?: IFarmer | null;
}

export const defaultValue: Readonly<IFarm> = {};
