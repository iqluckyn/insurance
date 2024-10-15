import dayjs from 'dayjs';
import { ISeason } from 'app/shared/model/season.model';
import { IFarmer } from 'app/shared/model/farmer.model';
import { IProduct } from 'app/shared/model/product.model';
import { IBusiness } from 'app/shared/model/business.model';
import { IQuotationStatus } from 'app/shared/model/quotation-status.model';

export interface IQuotation {
  id?: number;
  startOfRiskPeriod?: dayjs.Dayjs;
  lengthOfRiskPeriod?: number;
  depth?: number;
  claimsFrequency?: number;
  insuredValue?: number;
  bestPremium?: number;
  insuredRate?: number;
  insuredPremium?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  season?: ISeason | null;
  farmer?: IFarmer | null;
  product?: IProduct | null;
  business?: IBusiness | null;
  quotationStatus?: IQuotationStatus | null;
}

export const defaultValue: Readonly<IQuotation> = {};
