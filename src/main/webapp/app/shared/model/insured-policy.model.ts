import dayjs from 'dayjs';
import { IFarmer } from 'app/shared/model/farmer.model';
import { IFarm } from 'app/shared/model/farm.model';
import { IPolicyComponent } from 'app/shared/model/policy-component.model';
import { IQuotation } from 'app/shared/model/quotation.model';
import { PolicyStatus } from 'app/shared/model/enumerations/policy-status.model';

export interface IInsuredPolicy {
  id?: number;
  policyNumber?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  premiumAmount?: number;
  coverageAmount?: number;
  status?: keyof typeof PolicyStatus;
  insuredFarmer?: IFarmer | null;
  farm?: IFarm | null;
  components?: IPolicyComponent[] | null;
  quotation?: IQuotation | null;
}

export const defaultValue: Readonly<IInsuredPolicy> = {};
