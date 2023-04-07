import dayjs from 'dayjs';
import { IStore } from 'app/shared/model/store.model';
import { IStock } from 'app/shared/model/stock.model';
import { AlertType } from 'app/shared/model/enumerations/alert-type.model';

export interface IAlert {
  id?: number;
  datetime?: string | null;
  type?: AlertType | null;
  rectificationDatetime?: string | null;
  provider?: IStore | null;
  stock?: IStock | null;
}

export const defaultValue: Readonly<IAlert> = {};

export interface IAlertCriteria {
  rectificationDatetime?: {};
}

export class AlertCriteria implements IAlertCriteria {
  constructor(
    public rectificationDatetime?: {},
  ) {}
}