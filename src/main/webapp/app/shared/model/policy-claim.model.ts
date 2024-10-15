import dayjs from 'dayjs';
import { IInsuredPolicy } from 'app/shared/model/insured-policy.model';
import { ClaimStatus } from 'app/shared/model/enumerations/claim-status.model';

export interface IPolicyClaim {
  id?: number;
  claimNumber?: string;
  claimDate?: dayjs.Dayjs;
  amountClaimed?: number;
  status?: keyof typeof ClaimStatus;
  policy?: IInsuredPolicy | null;
}

export const defaultValue: Readonly<IPolicyClaim> = {};
