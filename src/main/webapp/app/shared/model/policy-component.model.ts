import dayjs from 'dayjs';
import { IPComponent } from 'app/shared/model/p-component.model';
import { IInsuredPolicy } from 'app/shared/model/insured-policy.model';

export interface IPolicyComponent {
  id?: number;
  componentValue?: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  component?: IPComponent | null;
  policies?: IInsuredPolicy[] | null;
}

export const defaultValue: Readonly<IPolicyComponent> = {};
